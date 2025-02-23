package me.drakosha.thefortressisles.game.gui;

import me.drakosha.thefortressisles.game.util.GuiUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class SellItemGui implements InventoryHolder {
    private final Inventory inventory;
    public SellItemGui(){
        inventory = Bukkit.createInventory(this, 9, "Продажа");

        inventory.setItem(0, GuiUtil.setPropertiesItemSell(new ItemStack(Material.EMERALD)));
        inventory.setItem(1, GuiUtil.setPropertiesItemSell(new ItemStack(Material.GOLD_INGOT)));
        inventory.setItem(2, GuiUtil.setPropertiesItemSell(new ItemStack(Material.IRON_INGOT)));
        inventory.setItem(3, GuiUtil.setPropertiesItemSell(new ItemStack(Material.LOG)));
        inventory.setItem(4, GuiUtil.setPropertiesItemSell(new ItemStack(Material.COAL)));
        inventory.setItem(5, GuiUtil.setPropertiesItemSell(new ItemStack(Material.LEATHER)));
        inventory.setItem(6, GuiUtil.setPropertiesItemSell(new ItemStack(Material.REDSTONE)));
        inventory.setItem(7, GuiUtil.setPropertiesItemSell(new ItemStack(Material.INK_SACK, 1, (short) 4)));
        inventory.setItem(8, GuiUtil.setPropertiesItemSell(new ItemStack(Material.COBBLESTONE)));

    }
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
