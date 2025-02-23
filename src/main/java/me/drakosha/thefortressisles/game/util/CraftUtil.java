package me.drakosha.thefortressisles.game.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class CraftUtil {
    public boolean isCraftingItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;

        Material type = item.getType();
        return ItemUtil.isHelmet(item) ||
                ItemUtil.isChestplate(item) ||
                ItemUtil.isLeggings(item) ||
                ItemUtil.isBoots(item) ||
                ItemUtil.isSword(item) ||
                ItemUtil.isAxe(item) ||
                ItemUtil.isPickaxe(item) ||
                type == Material.WOOD ||
                type == Material.STICK;
    }

    public void craftingInventoryUpdate(CraftingInventory inventory) {
        ItemStack[] matrix = inventory.getMatrix();
        for (int i = 0; i <= matrix.length; i++) {
            ItemStack item = inventory.getContents()[i];
            if (item == null || item.getType() == Material.AIR) continue;

            inventory.getItem(i).setAmount(item.getAmount() - 1);
        }
        inventory.setMatrix(matrix);
    }
}
