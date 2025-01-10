package game;

import game.gui.GuiSellItem;
import game.gui.GuiUpdateItem;
import me.drakosha.thefortressisles.TheFortressIsles;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.UUID;

import static game.Game.*;
import static game.ItemUtil.*;
import static game.command.AdminCommand.activeGame;
import static game.command.AdminCommand.startGame;
import static game.mine.Mine.*;
import static game.mob.CreateEntity.*;
import static game.UpdateItem.*;


public class PlayerEvent implements Listener {
    private final int[] mainItemInv = new int[]{0, 1, 2};


    @EventHandler
    public void blockBreakPlayer(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material breakItem = event.getBlock().getType();
        event.setDropItems(false);
            switch (breakItem) {
                case DIRT:
                    player.getInventory().addItem(new ItemStack(Material.LEATHER));
                    break;
                case COAL_ORE:
                    player.getInventory().addItem(new ItemStack(Material.COAL));
                    break;
                case DIAMOND_ORE:
                    player.getInventory().addItem(new ItemStack(Material.DIAMOND));
                    break;
                case EMERALD_ORE:
                    player.getInventory().addItem(new ItemStack(Material.EMERALD));
                    break;
                case STONE:
                    player.getInventory().addItem(new ItemStack(Material.COBBLESTONE));
                    break;

                case GOLD_ORE:
                case OBSIDIAN:
                case IRON_ORE:
                case LOG:
                    player.getInventory().addItem(new ItemStack(event.getBlock().getType()));
                    break;
                case LAPIS_ORE:
                case REDSTONE_ORE:
                case GLOWING_REDSTONE_ORE:
                    for (ItemStack item : event.getBlock().getDrops()) {
                        player.getInventory().addItem(new ItemStack(item));
                    }
                    break;
                default:
                    event.setCancelled(true);
            }
    }

    @EventHandler
    public void dropPlayerItem(PlayerDeathEvent event){
        Player player = event.getEntity();
        Player playerKiller = player.getKiller();
        if (playerKiller == null) return;

        for (ItemStack itemInventory : player.getInventory().getContents()){
            if (itemInventory == null|| isProtectedItem(itemInventory)) continue;

            if (itemInventory.getAmount() >= 2) {
                    int countGivePlayerItem = itemInventory.getAmount() / 2;
                    ItemStack itemGive = new ItemStack(itemInventory.getType(), countGivePlayerItem);
                    playerKiller.getInventory().addItem(itemGive);

                    itemInventory.setAmount(itemInventory.getAmount() - countGivePlayerItem);
            }
        }
    }

    @EventHandler
    public void deathPlayer(PlayerDeathEvent event){
        if (event.getEntity() != null){
            Player player = event.getEntity();

            player.spigot().respawn();
            player.setGameMode(GameMode.SPECTATOR);
            new BukkitRunnable(){
                int seconds = 10;
                @Override
                public void run() {
                    if (seconds <= 0){
                        player.setGameMode(GameMode.SURVIVAL);
                        player.sendMessage("Вы возродились");
                        player.teleport(player.getBedSpawnLocation());
                        player.sendTitle(" " + seconds, null, 10, 10, 10);
                        cancel();
                        return;
                    }
                    player.sendTitle("До возрождения = " + seconds, null, 10, 10, 10);
                    seconds--;
                }
            }.runTaskTimer(TheFortressIsles.getInstance(), 0L, 20L);
        }
    }

    @EventHandler
    public void playerDropItem(PlayerDropItemEvent event) {
        ItemStack dropItem = event.getItemDrop().getItemStack();

        if (isProtectedItem(dropItem)) event.setCancelled(true);
        else event.setCancelled(false);

    }

    @EventHandler
    public static void playerSelectTeam(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equalsIgnoreCase("Выбор команды")) {
            ItemStack currentItem = event.getCurrentItem();

            if (currentItem != null) {
                selectTeam(player, currentItem);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void protectedInventory(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();
        if (event.getClickedInventory() == null || event.getClickedInventory().getTitle() == null) return;

        boolean value = isProtectedItem(itemStack);
            if (value) event.setCancelled(true);
            else event.setCancelled(false);

        if (event.getHotbarButton() >= 0) {
            int hotbarSlot = event.getHotbarButton();
            for (int itemMain : mainItemInv) {
                if (hotbarSlot == itemMain) {
                    if (itemStack.getType() == Material.AIR || itemStack.getType() != null) event.setCancelled(true);

                    if (isProtectedItem(itemStack)) { event.setCancelled(true); return; }
                }
            }
        }
        GuiUpdateItem updateInv = new GuiUpdateItem(player);
            if (event.getClickedInventory().getTitle().equals(updateInv.getInventory().getTitle())) {
                if (event.getInventory() != null && event.getInventory().getTitle() != null) event.setCancelled(true);
            }

            GuiSellItem sellInv = new GuiSellItem();
            if (event.getClickedInventory().getTitle().equals(sellInv.getInventory().getTitle())) {
                if (event.getInventory() != null && event.getInventory().getTitle() != null) event.setCancelled(true);
        }
    }

    @EventHandler
    public void openSellGui(PlayerInteractAtEntityEvent event){
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (entity.getUniqueId() != null && entity instanceof Villager){
            Villager villager = (Villager) entity;
            villager.setRecipes(Collections.emptyList());
            event.setCancelled(true);

            for (int i = 0; i < villagerSeller.size(); i++){
                if (entity.getUniqueId() == villagerSeller.get(i)){
                    GuiSellItem inv = new GuiSellItem();
                    player.openInventory(inv.getInventory());
                }
            }
        }
    }

    @EventHandler
    public void openUpdateGui(PlayerInteractAtEntityEvent event){
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (entity.getUniqueId() != null && entity instanceof Villager){
            Villager villager = (Villager) entity;
            villager.setRecipes(Collections.emptyList());
            event.setCancelled(true);

            for (int i = 0; i < villagerUpdate.size(); i++){
                if (entity.getUniqueId() == villagerUpdate.get(i)){
                    GuiUpdateItem inv = new GuiUpdateItem(player);
                    player.openInventory(inv.getInventory());
                }
            }
        }
    }
    @EventHandler
    public void playerFoodLevel(FoodLevelChangeEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getEntity();

        if (event.getFoodLevel() < 20) {
            player.setLevel(20);
            player.setSaturation(20);
        }
    }

    @EventHandler
    public void repairMine(PlayerInteractEvent event){
        if  (!activeGame) return;

        Player player = event.getPlayer();
        if (event.getAction().toString().contains("RIGHT_CLICK")){
            Location blockLocation = event.getClickedBlock().getLocation();
            if (blockLocation != null){
            for (int i = 0; i < locationSign.size(); i++){
                if (blockLocation.equals(locationSign.get(i))){
                    repairZones(activeTeam.get(player.getUniqueId()), player);
                    player.sendMessage("Шахта обновлена");
                    return;
                }
            }
            if (blockLocation.equals(new Location(blockLocation.getWorld(), 149, 91, 107))){
                Location spawnLocationPlayer = player.getBedSpawnLocation();
                player.teleport(spawnLocationPlayer);
                player.sendMessage("Вы вернулись на базу");
                return;
            }
            for (int i = 0; i < signTeleportSuperMine.size(); i++) {
                if (blockLocation.equals(signTeleportSuperMine.get(i))) {
                    Location teleportSuperMine = new Location(blockLocation.getWorld(), 147.5, 90, 107.5);
                    teleportSuperMine.setYaw(90.0f);
                    player.teleport(teleportSuperMine);
                    }
                }
            }
        }
    }


    @EventHandler
    public void craftItem(CraftItemEvent event){
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        CraftingInventory craftInventory = event.getInventory();

        if (isPickaxe(item)) craftProperties(player, item, craftInventory, 1);
        else if (isAxe(item)) craftProperties(player, item, craftInventory, 2);
        else if (isSword(item)) craftProperties(player, item, craftInventory, 0);
        else if (isBoots(item)) craftPropertiesArrow(player, item, craftInventory, 0);
        else if (isLeggings(item)) craftPropertiesArrow(player, item, craftInventory, 1);
        else if (isChestplate(item)) craftPropertiesArrow(player, item, craftInventory, 2);
        else if (isHelmet(item)) craftPropertiesArrow(player, item, craftInventory, 3);
        if (isConsumablesItem(item)){

            int amountItem;

                amountItem = getAmountItem(craftInventory) * item.getAmount();
                item.setAmount(amountItem);
                player.getInventory().addItem(giveUnbreakableItem(item));

                ItemStack air = new ItemStack(Material.AIR);

                craftInventory.clear();
                craftInventory.setResult(air);
        } else if (!isConsumablesItem(item)) event.setCancelled(true);
    }

    @EventHandler
    public void updateItem(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle() == null || !event.getView().getTitle().equalsIgnoreCase("Прокачка")) return;

        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null || player == null || currentItem.getType() == Material.AIR) return;

        if (currentItem.getType() != Material.AIR) {
            if (isHelmet(currentItem) || isChestplate(currentItem) || isLeggings(currentItem) || isPickaxe(currentItem) || isBoots(currentItem) || isSword(currentItem) || isAxe(currentItem)) {
                setUpdateItem(currentItem, player);
            }else player.sendMessage("Нельзя прокачать начальный предмет");

        }
    }

    @EventHandler
    public void sellItem(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getClickedInventory() == null || event.getCurrentItem() == null) return;

        GuiSellItem sellInv = new GuiSellItem();
        if (event.getClickedInventory().getTitle().equalsIgnoreCase(sellInv.getInventory().getTitle())) {
            ItemStack currentItem = event.getCurrentItem();
            if (currentItem == null || player == null || currentItem.getType() == Material.AIR) return;


            if (isSellItem(currentItem)) {
                if (getAmountItem(currentItem, player) < 1){
                    event.setCancelled(true);
                    player.sendMessage("У вас нету нужных предметов");
                    return;
                }
                int itemAmount = getAmountItem(currentItem, player);
                double itemPrise = getMoneyGive(currentItem);

                double moneyGetGivePlayer = itemPrise * itemAmount;
                player.sendMessage("Вы получили + " + moneyGetGivePlayer);

                if (balance.get(player.getUniqueId()) == null ) balance.put(player.getUniqueId(), 0.0);

                double currentBalance = balance.get(player.getUniqueId());
                double moneyPut = currentBalance + moneyGetGivePlayer;

                balance.put(player.getUniqueId(), moneyPut);

                player.setLevel((int) moneyPut);
                removeItemsFromInventory(player, currentItem, itemAmount);
            }
        }
    }
    @EventHandler
    public void deleteMob(PluginDisableEvent event){
        for (UUID e : activeEntity){
            Entity entity = Bukkit.getEntity(e);
            entity.remove();
        }
    }
    @EventHandler
    public void setLevelPlayer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setExp(0.0f );
        player.setLevel(0);
    }

    @EventHandler
    public void playerGiveExp(PlayerExpChangeEvent event) { event.setAmount(0); }
    @EventHandler
    public void offDamagePlayerOnLobby(EntityDamageByEntityEvent event){ if (!startGame) event.setCancelled(true); }
    @EventHandler
    public void blockPutEvent(BlockPlaceEvent event){ event.setCancelled(true); }
    @EventHandler
    public void swapHand(PlayerSwapHandItemsEvent event){ event.setCancelled(true); }
    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) { event.setCancelled(true); }
}
