package me.drakosha.thefortressisles.game.util;

import lombok.experimental.UtilityClass;
import me.drakosha.thefortressisles.TheFortressIsles;
import me.drakosha.thefortressisles.game.Game;
import me.drakosha.thefortressisles.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

@UtilityClass
public class GameUtil {
    public void preparationPlayer(Player player, World world){
        player.getInventory().setItem(0, ItemUtil.setUnbreakable(new ItemStack(Material.WOOD_SWORD)));
        player.getInventory().setItem(1, ItemUtil.setUnbreakable(new ItemStack(Material.WOOD_PICKAXE)));
        player.getInventory().setItem(2, ItemUtil.setUnbreakable(new ItemStack(Material.WOOD_AXE)));

        ItemStack itemSelectTeam = new ItemStack(Material.FIREBALL);
        ItemMeta meta = itemSelectTeam.getItemMeta();
        meta.setDisplayName("Выбор команды");
        itemSelectTeam.setItemMeta(meta);

        player.teleport(world.getSpawnLocation());
        player.setGameMode(GameMode.SURVIVAL);

        player.setLevel(0);
        player.sendMessage("Вы присоединились к игре");

        player.getInventory().setItem(8, ItemUtil.setUnbreakable(itemSelectTeam));
    }
    public ArrayList<String> initListAllColor(){
        ArrayList<String> colorList = new ArrayList<>();
        colorList.add("green");
        colorList.add("red");
        colorList.add("cyan");
        colorList.add("purple");
        return colorList;
    }
    public String randomColor(ArrayList<String> availableTeam){
        if (availableTeam.isEmpty()) return null;
        Random random = new Random();
        int randomTeam = random.nextInt(availableTeam.size());
        return availableTeam.get(randomTeam);
    }
    public void checkLighthouseDeath(Game game, GameManager gameManager){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (game.getPlayerInGame().isEmpty() || game.getPlayerInGame().size() == 1){
                    game.removePlayerInGame(game.getPlayerInGame().get(0), "win");
                    gameManager.removeGame(game.getGameId());
                    cancel();
                }
                for (String color : game.getColorTeamPlayer().keySet()) {
                    if (game.getLighthouse().getHealth(color) > 0) continue;
                    Player player = game.getColorTeamPlayer().get(color);
                    game.removePlayerInGame(player, "lose");
                }
            }
        }.runTaskTimer(TheFortressIsles.getInstance(), 0L, 20L);
    }
}
