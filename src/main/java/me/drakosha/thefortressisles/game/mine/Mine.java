package me.drakosha.thefortressisles.game.mine;

import me.drakosha.thefortressisles.game.Game;
import me.drakosha.thefortressisles.game.util.FileUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class Mine {
    public List<Material> materialListBaseMine;
    public List<Material> materialListSuperMine;
    private final Random random = new Random();

    public Mine() {
              materialListBaseMine = new ArrayList<>(Arrays.asList(
                Material.STONE,
                Material.COAL_ORE,
                Material.IRON_ORE,
                Material.LOG,
                Material.LAPIS_ORE,
                Material.DIRT,
                Material.REDSTONE_ORE
        ));

        materialListSuperMine = new ArrayList<>(Arrays.asList(
                Material.STONE,
                Material.GOLD_ORE,
                Material.EMERALD_ORE,
                Material.DIAMOND_ORE,
                Material.OBSIDIAN
        ));
    }

    public void fillBaseMine(World world) {
        for (String color : Game.allColor) {
            Location loc1 = FileUtil.getLocationZoneMine(color, 1, world);
            Location loc2 = FileUtil.getLocationZoneMine(color, 2, world);

            int minX = (int) Math.min(loc1.getX(), loc2.getX());
            int maxX = (int) Math.max(loc1.getX(), loc2.getX());

            int minY = (int) Math.min(loc1.getY(), loc2.getY());
            int maxY = (int) Math.max(loc1.getY(), loc2.getY());

            int minZ = (int) Math.min(loc1.getZ(), loc2.getZ());
            int maxZ = (int) Math.max(loc1.getZ(), loc2.getZ());

            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        Block block = world.getBlockAt(x, y, z);
                        block.setType(materialListBaseMine.get(randomItemBaseMine()));
                    }
                }
            }
        }
    }

    public void fillSuperMine(World world) {
        Location loc1 = FileUtil.getLocationZoneSuperMine(1, world);
        Location loc2 = FileUtil.getLocationZoneSuperMine(2, world);

        int minX = (int) Math.min(loc1.getX(), loc2.getX());
        int maxX = (int) Math.max(loc1.getX(), loc2.getX());

        int minY = (int) Math.min(loc1.getY(), loc2.getY());
        int maxY = (int) Math.max(loc1.getY(), loc2.getY());

        int minZ = (int) Math.min(loc1.getZ(), loc2.getZ());
        int maxZ = (int) Math.max(loc1.getZ(), loc2.getZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    block.setType(materialListSuperMine.get(randomItemSuperMine()));
                }
            }
        }
    }

    public Set<Player> playerInMine(World world) {
        Set<Player> players = new HashSet<>();
        for (String color : Game.allColor) {
            Location loc1 = FileUtil.getLocationZoneMine(color, 1, world);
            Location loc2 = FileUtil.getLocationZoneMine(color, 2, world);

            int minX = (int) Math.min(loc1.getX(), loc2.getX());
            int maxX = (int) Math.max(loc1.getX(), loc2.getX());

            int minY = (int) Math.min(loc1.getY(), loc2.getY());
            int maxY = (int) Math.max(loc1.getY(), loc2.getY());

            int minZ = (int) Math.min(loc1.getZ(), loc2.getZ());
            int maxZ = (int) Math.max(loc1.getZ(), loc2.getZ());

            for (Player player : world.getPlayers()) {
                Location loc = player.getLocation();

                if (loc.getBlockX() >= minX && loc.getBlockX() <= maxX &&
                        loc.getBlockY() >= minY && loc.getBlockY() <= maxY &&
                        loc.getBlockZ() >= minZ && loc.getBlockZ() <= maxZ) {

                    players.add(player);
                }
            }
        }
        return players;
    }

    public static boolean isBlockInMine(Block block, Game game, Player player) {
        String color = game.getColorPlayer(player);
        Location loc1 = FileUtil.getLocationZoneMine(color, 1, block.getWorld());
        Location loc2 = FileUtil.getLocationZoneMine(color, 2, block.getWorld());


        int minX = (int) Math.min(loc1.getX(), loc2.getX());
        int maxX = (int) Math.max(loc1.getX(), loc2.getX());

        int minY = (int) Math.min(loc1.getY(), loc2.getY());
        int maxY = (int) Math.max(loc1.getY(), loc2.getY());

        int minZ = (int) Math.min(loc1.getZ(), loc2.getZ());
        int maxZ = (int) Math.max(loc1.getZ(), loc2.getZ());

        Location loc = block.getLocation();

        return loc.getBlockX() < minX || loc.getBlockX() > maxX ||
                loc.getBlockY() < minY || loc.getBlockY() > maxY ||
                loc.getBlockZ() < minZ || loc.getBlockZ() > maxZ;
    }
    public static boolean isBlockInSuperMine(Block block) {
        Location loc1 = FileUtil.getLocationZoneSuperMine(1, block.getWorld());
        Location loc2 = FileUtil.getLocationZoneSuperMine(2, block.getWorld());


        int minX = (int) Math.min(loc1.getX(), loc2.getX());
        int maxX = (int) Math.max(loc1.getX(), loc2.getX());

        int minY = (int) Math.min(loc1.getY(), loc2.getY());
        int maxY = (int) Math.max(loc1.getY(), loc2.getY());

        int minZ = (int) Math.min(loc1.getZ(), loc2.getZ());
        int maxZ = (int) Math.max(loc1.getZ(), loc2.getZ());

        Location loc = block.getLocation();

        return loc.getBlockX() < minX || loc.getBlockX() > maxX ||
                loc.getBlockY() < minY || loc.getBlockY() > maxY ||
                loc.getBlockZ() < minZ || loc.getBlockZ() > maxZ;
    }

    private int randomItemBaseMine() {
        int rang = random.nextInt(100) + 1;

        if (rang <= 70) return 0;
        else if (rang <= 75) return 1;
        else if (rang <= 80) return 2;
        else if (rang <= 85) return 3;
        else if (rang <= 90) return 4;
        else if (rang == 95) return 5;
        else return 6;

    }

    private int randomItemSuperMine() {
        int rang = random.nextInt(100) + 1;

        if (rang <= 92) return 0;
        else if (rang <= 95) return 1;
        else if (rang <= 97) return 2;
        else if (rang <= 99) return 3;
        else return 4;
    }
}
