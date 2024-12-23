package game.mob;

import me.drakosha.thefortressisles.TheFortressIsles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

import static game.mine.Mine.teamColor;
import static game.mob.SpawnMob.*;

public class Lighthouse {
    public static Map<String, Location> lighthouseLocationTeam = new HashMap<>();
    public static Map<String, Double> healthTeam = new HashMap<>();

    public Location getLocation(String color){
        return this.lighthouseLocationTeam.get(color);
    }

    public static double getHealth(String color){
        return healthTeam.get(color);
    }

    public static void reduceHealth() {
             new BukkitRunnable() {
            @Override
            public void run() {
                for (String color : teamColor) {
                    Location currentLoc = lighthouseLocationTeam.get(color);
                    for (Entity entity : currentLoc.getWorld().getNearbyEntities(currentLoc, 5, 4, 5)) {
                        if (entity.getType() == EntityType.ZOMBIE) {
                            double putHealthMap = healthTeam.get(color) - 1;
                            healthTeam.put(color, putHealthMap);
                        }
                    }
                    if (healthTeam.get(color) <= 0) {
                        Zombie zombie = (Zombie) Bukkit.getEntity(aliveZombieTeam.get(color));
                        zombie.remove();
                        aliveZombieTeam.remove(color);
                        cancel();
                        return;
                    }
                }
            }
        }.runTaskTimer(TheFortressIsles.getInstance(), 0L, 20L);
    }
}
