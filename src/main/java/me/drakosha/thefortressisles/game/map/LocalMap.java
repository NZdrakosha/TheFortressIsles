package me.drakosha.thefortressisles.game.map;

import me.drakosha.thefortressisles.game.util.FileUtil;
import me.drakosha.thefortressisles.game.util.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;

public class LocalMap implements GameMap{
    private final File sourceFolder;
    private File activeWorldFolder;
    private World bukkitWorld;
    private final String worldName;
    private final String gameId;

    public LocalMap(File worldFolder, String worldName, boolean load, String gameId){
        this.sourceFolder = new File(worldFolder, worldName);
        this.worldName = worldName;
        this.gameId = gameId;
        if (load) load();


    }
    @Override
    public boolean load() { 
        if (isLoaded()) return true;
        activeWorldFolder = new File(Bukkit.getWorldContainer(), sourceFolder.getName() + "_active_" + gameId);
        if (!activeWorldFolder.exists()) activeWorldFolder.mkdirs();
        try {
            FileUtil.copyWorld(sourceFolder, activeWorldFolder);
        }catch (Exception e){
            Bukkit.getLogger().severe("Error " + sourceFolder.getName());
            e.printStackTrace(System.err);
        }
        WorldCreator creator = new WorldCreator(activeWorldFolder.getName());

        bukkitWorld = creator.createWorld();
        if (bukkitWorld != null) bukkitWorld.setAutoSave(false);

        return isLoaded();
    }

    @Override
    public void unload() {
        WorldUtil.unloadWorld(bukkitWorld);
        if (activeWorldFolder != null) WorldUtil.deleteWorld(activeWorldFolder);
        bukkitWorld = null;
        activeWorldFolder = null;

    }

    @Override
    public boolean deleteSource() {
        unload();
        return load();
    }

    @Override
    public boolean isLoaded() {
        return bukkitWorld != null;
    }

    @Override
    public World getWorld() {
        return bukkitWorld;
    }

    @Override
    public String getWorldName() {
        return worldName;
    }
}
