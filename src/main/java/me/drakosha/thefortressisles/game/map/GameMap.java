package me.drakosha.thefortressisles.game.map;

import org.bukkit.World;

public interface GameMap {
    boolean load();
    void unload();
    boolean deleteSource();

    boolean isLoaded();
    World getWorld();
    String getWorldName();
}
