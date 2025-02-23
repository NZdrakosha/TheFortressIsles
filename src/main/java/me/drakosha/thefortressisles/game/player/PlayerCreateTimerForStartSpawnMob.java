package me.drakosha.thefortressisles.game.player;

import me.drakosha.thefortressisles.TheFortressIsles;
import me.drakosha.thefortressisles.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerCreateTimerForStartSpawnMob {
    private final BossBar bossBar;
    private int secondToEnd;
    private final Game game;

    public PlayerCreateTimerForStartSpawnMob(Game game) {
        bossBar = Bukkit.createBossBar("До начала волны", BarColor.WHITE, BarStyle.SOLID);
        secondToEnd = 10;

        this.game = game;
    }

    public void startBossBarTimer() {
        if (game.getPlayerInGame().isEmpty()) return;
        for (Player p : game.getPlayerInGame()) {
            if (!bossBar.getPlayers().contains(p))bossBar.addPlayer(p);
        }
        new BukkitRunnable() {
            int totalTick = secondToEnd * 20;
            double progress = 1.0;

            @Override
            public void run() {
                if (totalTick <= 0) {
                    bossBar.removeAll();
                    game.getWaveMob().spawnMob(game);

                    secondToEnd += 40;
                    cancel();
                    return;
                }
                totalTick--;
                progress -= 1.0 / (secondToEnd * 20);
                progress = Math.max(0.0, Math.min(1.0, progress));
                bossBar.setProgress(progress);
            }
        }.runTaskTimer(TheFortressIsles.getInstance(), 0L, 1L);
    }
}
