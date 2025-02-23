package me.drakosha.thefortressisles.game.gui;

import me.drakosha.thefortressisles.game.util.GuiUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class UpdateItemGui implements InventoryHolder {
    private final Inventory inventory;
    public UpdateItemGui(Player player){
        inventory = Bukkit.createInventory(this, 9, "Прокачка");

        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta meta = barrier.getItemMeta();
        meta.setDisplayName("Пусто");
        barrier.setItemMeta(meta);

        ItemStack[] armor = player.getPlayer().getEquipment().getArmorContents();
        for (int i = 0; i < player.getPlayer().getEquipment().getArmorContents().length; i++){
            if (armor[i] == null) { inventory.setItem(i, barrier); continue; };
            ItemStack armorItem = new ItemStack(armor[i]);
            ItemMeta itemMeta = armor[i].getItemMeta();
            armorItem.setItemMeta(itemMeta);

            inventory.setItem(i, GuiUtil.setPropertiesItemUpdate(armorItem));
        }

        ItemStack sword = new ItemStack(player.getInventory().getItem(0).getType());
        ItemMeta swordMeta = player.getInventory().getItem(0).getItemMeta();
        sword.setItemMeta(swordMeta);


        ItemStack pickaxe = new ItemStack(player.getInventory().getItem(1).getType());
        ItemMeta pickaxeMeta = player.getInventory().getItem(1).getItemMeta();
        pickaxe.setItemMeta(pickaxeMeta);

        ItemStack axe = new ItemStack(player.getInventory().getItem(2).getType());
        ItemMeta axeMeta = player.getInventory().getItem(2).getItemMeta();
        axe.setItemMeta(axeMeta);

        inventory.setItem(4, GuiUtil.setPropertiesItemUpdate(sword));
        inventory.setItem(5, GuiUtil.setPropertiesItemUpdate(pickaxe));
        inventory.setItem(6, GuiUtil.setPropertiesItemUpdate(axe));
    }
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
