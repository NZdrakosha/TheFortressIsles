package me.drakosha.thefortressisles;

import lombok.Getter;
import me.drakosha.thefortressisles.game.GameManager;
import me.drakosha.thefortressisles.game.player.PlayerEvent;
import me.drakosha.thefortressisles.game.player.PlayerTabAndChatFilter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class TheFortressIsles extends JavaPlugin {
    @Getter
    private static TheFortressIsles instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfigPlugin();

        instance = this;

        registerEvent();
    }

    private void registerEvent() {
        GameManager gameManager = new GameManager();
        Bukkit.getPluginManager().registerEvents(new PlayerEvent(gameManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerTabAndChatFilter(), this);

    }

    private void reloadConfigPlugin() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (configFile.exists()) configFile.delete();

        saveResource("config.yml", false);
        reloadConfig();
    }
}
