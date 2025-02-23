package me.drakosha.thefortressisles.game.gui;

import me.drakosha.thefortressisles.game.Game;
import me.drakosha.thefortressisles.game.GameManager;
import me.drakosha.thefortressisles.game.util.GuiUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class SelectTeamGui implements InventoryHolder {
    private final Inventory inventory;
    public SelectTeamGui(GameManager gameManager, Player player){
        Game game = gameManager.getGame(Game.playerGame.get(player));
        inventory = Bukkit.createInventory(this, 9, "Выбор команды");

        inventory.setItem(0, GuiUtil.setPropertiesGlass("red", new ItemStack(Material.STAINED_GLASS, 1, (short) 14), game.getColorTeamPlayer()));
        inventory.setItem(3, GuiUtil.setPropertiesGlass("cyan", new ItemStack(Material.STAINED_GLASS, 1, (short) 9), game.getColorTeamPlayer()));
        inventory.setItem(5, GuiUtil.setPropertiesGlass("purple", new ItemStack(Material.STAINED_GLASS, 1, (short) 2), game.getColorTeamPlayer()));
        inventory.setItem(8, GuiUtil.setPropertiesGlass("green", new ItemStack(Material.STAINED_GLASS, 1, (short) 13), game.getColorTeamPlayer()));
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
