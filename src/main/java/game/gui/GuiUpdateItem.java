package game.gui;

import game.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
        int price = ConfigManager.getPriceUpdate(0);


        ItemStack[] armorPlayer = player.getPlayer().getEquipment().getArmorContents();
        for (int i = 0; i < player.getPlayer().getEquipment().getArmorContents().length; i++) {
            if (armorPlayer[i] == null) {
                invSell.setItem(i, barrier);
            }else {
                ItemStack armor = armorPlayer[i];
                armor.setItemMeta(setLoreUpdateItem(armor, price));
                invSell.setItem(i, armor);
            }
        }
        ItemStack item4 = player.getInventory().getItem(0);
        item4.setItemMeta(setLoreUpdateItem(item4, price));

        ItemStack item5 = player.getInventory().getItem(1);
        item5.setItemMeta(setLoreUpdateItem(item5, price));

        ItemStack item6 = player.getInventory().getItem(2);
        item6.setItemMeta(setLoreUpdateItem(item6, price));
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
