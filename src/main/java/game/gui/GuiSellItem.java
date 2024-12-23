package game.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GuiSellItem implements InventoryHolder {
    private static Inventory invSell;
    public GuiSellItem(){
        invSell = Bukkit.createInventory(this, 9, "Продажа");

        invSell.setItem(0, setItemMeta(new ItemStack(Material.IRON_INGOT)));
        invSell.setItem(1, setItemMeta(new ItemStack(Material.LOG)));
        invSell.setItem(2, setItemMeta(new ItemStack(Material.COAL)));
        invSell.setItem(3, setItemMeta(new ItemStack(Material.LEATHER)));
        invSell.setItem(4, setItemMeta(new ItemStack(Material.COBBLESTONE)));
        invSell.setItem(5, setItemMeta(new ItemStack(Material.REDSTONE)));
        invSell.setItem(6, setItemMeta(new ItemStack(Material.INK_SACK, 1, (short) 4)));




    }
    @Override
    public Inventory getInventory() {
        return invSell;
    }
    public static Inventory getInvSell(){
        return invSell;
    }


    private static ItemStack setItemMeta(ItemStack item){
        if (item.getType() == Material.IRON_INGOT){
            item.setItemMeta(setLore(item, ChatColor.GRAY + "Цена продажи = 20 монет"));
        }
        if (item.getType() == Material.LOG){
            item.setItemMeta(setLore(item, ChatColor.GRAY + "Цена продажи = 15 монет"));
        }
        if (item.getType() == Material.COAL) {
            item.setItemMeta(setLore(item, ChatColor.GRAY + "Цена продажи = 15 монет"));
        }
        if (item.getType() == Material.LEATHER){
            item.setItemMeta(setLore(item, ChatColor.GRAY + "Цена продажи = 10 монет"));
        }
        if (item.getType() == Material.COBBLESTONE) {
            item.setItemMeta(setLore(item, ChatColor.GRAY + "Цена продажи = 0.5 монеты"));
        }
        if (item.getType() == Material.INK_SACK && item.getDurability() == 4) {
            item.setItemMeta(setLore(item, ChatColor.GRAY + "Цена продажи = 0.1 монета"));
        }
        if (item.getType() == Material.REDSTONE) {
            item.setItemMeta(setLore(item, ChatColor.GRAY + "Цена продажи = 0.2 монеты"));
        }

        return item;
    }

    private static ItemMeta setLore(ItemStack item, String lore){
        ItemMeta meta = item.getItemMeta();
        ArrayList<String> list = new ArrayList<>();
        list.add(lore);
        meta.setLore(list);

        return meta;
    }
}
