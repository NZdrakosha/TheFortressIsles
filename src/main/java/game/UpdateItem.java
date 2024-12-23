package game;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static game.Game.balance;
import static game.ItemUtil.*;

public class UpdateItem {

    public static ItemStack updateItemArmor(ItemStack itemStack, int price){
        if (itemStack == null) return null;
        ItemMeta meta = itemStack.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add("цена прокачки = " + price);
        meta.setLore(lore);
        meta.spigot().setUnbreakable(true);
            if (meta.getEnchants().keySet().isEmpty()) {
                meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                itemStack.setItemMeta(meta);
            }else {
                    int levelEnchant = meta.getEnchantLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
                    meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, levelEnchant + 1, true);
                    meta.setLore(null);
                    itemStack.setItemMeta(meta);
                }
        return itemStack;
        }
        public static ItemStack updateSword(ItemStack itemStack, int price){
            if (itemStack == null) return null;
            ItemMeta meta = itemStack.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            lore.add("цена прокачки = " + price);
            meta.setLore(lore);
            meta.spigot().setUnbreakable(true);
            if (meta.getEnchants().keySet().isEmpty()) {
                meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                itemStack.setItemMeta(meta);
            }else {
                int levelEncant = meta.getEnchantLevel(Enchantment.DAMAGE_ALL);
                meta.addEnchant(Enchantment.DAMAGE_ALL, levelEncant + 1, true);
                itemStack.setItemMeta(meta);
            }
            return itemStack;
        }

        public static ItemStack updatePicaxeAndAxe(ItemStack itemStack, int price){
        if (itemStack == null) return null;
        ItemMeta meta = itemStack.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            lore.add("цена прокачки = " + price);
            meta.setLore(lore);

                meta.spigot().setUnbreakable(true);
                if (meta.getEnchants().keySet().isEmpty()) {
                    meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
                    itemStack.setItemMeta(meta);
                }else {
                    int levelEncant = meta.getEnchantLevel(Enchantment.DIG_SPEED);
                    meta.addEnchant(Enchantment.DIG_SPEED, levelEncant + 1, true);
                    itemStack.setItemMeta(meta);
                }
            return itemStack;
        }


        static void updateItemHotBar(ItemStack item, Player player, int index) {
            double playerBalance = balance.get(player.getUniqueId());
            int priseKey;
            if (item.getEnchantments().isEmpty()) {
                priseKey = 1;
            } else {
                priseKey = item.getEnchantmentLevel(Enchantment.DIG_SPEED);
            }

            int priseUpdate = ConfigManager.getPriceUpdate(priseKey);
            Bukkit.getLogger().info("Player balance = " + playerBalance);
            Bukkit.getLogger().info("Prise update = " + priseUpdate);
            if (playerBalance != 0 && playerBalance >= priseUpdate) {
                ItemMeta meta = item.getItemMeta();
                meta.setLore(null);
                item.setItemMeta(meta);

                player.getInventory().setItem(index, updatePicaxeAndAxe(item, priseUpdate));
                double moneyPutMap = balance.get(player.getUniqueId()) - priseUpdate;
                balance.put(player.getUniqueId(), moneyPutMap);
                player.sendMessage("-" + priseUpdate + " осталось = " + moneyPutMap);
                player.setLevel((int) moneyPutMap);
            }else {
                player.sendMessage("Недостаточно средств");
            }
        }
        static void setUpdateItemArmorAndSword(ItemStack item, Player player) {
            double playerBalance = balance.get(player.getUniqueId());
            int priseKey;
            if (item.getEnchantments().isEmpty()) {
                priseKey = 0;
            }
            if (item.getType() == Material.IRON_SWORD){
                priseKey = item.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
            }
            else {
                priseKey = item.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
            }
            int priseUpdate = ConfigManager.getPriceUpdate(priseKey);
            ItemMeta meta = item.getItemMeta();
            meta.setLore(null);
            item.setItemMeta(meta);


            Bukkit.getLogger().info("Player balance = " + playerBalance);
            Bukkit.getLogger().info("Prise update = " + priseUpdate);
            if (playerBalance != 0 && playerBalance >= priseUpdate) {

                if (isHelmet(item)){
                    player.getInventory().setHelmet(updateItemArmor(item, priseUpdate));
                }
                else if (isChestplate(item)){
                    player.getInventory().setChestplate(updateItemArmor(item, priseUpdate));
                }
                else if(isLeggings(item)){
                    player.getInventory().setLeggings(updateItemArmor(item, priseUpdate));
                }
                else if (isBoots(item)){
                    player.getInventory().setBoots(updateItemArmor(item, priseUpdate));
                }
                if (isSword(item)){
                    player.getInventory().setItem(0, updateSword(item, priseUpdate));
                }

                double moneyPutMap = balance.get(player.getUniqueId()) - priseUpdate;
                balance.put(player.getUniqueId(), moneyPutMap);
                player.sendMessage("-" + priseUpdate + " осталось = " + moneyPutMap);
                player.setLevel((int) moneyPutMap);
            }else {
                player.sendMessage("Недостаточно средств");
            }
        }
    }

