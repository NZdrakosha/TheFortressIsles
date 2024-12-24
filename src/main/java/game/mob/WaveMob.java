package game.mob;

import me.drakosha.thefortressisles.TheFortressIsles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static game.Game.startBossBarTimer;
import static game.mob.SpawnMob.*;

public class WaveMob {
    private static int deley = 200;
    private static int waveCount = 1;
    public static void wave1(Player p){
        if (waveCount == 1){
            teamSpawnMob();
            teamSpawnMob();
            waveCount++;
        }
            waveMobSpawn(deley, waveCount);
                startBossBarTimer(deley, p, "До начала волны");
    }

    private static void waveMobSpawn(int second, int wave){
        Bukkit.getScheduler().runTaskLater(TheFortressIsles.getInstance(), () -> {
            if (wave == 2){
                teamSpawnMob();
                teamSpawnMob();
                teamSpawnMob();
            }
            else if (wave == 3){
                teamSpawnMob();
                teamSpawnMob();
                teamSpawnMob();
                teamSpawnMob();

            }
            else if (wave == 4){
                teamSpawnMob();
                teamSpawnMob();
                teamSpawnMob();
                teamSpawnMob();
                teamSpawnMob();
                teamSpawnMob();

            }
            deley += 30;
            waveCount++;
        },  20 * second);
    }
}
