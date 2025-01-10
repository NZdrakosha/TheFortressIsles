package me.drakosha.thefortressisles;

import game.InitializationMap;
import game.command.AdminCommand;
import game.PlayerEvent;
import game.command.AdminTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;




public final class TheFortressIsles extends JavaPlugin {
    private static  TheFortressIsles instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        InitializationMap.init();

        registerCommands();
        registerEvent();
    }
    public static TheFortressIsles getInstance(){
        return instance;
    }

    @Override
    public void onDisable() {
    }

    private void registerCommands(){
        getCommand("admin").setExecutor(new AdminCommand());
        getCommand("admin").setTabCompleter(new AdminTabCompleter());
    }
    private void registerEvent(){
        Bukkit.getPluginManager().registerEvents(new PlayerEvent(), this);
    }
}
