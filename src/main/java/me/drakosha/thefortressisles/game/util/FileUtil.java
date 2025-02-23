package me.drakosha.thefortressisles.game.util;

import lombok.experimental.UtilityClass;
import me.drakosha.thefortressisles.TheFortressIsles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@UtilityClass
public class FileUtil {
    private final FileConfiguration config = TheFortressIsles.getInstance().getConfig();

    public void copyWorld(File source, File target) {
        try {
            ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists())
                        target.mkdirs();
                    String files[] = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyWorld(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {

        }
    }

    public Location getSpawnLocationNpc() {
        return locationPropertiesConfig("maps.lobby.locationspawnnpcstartgame.villager", Bukkit.getWorld("lobby"));
    }

    public String getNameColor(String color) {
        return config.getString("more.item.name.glass." + color, "null");
    }

    public Location getLocationSpawnNpcUpdate(int id, World world) {
        return locationSetYAW("maps.map.location.spawn.npc.update." + id, world, 180);
    }

    public Location getLocationSpawnPlayer(String color, World world) {
        return locationSetYAW("maps.map.location.spawn." + color, world, 90);
    }

    public Location getLocationSpawnNpcSell(int id, World world) {
        return locationSetYAW("maps.map.location.spawn.npc.sell." + id, world, 0);
    }

    public Location getLocationZoneMine(String color, int numZone, World world) {
        return locationPropertiesConfig("maps.map.location.mine.base.zone." + color + "." + numZone, world);
    }

    public Location getLocationZoneSuperMine(int numZone, World world) {
        return locationPropertiesConfig("maps.map.location.mine.supermine.zone." + numZone, world);
    }

    public Location getLocationArmorStand(int num, World world) {
        return locationPropertiesConfig("maps.map.location.armorstand." + num, world);
    }

    public int getPriceUpdate(int num) {
        return config.getInt("more.price." + num, 10000);
    }

    public Location getLocationLighthouse(String color, World world) {
        return locationPropertiesConfig("maps.map.location.lighthouse." + color, world);
    }

    public Location getLocationSpawnMob(String color, int num, World world) {
        return locationPropertiesConfig("maps.map.location.mob.spawnlocation." + color + "." + num, world);
    }

    public Location getLocationSign(World world) {
        return locationPropertiesConfig("maps.map.location.baseteleportsign", world);
    }

    public Location getLocationSign(int num, World world) {
        return locationPropertiesConfig("maps.map.location.signteleportsupersign." + num, world);
    }

    public Location getLocationSpawnSuperMine(World world) {
        return locationSetYAW("maps.map.location.mine.supermine.loc", world, 90);
    }

    private Location locationPropertiesConfig(String path, World world) {
        if (world == null) return null;
        Map<String, Object> objectLocation = config.getConfigurationSection(path).getValues(false);
        double x = ((Number) objectLocation.getOrDefault("x", 0.0)).doubleValue();
        double y = ((Number) objectLocation.getOrDefault("y", 0.0)).doubleValue();
        double z = ((Number) objectLocation.getOrDefault("z", 0.0)).doubleValue();
        return new Location(world, x, y, z);
    }

    private Location locationSetYAW(String path, World world, float yaw) {
        Location loc = locationPropertiesConfig(path, world);
        if (loc != null) loc.setYaw(yaw);
        return loc;
    }
}
