package me.drakosha.thefortressisles.game.util;

import lombok.experimental.UtilityClass;
import me.drakosha.thefortressisles.game.Game;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Objects;

@UtilityClass
public class ItemUtil {
    public ItemStack setUnbreakable(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.spigot().setUnbreakable(true);
        item.setItemMeta(itemMeta);
        return item;
    }

    public boolean isProtectedItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;

        return item.getDurability() == 14 ||
                item.getDurability() == 13 ||
                item.getDurability() == 2 ||
                item.getDurability() == 9 ||
                isPickaxe(item) ||
                isAxe(item) ||
                isStartItem(item) ||
                isSword(item) ||
                isBoots(item) ||
                isLeggings(item) ||
                isChestplate(item) ||
                isHelmet(item);
    }

    public boolean isPickaxe(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;

        Material type = item.getType();
        return type == Material.STONE_PICKAXE ||
                type == Material.IRON_PICKAXE ||
                type == Material.GOLD_PICKAXE ||
                type == Material.DIAMOND_PICKAXE;
    }

    public boolean isAxe(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;

        Material type = item.getType();
        return type == Material.STONE_AXE ||
                type == Material.IRON_AXE ||
                type == Material.GOLD_AXE ||
                type == Material.DIAMOND_AXE;

    }

    public boolean isSword(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;

        Material type = item.getType();
        return type == Material.STONE_SWORD ||
                type == Material.IRON_SWORD ||
                type == Material.GOLD_SWORD ||
                type == Material.DIAMOND_SWORD;
    }

    public boolean isHelmet(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;

        Material type = item.getType();
        return type == Material.LEATHER_HELMET ||
                type == Material.IRON_HELMET ||
                type == Material.GOLD_HELMET ||
                type == Material.DIAMOND_HELMET;
    }

    public boolean isChestplate(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;

        Material type = item.getType();
        return type == Material.LEATHER_CHESTPLATE ||
                type == Material.IRON_CHESTPLATE ||
                type == Material.GOLD_CHESTPLATE ||
                type == Material.DIAMOND_CHESTPLATE;
    }

    public boolean isLeggings(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;

        Material type = item.getType();
        return type == Material.LEATHER_LEGGINGS ||
                type == Material.IRON_LEGGINGS ||
                type == Material.GOLD_LEGGINGS ||
                type == Material.DIAMOND_LEGGINGS;
    }

    public boolean isBoots(ItemStack item) {
        if (item == null || item.getType() == null) return false;

        Material type = item.getType();
        return type == Material.LEATHER_BOOTS ||
                type == Material.IRON_BOOTS ||
                type == Material.GOLD_BOOTS ||
                type == Material.DIAMOND_BOOTS;
    }

    public boolean isStartItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        Material type = item.getType();
        return type == Material.WOOD_SWORD ||
                type == Material.WOOD_AXE ||
                type == Material.WOOD_PICKAXE ||
                type == Material.FIREBALL;
    }


    private int getPriceItem(ItemStack item) {
        Material type = item.getType();
        switch (type) {
            case EMERALD:
                return 100;
            case GOLD_INGOT:
                return 60;
            case IRON_INGOT:
                return 50;
            case LOG:
                return 30;
            case COAL:
                return 25;
            case LEATHER:
                return 20;
            case REDSTONE:
                return 10;
            case INK_SACK:
                if (item.getDurability() == 4) return 5;
                break;
            case COBBLESTONE:
                return 2;
            default:
                return 0;
        }
        return 0;
    }

    public void givePlayerMoney(Game game, Player player, ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return;
        int count = getAmountItem(item.getType(), player);
        int price = getPriceItem(item);

        if (count == 0) {
            player.sendMessage("Нету предметов для продажи");
            return;
        }
        if (price == 0) return;

        int addMoney = price * count;
        player.getInventory().remove(item.getType());
        game.addMoneyPlayer(player, addMoney);
    }

    private int getAmountItem(Material itemType, Player player) {
        return Arrays.stream(player.getInventory().getContents())
                .filter(Objects::nonNull)
                .filter(item -> item.getType() == itemType)
                .mapToInt(ItemStack::getAmount)
                .sum();
    }

    public void updateItem(ItemStack item, Game game, Player player) {
        Enchantment enchantment = null;

        if (isSword(item)) enchantment = Enchantment.DAMAGE_ALL;
        if (isHelmet(item) || isBoots(item) || isLeggings(item) || isChestplate(item))
            enchantment = Enchantment.PROTECTION_ENVIRONMENTAL;
        if (isPickaxe(item) || isAxe(item)) enchantment = Enchantment.DIG_SPEED;
        if (enchantment == null) return;

        int playerBalance = game.getBalance().get(player);

        int currentLevel;
        if (item.getEnchantments().isEmpty()) currentLevel = 0;
        else currentLevel = item.getEnchantmentLevel(enchantment);

        int itemPrice = currentLevel == 0 ? FileUtil.getPriceUpdate(1) : FileUtil.getPriceUpdate(currentLevel + 1);
        System.out.println(itemPrice);
        if (playerBalance == 0 || playerBalance < itemPrice) {
            player.sendMessage("Недостаточно средств");
            return;
        }
        game.removeMonetPlayer(player, itemPrice);

        ItemStack updatesItem = new ItemStack(item.getType());
        ItemMeta updatesMeta = item.getItemMeta();
        updatesMeta.setLore(null);
        updatesItem.setItemMeta(updatesMeta);

        if (isHelmet(item)) player.getInventory().setHelmet(update(updatesItem, enchantment));
        else if (isChestplate(item)) player.getInventory().setChestplate(update(updatesItem, enchantment));
        else if (isLeggings(item)) player.getInventory().setLeggings(update(updatesItem, enchantment));
        else if (isBoots(item)) player.getInventory().setBoots(update(updatesItem, enchantment));
        else if (isSword(item)) player.getInventory().setItem(0, update(updatesItem, enchantment));
        else if (isPickaxe(item)) player.getInventory().setItem(1, update(updatesItem, enchantment));
        else if (isAxe(item)) player.getInventory().setItem(2, update(updatesItem, enchantment));

        player.updateInventory();
    }

    private ItemStack update(ItemStack item, Enchantment enchantment) {
        ItemMeta meta = item.getItemMeta();

        meta.spigot().setUnbreakable(true);
        if (meta.getEnchants().isEmpty()) {
            meta.addEnchant(enchantment, 1, true);
            item.setItemMeta(meta);
        } else {
            int levelEncant = meta.getEnchantLevel(enchantment);
            meta.addEnchant(enchantment, levelEncant + 1, true);
            item.setItemMeta(meta);
        }
        return item;
    }
}