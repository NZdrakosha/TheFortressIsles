package game.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static game.Game.*;

public class InventorySelectTeam implements InventoryHolder {
    public static Map<String, ItemStack> glassList = new HashMap<>();
    public static ItemStack cyanGlass = new ItemStack(Material.STAINED_GLASS, 1, (short) 9);
    public static ItemStack redGlass = new ItemStack(Material.STAINED_GLASS, 1, (short) 14);
    public static ItemStack purpleGlass = new ItemStack(Material.STAINED_GLASS, 1, (short) 2);
    public static ItemStack greenGlass = new ItemStack(Material.STAINED_GLASS, 1, (short) 13);

    private static Inventory inv;
    public InventorySelectTeam(){
        inv = Bukkit.createInventory(this, 9, "Выбор команды");


        cyanGlass = setName(cyanGlass, "Бирюзовая команда");

        redGlass = setName(redGlass, "Красная команда");

        purpleGlass = setName(purpleGlass, "Фиолетовая команда");

        greenGlass = setName(greenGlass, "Зеленая команда");


        inv.setItem(0, setItemMeta("red", redGlass));
        inv.setItem(1, setItemMeta("cyan", cyanGlass));
        inv.setItem(2, setItemMeta("purple", purpleGlass));
        inv.setItem(3, setItemMeta("green", greenGlass));

    }
    @Override
    public Inventory getInventory() {
        return inv;
    }

    private static ItemStack setItemMeta(String color, ItemStack itemStack) {
        if (equippedTeam.get(color) != null && colorTeamPlayer.get(color) != null) {
            Player player = Bukkit.getPlayer(colorTeamPlayer.get(color));

            if (player != null) {
                ItemMeta meta = itemStack.getItemMeta();
                ArrayList<String> lore = new ArrayList<>();

                lore.add(ChatColor.GRAY + player.getName());
                meta.setLore(lore);

                meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                itemStack.setItemMeta(meta);
            }
        }else {
            ItemMeta meta = itemStack.getItemMeta();
            meta.setLore(null);
            meta.removeEnchant(Enchantment.ARROW_INFINITE);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }
    private static ItemStack setName(ItemStack itemStack, String name){
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

}
