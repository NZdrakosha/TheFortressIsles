package game;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import static game.Game.giveUnbreakableItem;

public class ItemUtil {


    public static boolean isPickaxe(ItemStack itemStack){
        if (itemStack == null || itemStack.getType() == null){
            return false;
        }
        Material type = itemStack.getType();
        return
                type == Material.STONE_PICKAXE ||
                        type == Material.IRON_PICKAXE ||
                        type == Material.GOLD_PICKAXE ||
                        type == Material.DIAMOND_PICKAXE;
    }

    public static boolean isAxe(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == null) {
            return false;
        }
        Material type = itemStack.getType();
        return
                type == Material.IRON_AXE ||
                        type == Material.GOLD_AXE ||
                        type == Material.DIAMOND_AXE;

    }

    public static boolean isSword(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == null) {
            return false;
        }
        Material type = itemStack.getType();
        return
                type == Material.STONE_SWORD ||
                        type == Material.IRON_SWORD ||
                        type == Material.GOLD_SWORD ||
                        type == Material.DIAMOND_SWORD;
    }

    public static boolean isHelmet(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == null) {
            return false;
        }
        Material type = itemStack.getType();
        return type == Material.LEATHER_HELMET ||
                type == Material.IRON_HELMET ||
                type == Material.GOLD_HELMET ||
                type == Material.DIAMOND_HELMET;
    }

    public static boolean isChestplate(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == null) {
            return false;
        }
        Material type = itemStack.getType();
        return type == Material.LEATHER_CHESTPLATE ||
                type == Material.IRON_CHESTPLATE ||
                type == Material.GOLD_CHESTPLATE ||
                type == Material.DIAMOND_CHESTPLATE;
    }

    public static boolean isLeggings(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == null) {
            return false;
        }
        Material type = itemStack.getType();
        return type == Material.LEATHER_LEGGINGS ||
                type == Material.IRON_LEGGINGS ||
                type == Material.GOLD_LEGGINGS ||
                type == Material.DIAMOND_LEGGINGS;
    }

    public static boolean isBoots(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == null) {
            return false;
        }
        Material type = itemStack.getType();
        return type == Material.LEATHER_BOOTS ||
                type == Material.IRON_BOOTS ||
                type == Material.GOLD_BOOTS ||
                type == Material.DIAMOND_BOOTS;
    }

    public static boolean isConsumablesItem(ItemStack itemStack){
        if (itemStack == null || itemStack.getType() == null) {
        return false;
    }
        Material type = itemStack.getType();
        return type == Material.STICK ||
                type == Material.WOOD;
    }
    public static boolean isSellItem(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == null) {
            return false;
        }

        Material type = itemStack.getType();

        return type == Material.IRON_INGOT ||
                type == Material.LOG ||
                type == Material.COAL ||
                type == Material.LEATHER ||
                type == Material.COBBLESTONE ||
                type == Material.REDSTONE ||
                (type == Material.INK_SACK && itemStack.getDurability() == 4);
    }



    public static int getAmountItem(CraftingInventory inventory){
        int amount = 0;
        for (ItemStack itemStack : inventory.getMatrix()){
            if (itemStack != null){

                amount = itemStack.getAmount();
                return amount;
            }
        }
        return amount;
    }
    public static void craftPropertiesArrow(Player player, ItemStack item, CraftingInventory craftInventory, int index){
        if (index == 0){
            player.getInventory().setBoots(giveUnbreakableItem(item));
        }
        if (index == 1){
            player.getInventory().setLeggings(giveUnbreakableItem(item));
        }
        if (index == 2){
            player.getInventory().setChestplate(giveUnbreakableItem(item));
        }
        if (index == 3){
            player.getInventory().setHelmet(giveUnbreakableItem(item));
        }

        ItemStack air = new ItemStack(Material.AIR);

        craftInventory.clear();
        craftInventory.setResult(air);
    }

    public static void craftProperties(Player player, ItemStack item, CraftingInventory craftInventory, int index){
        player.getInventory().setItem(index, giveUnbreakableItem(item));

        ItemStack air = new ItemStack(Material.AIR);

        craftInventory.clear();
        craftInventory.setResult(air);
    }

    public static boolean isProtectedItem(ItemStack itemStack){
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return false;
        }
        Material type = itemStack.getType();
        return
                type == Material.STONE_PICKAXE ||
                        type == Material.IRON_PICKAXE ||
                        type == Material.GOLD_PICKAXE ||
                        type == Material.DIAMOND_PICKAXE ||
                        type == Material.IRON_AXE ||
                        type == Material.GOLD_AXE ||
                        type == Material.DIAMOND_AXE ||
                        type == Material.STONE_SWORD ||
                        type == Material.IRON_SWORD ||
                        type == Material.GOLD_SWORD ||
                        type == Material.DIAMOND_SWORD ||
                        type == Material.LEATHER_HELMET ||
                        type == Material.IRON_HELMET ||
                        type == Material.GOLD_HELMET ||
                        type == Material.DIAMOND_HELMET ||
                        type == Material.LEATHER_CHESTPLATE ||
                        type == Material.IRON_CHESTPLATE ||
                        type == Material.GOLD_CHESTPLATE ||
                        type == Material.DIAMOND_CHESTPLATE ||
                        type == Material.LEATHER_LEGGINGS ||
                        type == Material.IRON_LEGGINGS ||
                        type == Material.GOLD_LEGGINGS ||
                        type == Material.DIAMOND_LEGGINGS ||
                        type == Material.LEATHER_BOOTS ||
                        type == Material.IRON_BOOTS ||
                        type == Material.GOLD_BOOTS ||
                        type == Material.WOOD_PICKAXE ||
                        type == Material.WOOD_SWORD ||
                        type == Material.STONE_AXE ||
                        type == Material.DIAMOND_BOOTS;
    }
}

