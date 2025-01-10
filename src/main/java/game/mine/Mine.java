package game.mine;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static game.Game.*;
import static game.mob.CreateEntity.*;
import static game.command.AdminCommand.*;
import static game.gui.InventorySelectTeam.*;
import static game.mob.Lighthouse.*;
import static game.mob.SpawnMob.*;

public class Mine {
    public static Map<String, Location> coordinatesArmorStand = new HashMap<>();
    public static ArrayList<Location> signTeleportSuperMine = new ArrayList<>();
    public static Map<String, Location> zoneMineTeam1 = new HashMap<>();
    public static Map<String, Location> zoneMineTeam2 = new HashMap<>();
    public static Random random = new Random();
    public static ArrayList<Material> materialMineList = new ArrayList<>();

    public static ArrayList<Material> materialListSuperMine = new ArrayList<>();

    public static Location zoneSuperMine1;
    public static Location zoneSuperMine2;

    public static ArrayList<String> teamColor = new ArrayList<>();

    public static void fillMine() {
        for (int i = 0; i < teamColor.size(); i++) {
            Location zone1 = zoneMineTeam1.get(teamColor.get(i));
            Location zone2 = zoneMineTeam2.get(teamColor.get(i));
            int minX = (int) Math.min(zone1.getX(), zone2.getX());
            int maxX = (int) Math.max(zone1.getX(), zone2.getX());

            int minY = (int) Math.min(zone1.getY(), zone2.getY());
            int maxY = (int) Math.max(zone1.getY(), zone2.getY());

            int minZ = (int) Math.min(zone1.getZ(), zone2.getZ());
            int maxZ = (int) Math.max(zone1.getZ(), zone2.getZ());

            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        Block block = zone1.getWorld().getBlockAt(x, y, z);
                        block.setType(materialMineList.get(itemInMine()));
                    }
                }
            }
        }
    }

    public static void fillSuperMine(){
        Location zone1 = zoneSuperMine1;
        Location zone2 = zoneSuperMine2;

        int minX = (int) Math.min(zone1.getX(), zone2.getX());
        int maxX = (int) Math.max(zone1.getX(), zone2.getX());

        int minY = (int) Math.min(zone1.getY(), zone2.getY());
        int maxY = (int) Math.max(zone1.getY(), zone2.getY());

        int minZ = (int) Math.min(zone1.getZ(), zone2.getZ());
        int maxZ = (int) Math.max(zone1.getZ(), zone2.getZ());

        for (int x = minX; x <= maxX; x++){
            for (int y = minY; y <= maxY; y++){
                for (int z = minZ; z <= maxZ; z++){
                    Block block = zone1.getWorld().getBlockAt(x, y, z);
                    block.setType(materialListSuperMine.get(itemInSuperMine()));
                }
            }
        }
    }
    public static void repairZones(String color, Player player){
        if (!activeGame){
            player.sendMessage("Игра не началась");
            return;
        }
        if (!canRepairMine.get(color) && color.equalsIgnoreCase(activeTeam.get(player.getUniqueId()))){
            player.sendMessage("Вы не можете обновлять шахту ещё " + minute.get(color) + ":" + seconds.get(color));
            return;
        }
        Location zone1 = zoneMineTeam1.get(color);
        Location zone2 = zoneMineTeam2.get(color);

        int minX = (int) Math.min(zone1.getX(), zone2.getX());
        int maxX = (int) Math.max(zone1.getX(), zone2.getX());

        int minY = (int) Math.min(zone1.getY(), zone2.getY());
        int maxY = (int) Math.max(zone1.getY(), zone2.getY());

        int minZ = (int) Math.min(zone1.getZ(), zone2.getZ());
        int maxZ = (int) Math.max(zone1.getZ(), zone2.getZ());

        for (int x = minX; x <= maxX; x++){
            for (int y = minY; y <= maxY; y++){
                for (int z = minZ; z <= maxZ; z++){
                    Block block = zone1.getWorld().getBlockAt(x, y, z);
                    block.setType(materialMineList.get(itemInMine()));
                }
            }
        }
        canRepairMine.put(color, false);
        Bukkit.getLogger().info("canRepairMine = " + canRepairMine.get(color));

        minute.put(color, new AtomicInteger(2));
        seconds.put(color, new AtomicInteger(60));
    }

    private static int itemInMine() {
        int rang = random.nextInt(100) + 1;

        if (rang <= 70) return 0;
        else if (rang <= 75) return 1;
         else if (rang <= 80) return 2;
         else if (rang <= 85) return 3;
         else if (rang <= 90) return 4;
         else if (rang == 95) return 5;
         else return 6;

    }
    private static int itemInSuperMine(){
        int rang = random.nextInt(100) + 1;
        if (rang <= 92) return 0;
        else if (rang <= 95) return 1;
        else if (rang <= 97) return 2;
        else if (rang <= 99) return 3;
        else return 4;
    }

}
