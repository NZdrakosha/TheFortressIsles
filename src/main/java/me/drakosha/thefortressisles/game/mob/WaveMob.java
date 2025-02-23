package me.drakosha.thefortressisles.game.mob;

import lombok.Getter;
import me.drakosha.thefortressisles.TheFortressIsles;
import me.drakosha.thefortressisles.game.Game;
import me.drakosha.thefortressisles.game.player.lighthouse.Lighthouse;
import net.minecraft.server.v1_12_R1.EntityZombie;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class WaveMob {
    private int countMob;
    private final SpawnEntities spawnEntities;
    @Getter
    private final Map<String, ArrayList<UUID>> aliveZombie;
    private final Lighthouse lighthouse;
    boolean allMobSpawn;
    private int currentMobCount;

    public WaveMob(Lighthouse lighthouse) {
        countMob = 6;
        aliveZombie = new HashMap<>();
        spawnEntities = new SpawnEntities();
        this.lighthouse = lighthouse;
    }

    public void spawnMob(Game game) {
        allMobSpawn = false;
        currentMobCount = countMob;
        new BukkitRunnable() {

            @Override
            public void run() {
                for (String color : game.getColorTeamPlayer().keySet()) {
                    ArrayList<UUID> spawnZombieList = aliveZombie.getOrDefault(color, new ArrayList<>());
                    UUID zombieUUID = spawnEntities.createZombie(color, randomSpawnLocationMob(), game.getWorld(), lighthouse, game);
                    spawnZombieList.add(zombieUUID);

                    aliveZombie.put(color, spawnZombieList);
                }
                currentMobCount--;
                if (currentMobCount <= 0) {
                    allMobSpawn = true;
                    cancel();
                }
            }
        }.runTaskTimer(TheFortressIsles.getInstance(), 0L, 60L);
        countMob += 6;
    }

    public void killZombie(EntityZombie zombie, String colorTeam, Game game) {
        ArrayList<UUID> currentZombieList = aliveZombie.get(colorTeam);
        if (currentZombieList.contains(zombie.getUniqueID())) {
            currentZombieList.remove(zombie.getUniqueID());

            if (currentZombieList.isEmpty() && allMobSpawn) {
                aliveZombie.remove(colorTeam);
            } else aliveZombie.put(colorTeam, currentZombieList);

            if (aliveZombie.isEmpty()) game.getStartTimer().startBossBarTimer();

        }
    }

    public int randomSpawnLocationMob() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }
}
