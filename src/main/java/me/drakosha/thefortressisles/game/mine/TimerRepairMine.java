package me.drakosha.thefortressisles.game.mine;

import me.drakosha.thefortressisles.TheFortressIsles;
import me.drakosha.thefortressisles.game.Game;
import me.drakosha.thefortressisles.game.mob.SpawnEntities;
import me.drakosha.thefortressisles.game.util.FileUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class TimerRepairMine {

    public void startTimerForRepairMine(Game game) {
        SpawnEntities spawnEntities = new SpawnEntities();
        Mine mine = new Mine();
        new BukkitRunnable() {
            int minute = 5;
            int second = 0;

            @Override
            public void run() {
                if (minute <= 0 && second <= 0) {
                    Set<Player> players = mine.playerInMine(game.getWorld());
                    if (!players.isEmpty()) {
                        for (Player player : players)
                            player.teleport(FileUtil.getLocationSpawnPlayer(game.getColorPlayer(player), game.getWorld()));
                    }
                    mine.fillBaseMine(game.getWorld());
                    minute = 5;
                    second = 0;
                } else {
                    if (second == 0) {
                        minute--;
                        second = 59;

                    } else {
                        second--;
                    }

                    if (second < 10) {
                        spawnEntities.createArmorStand(game.getWorld(), "До обновления шахты " + minute + ":0" + second, game);
                    } else {
                        spawnEntities.createArmorStand(game.getWorld(), "До обновления шахты " + minute + ":" + second, game);
                    }

                }
            }
        }.runTaskTimer(TheFortressIsles.getInstance(), 0L, 20L);
    }
}

