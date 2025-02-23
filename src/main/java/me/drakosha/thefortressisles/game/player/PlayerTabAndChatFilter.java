package me.drakosha.thefortressisles.game.player;

import me.drakosha.thefortressisles.TheFortressIsles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerTabAndChatFilter implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateTab();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        updateTab();
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        updateTab();
    }

    @EventHandler
    public void onPlayerCharMessage(AsyncPlayerChatEvent event){
        for (Player p : event.getPlayer().getWorld().getPlayers()) p.sendMessage("<" + event.getPlayer().getName() + "> " + event.getMessage());

        event.setCancelled(true);
    }


    private void updateTab() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getScoreboard().clearSlot(org.bukkit.scoreboard.DisplaySlot.PLAYER_LIST);

            for (Player p : player.getWorld().getPlayers()) player.showPlayer(TheFortressIsles.getInstance(), p);


            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.getWorld().equals(player.getWorld())) player.hidePlayer(TheFortressIsles.getInstance(), p);
            }
        }
    }
}
