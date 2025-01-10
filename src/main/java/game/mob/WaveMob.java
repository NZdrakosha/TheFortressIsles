package game.mob;

import me.drakosha.thefortressisles.TheFortressIsles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static game.Game.startBossBarTimer;
import static game.mob.SpawnMob.*;

public class WaveMob {
    private static int deley = 200;
    private static int waveCount = 1;
    public static void wave(){
        if (waveCount == 1){
            teamSpawnMob();
            waveCount++;
        }
            waveMobSpawn(deley, waveCount);
            startBossBarTimer(deley);
    }

    private static void waveMobSpawn(int second, int wave){
        Bukkit.getScheduler().runTaskLater(TheFortressIsles.getInstance(), () -> {
            countWaveMob(wave);

            if (wave >= 5){
                for (Player player : Bukkit.getOnlinePlayers()) player.sendMessage("Вы прошли все волны");
                return;
            }
            deley += 30;
            waveCount++;
        },  20L * second);
    }

    private static void countWaveMob(int count){
        for (int i = 1; i <= count; i++){
            teamSpawnMob();
        }
    }
}
