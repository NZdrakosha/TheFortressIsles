package me.drakosha.thefortressisles.game.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.*;

@UtilityClass
public class WorldUtil {

    public boolean unloadWorld(World world) {
        return world != null && Bukkit.getServer().unloadWorld(world, false);
    }

    public boolean deleteWorld(File file) {
        if (file.exists()) {
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (file.delete());
    }
}
