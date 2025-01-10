package game.mob;

import me.drakosha.thefortressisles.TheFortressIsles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static game.Game.activeTeam;
import static game.mob.SpawnMob.*;

public class Lighthouse {
    public static Map<String, Location> lighthouseLocationTeam = new ConcurrentHashMap<>();
    public static Map<String, Double> healthTeam = new ConcurrentHashMap<>();

    public Location getLocation(String color){ return this.lighthouseLocationTeam.get(color); }

    public static double getHealth(String color){
        return healthTeam.get(color);
    }

    public static void reduceHealth() {
        new BukkitRunnable() {
            boolean canAttackLighthouse;
            @Override
            public void run() {
                for (UUID player : activeTeam.keySet()) {
                    String color = activeTeam.get(player);
                    if (color == null) continue;
                    Location currentLoc = lighthouseLocationTeam.get(color);
                    for (Entity entity : currentLoc.getWorld().getNearbyEntities(currentLoc, 5, 4, 5)) {
                        if (entity.getType() == EntityType.ZOMBIE && canAttackLighthouse) {
                            double putHealthMap = healthTeam.get(color) - 1;
                            healthTeam.put(color, putHealthMap);
                            canAttackLighthouse = false;
                        }
                    }
                    if (healthTeam.get(color) <= 0) {
                        UUID zombieId = aliveZombieTeam.remove(color);
                        if (zombieId != null) {
                            Entity zombie = Bukkit.getEntity(zombieId);
                            if (zombie instanceof Zombie) {
                                zombie.remove();
                            }
                        }
                        aliveZombieTeam.remove(color);
                        activeTeam.remove(player);
                        cancel();
                        return;
                    }
                }
                canAttackLighthouse = true;
            }
        }.runTaskTimer(TheFortressIsles.getInstance(), 0L, 20L);
    }
}
