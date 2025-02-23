package me.drakosha.thefortressisles.game.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@UtilityClass
public class GuiUtil {
    public ItemStack setPropertiesGlass(String color, ItemStack item, Map<String, Player> activeTeam) {
        Player player = activeTeam.get(color);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(FileUtil.getNameColor(color));

        if (activeTeam.get(color) != null) {
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + player.getName());
            itemMeta.setLore(lore);

            itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            itemMeta.setLore(null);
            itemMeta.removeEnchant(Enchantment.ARROW_INFINITE);
        }
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack setPropertiesItemSell(ItemStack item) {
        switch (item.getType()) {
            case EMERALD:
                item.setItemMeta(setLore(item, "Цена продажи = 100"));
                break;
            case GOLD_INGOT:
                item.setItemMeta(setLore(item, "Цена продажи = 60"));
                break;
            case IRON_INGOT:
                item.setItemMeta(setLore(item, "Цена продажи = 50"));
                break;
            case LOG:
                item.setItemMeta(setLore(item, "Цена продажи = 30"));
                break;
            case COAL:
                item.setItemMeta(setLore(item, "Цена продажи = 25"));
                break;
            case LEATHER:
                item.setItemMeta(setLore(item, "Цена продажи = 20"));
                break;
            case REDSTONE:
                item.setItemMeta(setLore(item, "Цена продажи = 10"));
                break;
            case INK_SACK:
                if (item.getDurability() == 4) item.setItemMeta(setLore(item, "Цена продажи = 5"));
                break;
            case COBBLESTONE:
                item.setItemMeta(setLore(item, "Цена продажи = 2"));
                break;
        }
        return item;
    }

    private ItemMeta setLore(ItemStack item, String lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Collections.singletonList(ChatColor.GRAY + lore));

        return meta;
    }

    public ItemStack setPropertiesItemUpdate(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return null;
        if (ItemUtil.isStartItem(item)) {
            item.setItemMeta(setLore(item, ChatColor.GRAY + "Нельзя прокачивать начальный предмет"));
            return item;
        }
        Enchantment enchantment = null;
        if (ItemUtil.isAxe(item) || ItemUtil.isPickaxe(item)) enchantment = Enchantment.DIG_SPEED;
        if (ItemUtil.isSword(item)) enchantment = Enchantment.DAMAGE_ALL;
        if (ItemUtil.isBoots(item) || ItemUtil.isLeggings(item) ||
                ItemUtil.isChestplate(item)
                || ItemUtil.isHelmet(item)) enchantment = Enchantment.PROTECTION_ENVIRONMENTAL;

        int itemPrice = item.getEnchantmentLevel(enchantment) == 0 ? FileUtil.getPriceUpdate(1) : FileUtil.getPriceUpdate(item.getEnchantmentLevel(enchantment) + 1);

        item.setItemMeta(setLore(item, "Цена прокачки = " + itemPrice));

        return item;
    }

}
