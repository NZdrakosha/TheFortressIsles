package game;

import me.drakosha.thefortressisles.TheFortressIsles;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static game.ConfigManager.getColor;
import static game.gui.InventorySelectTeam.glassList;
import static game.mine.Mine.*;
import static game.mob.CreateEntity.*;
import static game.mob.WaveMob.wave1;

public class Game {
    private static BossBar bossBar;
    public static ArrayList<Location> locationSign = new ArrayList<>();
    public static Map<UUID, Double> balance = new HashMap<>();

    public static Map<String, Location> locationSpawnPlayer = new HashMap<>();
    public static HashMap<String, Boolean> equippedTeam = new HashMap<>();
    public static Map<String, UUID> colorTeamPlayer = new HashMap<>();



    public static Map<String, AtomicInteger> minute = new HashMap<>();
    public static Map<String, AtomicInteger> seconds = new HashMap<>();


    public static Map<UUID, String> activeTeam = new HashMap<>();
    public static Map<String, Boolean> canRepairMine = new HashMap<>();


    public static void giveStartLot(Player player){
        player.setGameMode(GameMode.SURVIVAL);
        player.setSaturation(20.0f);
        player.setFoodLevel(20);
        player.getInventory().clear();
        player.getInventory().setItem(0, giveUnbreakableItem(new ItemStack(Material.WOOD_SWORD)));
        player.getInventory().setItem(1, giveUnbreakableItem(new ItemStack(Material.WOOD_PICKAXE)));
        player.getInventory().setItem(2, giveUnbreakableItem(new ItemStack(Material.STONE_AXE)));
        player.setExp(0);
        player.setLevel(0);

        player.getEnderChest().clear();
    }
    public static void startTimerForRepairMine(){
        Bukkit.getScheduler().runTaskTimer(TheFortressIsles.getInstance(), () -> {
            for (String color : teamColor) {
                if (!canRepairMine.get(color)) {
                    seconds.get(color).getAndDecrement();
                    if (minute.get(color).get() == 0 && seconds.get(color).get() == 0) {
                        canRepairMine.put(color, true);
                    }
                    if (seconds.get(color).get() == 0 && minute.get(color).get() != 0) {
                        minute.get(color).getAndDecrement();
                        seconds.get(color).set(59);
                    }
                }
            }
        for (String color : teamColor) {
            Location location =  coordinatesArmorStand.get(color);
            if (canRepairMine.get(color)){
                createArmorStand(location, "Вы можете обновить шахту", color);
            }else if(!canRepairMine.get(color)) {
                createArmorStand(location, "Время до обновления шахты = " + minute.get(color) + ":" + seconds.get(color), color);
                }
            }

        }, 0L, 20L);

    }

    public static ItemStack giveUnbreakableItem(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        meta.spigot().setUnbreakable(true);
        item.setItemMeta(meta);
       return item;
    }
    public static void selectTeam(Player player, ItemStack item) {
        for (String colors : teamColor) {
            if (item.getType() == glassList.get(colors).getType() &&
                    item.getItemMeta().equals(glassList.get(colors).getItemMeta()) &&
                    !equippedTeam.get(colors)) {

                if (item.getType() == glassList.get(colors).getType() &&
                        item.getItemMeta().equals(glassList.get(colors).getItemMeta()) &&
                        !equippedTeam.get(colors)) {

                    if (activeTeam.containsKey(player.getUniqueId())) {
                        String prevColor = activeTeam.get(player.getUniqueId());
                        colorTeamPlayer.remove(prevColor);
                        equippedTeam.put(prevColor, false);
                    }

                    player.closeInventory();
                    selectTeamProperties(colors, player);
                    break;
                }
            }
        }
    }

    public static void selectTeamProperties(String color, Player player){
        activeTeam.put(player.getUniqueId(), color);
        player.sendMessage("Вы выбрали " + getColor(color) + " цвет команды");
        colorTeamPlayer.put(color, player.getUniqueId());
        equippedTeam.put(color, true);
    }

    public static int getAmountItem(ItemStack item, Player player) {
        if (item == null || player == null) {
            return 0;
        }
        int count = 0;
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null || itemStack.getType() == Material.AIR){
                continue;
            }

            if (item.getType() == itemStack.getType()) {
                count += itemStack.getAmount();
            }
        }
        return count;
    }
    public static void removeItemsFromInventory(Player player, ItemStack item, int amount) {
        int remaining = amount;
        for (ItemStack stack : player.getInventory().getContents()) {
            if (stack != null && stack.getType() == item.getType()) {
                int stackAmount = stack.getAmount();

                if (stackAmount > remaining) {
                    stack.setAmount(stackAmount - remaining);
                    break;
                } else {
                    player.getInventory().remove(stack);
                    remaining -= stackAmount;
                }

                if (remaining <= 0) {
                    break;
                }
            }
        }
    }

    public static double getMoneyGive(ItemStack item) {
        if (item == null) {
            return 0.0;
        }

        Material type = item.getType();
        double num = 0;

        if (type == Material.IRON_INGOT) num += 20.0;
        else if (type == Material.LOG)  num += 15.0;
        else if (type == Material.COAL) num += 15.0;
        else if (type == Material.LEATHER) num += 10.0;
        else if (type == Material.COBBLESTONE) num += 0.5;
        else if (type == Material.INK_SACK && item.getDurability() == 4)  num += 0.1;
        else if (type == Material.REDSTONE) num += 0.2;


        return num;
    }
    public static void startBossBarTimer(int seconds, Player player, String lore) {

        bossBar = Bukkit.createBossBar(lore, BarColor.WHITE, BarStyle.SOLID);
        bossBar.addPlayer(player);
        bossBar.setVisible(true);


        new BukkitRunnable() {
            int ticksLeft = seconds * 20;
            double progress = 1.0;


            @Override
            public void run() {
                if (ticksLeft <= 0) {
                        bossBar.removeAll();
                        wave1(player);
                        cancel();
                }
                ticksLeft--;
                progress -= 1.0 / (seconds * 20);
                bossBar.setProgress(progress);
            }
        }.runTaskTimer(TheFortressIsles.getInstance(), 0L, 1L);
    }
}


