package game.gui;

import game.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GuiUpdateItem implements InventoryHolder {
    private static Inventory invSell;

    public GuiUpdateItem(Player player){
        invSell = Bukkit.createInventory(this, 9, "Прокачка");


        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta meta = barrier.getItemMeta();
        meta.setDisplayName("Пусто");
        barrier.setItemMeta(meta);


        ItemStack item4 = player.getInventory().getItem(0);
        int priceItem4 = item4.getEnchantmentLevel(Enchantment.DAMAGE_ALL) == 0 ? ConfigManager.getPriceUpdate(1) :
                ConfigManager.getPriceUpdate(item4.getEnchantmentLevel(Enchantment.DAMAGE_ALL) + 1);
        item4.setItemMeta(setLoreUpdateItem(item4, priceItem4));


        ItemStack item5 = player.getInventory().getItem(1);
        int priceItem5 = item5.getEnchantmentLevel(Enchantment.DIG_SPEED) == 0 ? ConfigManager.getPriceUpdate(1) :
                ConfigManager.getPriceUpdate(item5.getEnchantmentLevel(Enchantment.DIG_SPEED) + 1);
        item5.setItemMeta(setLoreUpdateItem(item5, priceItem5));

        ItemStack item6 = player.getInventory().getItem(2);
        int priceItem6 = item6.getEnchantmentLevel(Enchantment.DIG_SPEED) == 0 ? ConfigManager.getPriceUpdate(1) :
                ConfigManager.getPriceUpdate(item6.getEnchantmentLevel(Enchantment.DIG_SPEED) + 1);
        item6.setItemMeta(setLoreUpdateItem(item6, priceItem6));

        ItemStack[] armorPlayer = player.getPlayer().getEquipment().getArmorContents();
        for (int i = 0; i < player.getPlayer().getEquipment().getArmorContents().length; i++) {
            if (armorPlayer[i] == null)invSell.setItem(i, barrier);
            else {
                ItemStack armor = armorPlayer[i];
                int priceArmor = armor.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) == 0 ? ConfigManager.getPriceUpdate(1) :
                        ConfigManager.getPriceUpdate(armor.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) + 1);
                armor.setItemMeta(setLoreUpdateItem(armor, priceArmor));
                invSell.setItem(i, armor);
            }
        }
        invSell.setItem(4, item4);
        invSell.setItem(5, item5);
        invSell.setItem(6, item6);
    }
    @Override
    public Inventory getInventory() {
        return invSell;
    }

    private static ItemMeta setLoreUpdateItem(ItemStack itemStack, int price){
        if (itemStack != null) {
            ItemMeta meta = itemStack.getItemMeta();
            ArrayList<String> lores = new ArrayList<>();
            lores.add("Цена прокачки = " + price);
            meta.setLore(lores);
            itemStack.setItemMeta(meta);
            return meta;
        }
        return null;
    }
}
