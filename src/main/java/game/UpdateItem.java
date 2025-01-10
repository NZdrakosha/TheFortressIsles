package game;

import org.bukkit.Bukkit;
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
        update(itemStack, price, Enchantment.PROTECTION_ENVIRONMENTAL);
        return itemStack;
        }
        public static ItemStack updateSword(ItemStack itemStack, int price){
            if (itemStack == null) return null;
            update(itemStack, price, Enchantment.DAMAGE_ALL);
                return itemStack;
        }

        public static ItemStack updatePicaxeAndAxe(ItemStack itemStack, int price){
        if (itemStack == null) return null;
        update(itemStack, price, Enchantment.DIG_SPEED);
            return itemStack;
        }

        private static void update(ItemStack itemStack, int price, Enchantment enchantment){
            ItemMeta meta = itemStack.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            lore.add("цена прокачки = " + price);
            meta.setLore(lore);

            meta.spigot().setUnbreakable(true);
            if (meta.getEnchants().keySet().isEmpty()) {
                meta.addEnchant(enchantment, 1, true);
                itemStack.setItemMeta(meta);
            }else {
                int levelEncant = meta.getEnchantLevel(enchantment);
                meta.addEnchant(enchantment, levelEncant + 1, true);
                itemStack.setItemMeta(meta);
            }
        }

        public static void setUpdateItem(ItemStack item, Player player){
            double playerBalance = balance.get(player.getUniqueId());

            int priceKey;
            int priceUpdate;
            int priceUpdateLore;
            if (item.getEnchantments().isEmpty()) {
                priceUpdateLore = ConfigManager.getPriceUpdate(2);
                priceUpdate = ConfigManager.getPriceUpdate(1);
            }  else {
                priceKey = item.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) + 1;
                priceUpdateLore = ConfigManager.getPriceUpdate(priceKey + 1);
                priceUpdate = ConfigManager.getPriceUpdate(priceKey);
            }
            if (isSword(item)){
                priceKey = item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) + 1;
                priceUpdateLore = ConfigManager.getPriceUpdate(priceKey + 1);
                priceUpdate = ConfigManager.getPriceUpdate(priceKey);
            }
            if (isPickaxe(item) || isAxe(item)){
                priceKey = item.getEnchantmentLevel(Enchantment.DIG_SPEED) + 1;
                priceUpdate = ConfigManager.getPriceUpdate(priceKey);
                priceUpdateLore = ConfigManager.getPriceUpdate(priceKey + 1);
            }

            ItemMeta meta = item.getItemMeta();
            item.setItemMeta(meta);

            Bukkit.getLogger().info("Player balance = " + playerBalance);
            Bukkit.getLogger().info("Prise update = " + priceUpdate);
            if (playerBalance != 0 && playerBalance >= priceUpdate) {

                if (isHelmet(item)) player.getInventory().setHelmet(updateItemArmor(item, priceUpdateLore));
                else if (isChestplate(item)) player.getInventory().setChestplate(updateItemArmor(item, priceUpdateLore));
                else if(isLeggings(item)) player.getInventory().setLeggings(updateItemArmor(item, priceUpdateLore));
                else if (isBoots(item)) player.getInventory().setBoots(updateItemArmor(item, priceUpdateLore));
                else if (isSword(item)) player.getInventory().setItem(0, updateSword(item, priceUpdateLore));
                else if (isPickaxe(item)) player.getInventory().setItem(1, updatePicaxeAndAxe(item, priceUpdateLore));
                else if (isAxe(item)) player.getInventory().setItem(2, updatePicaxeAndAxe(item, priceUpdateLore));


                double moneyPutMap = balance.get(player.getUniqueId()) - priceUpdate;
                balance.put(player.getUniqueId(), moneyPutMap);
                player.sendMessage("-" + priceUpdate + " осталось = " + moneyPutMap);
                player.setLevel((int) moneyPutMap);
                player.updateInventory();


            }else player.sendMessage("Недостаточно средств");
        }
    }