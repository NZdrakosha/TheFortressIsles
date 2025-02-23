package me.drakosha.thefortressisles.game.player;

import me.drakosha.thefortressisles.game.Game;
import me.drakosha.thefortressisles.game.GameManager;
import me.drakosha.thefortressisles.game.GameState;
import me.drakosha.thefortressisles.game.gui.SelectTeamGui;
import me.drakosha.thefortressisles.game.gui.SellItemGui;
import me.drakosha.thefortressisles.game.gui.UpdateItemGui;
import me.drakosha.thefortressisles.game.mine.Mine;
import me.drakosha.thefortressisles.game.mob.SpawnEntities;
import me.drakosha.thefortressisles.game.mob.WaveMob;
import me.drakosha.thefortressisles.game.util.CraftUtil;
import me.drakosha.thefortressisles.game.util.FileUtil;
import me.drakosha.thefortressisles.game.util.ItemUtil;
import me.drakosha.thefortressisles.game.util.WorldUtil;
import net.minecraft.server.v1_12_R1.EntityZombie;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftZombie;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class PlayerEvent implements Listener {
    private UUID startGameVillager;
    private final GameManager gameManager;

    public PlayerEvent(GameManager gameManager) {
        this.gameManager = gameManager;
    }


    @EventHandler
    public void playerBreakMine(BlockBreakEvent event) {
        if (Mine.isBlockInMine(event.getBlock(), gameManager.getGame(Game.playerGame.get(event.getPlayer())), event.getPlayer()) &&
        Mine.isBlockInSuperMine(event.getBlock())) {
            event.setCancelled(true);
            return;
        }
        Player player = event.getPlayer();
        Material breakItem = event.getBlock().getType();
        event.setDropItems(false);
        event.setExpToDrop(0);
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
            case COBBLESTONE:
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
    public void offPlaceBlock(BlockPlaceEvent event) {
        if (event.getBlock() == null) return;
        Block block = event.getBlock();
        if (Mine.isBlockInMine(block, gameManager.getGame(Game.playerGame.get(event.getPlayer())), event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        player.setLevel(0);
        player.setHealth(20);
        Game game = gameManager.getGame(Game.playerGame.get(player));
        if (game != null) {
            if (game.getPlayerInGame().contains(player)) game.removePlayerInGame(player, "lose");
        }
    }

    @EventHandler
    public void enablePlugin(PluginEnableEvent event) {
        WorldCreator creator = new WorldCreator("lobby");
        creator.createWorld();

        SpawnEntities spawnEntities = new SpawnEntities();

        spawnEntities.spawnNPCStartGame();
        startGameVillager = spawnEntities.getVillagerUUID();
    }

    @EventHandler
    public void playerJoinServer(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        World lobby = Bukkit.getWorld("lobby");
        lobby.setAutoSave(false);
        lobby.setDifficulty(Difficulty.PEACEFUL);
        lobby.setPVP(false);
        Location spawnLocation = lobby.getSpawnLocation();
        player.teleport(spawnLocation);
        player.getInventory().clear();
        player.setLevel(0);
        player.setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.getWorld().getName().equalsIgnoreCase("lobby")) {
            World worldLobby = Bukkit.getWorld("lobby");
            player.spigot().respawn();
            player.teleport(worldLobby.getSpawnLocation());
        }
    }

    @EventHandler
    public void killMob(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        event.getDrops().clear();
        event.setDroppedExp(0);
        if (entity instanceof CraftZombie) {
            EntityZombie zombie = ((CraftZombie) entity).getHandle();

            if (((Zombie) entity).getKiller() != null) {
                Player player = ((Zombie) entity).getKiller();
                Game game = gameManager.getGame(Game.playerGame.get(player));
                WaveMob waveMob = game.getWaveMob();
                if (waveMob == null) return;

                waveMob.killZombie(zombie, game.getColorPlayer(player), game);
                game.addMoneyPlayer(player, 15);
                player.sendMessage("Вы получили +15 монет за убийство моба");
            }
        }
    }

    @EventHandler
    public void startGame(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (entity.getUniqueId() != null && entity instanceof Villager) {
            Villager villager = (Villager) entity;
            villager.setRecipes(Collections.emptyList());
            event.setCancelled(true);
            if (entity.getUniqueId() == startGameVillager) {
                if (gameManager.getAllGames().isEmpty()) {
                    UUID createGameUUID = gameManager.createGame().getGameId();

                    Game game = gameManager.getGame(createGameUUID);
                    if (game != null) game.addPlayerInGame(player, gameManager);

                    return;
                }
                for (Game g : gameManager.getAllGames()) {

                    if (g.getGameState() == GameState.RUN) continue;
                    if (g.getGameState() == GameState.STOP) continue;

                    if (g.getGameState() == GameState.WAIT && g.getPlayerInGame().size() != 2) {
                        g.addPlayerInGame(player, gameManager);
                        return;
                    }
                }
                UUID createGameUUID = gameManager.createGame().getGameId();

                Game game = gameManager.getGame(createGameUUID);
                if (game != null) game.addPlayerInGame(player, gameManager);

            }
        }
    }

    @EventHandler
    public void inventoryDropProtected(PlayerDropItemEvent event) {
        if (event.getItemDrop() == null || event.getItemDrop().getItemStack().getType() == Material.AIR) return;
        if (ItemUtil.isProtectedItem(event.getItemDrop().getItemStack())) event.setCancelled(true);
    }

    @EventHandler
    public void inventoryClickProtected(InventoryClickEvent event) {
        int[] protectedSlot = new int[]{0, 1, 2};
        Game game = gameManager.getGame(Game.playerGame.get((Player) event.getWhoClicked()));
        if (game.getGameState() == GameState.WAIT) protectedSlot = new int[]{0, 1, 2, 8};

        ItemStack item = event.getCurrentItem();

        if (event.getHotbarButton() >= 0) {
            int hotbarSlot = event.getHotbarButton();
            for (int itemMain : protectedSlot) {
                if (hotbarSlot == itemMain) {
                    event.setCancelled(true);
                }
            }
        }
        if (ItemUtil.isProtectedItem(item)) event.setCancelled(true);

    }

    @EventHandler
    public void inventoryDragProtected(InventoryDragEvent event) {
        if (event.getCursor() == null || event.getCursor().getType() == Material.AIR) return;
        if (ItemUtil.isProtectedItem(event.getCursor())) event.setCancelled(true);

    }

    @EventHandler
    public void teleportPlayerSuperMine(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null
                && event.getClickedBlock().getType() != Material.AIR
                && event.getClickedBlock().getType() == Material.WALL_SIGN) {

            if (event.getAction().toString().contains("RIGHT_CLICK")) {
                Player player = event.getPlayer();
                Block clickedBlock = event.getClickedBlock();

                if (clickedBlock.getLocation().equals(FileUtil.getLocationSign(player.getWorld()))) {
                    Game game = gameManager.getGame(Game.playerGame.get(player));
                    player.teleport(FileUtil.getLocationSpawnPlayer(game.getColorPlayer(player), player.getWorld()));
                    return;
                }

                for (int i = 1; i <= 8; i++) {
                    if (clickedBlock.getLocation().equals(FileUtil.getLocationSign(i, event.getClickedBlock().getWorld()))) {
                        player.teleport(FileUtil.getLocationSpawnSuperMine(player.getWorld()));
                        player.sendMessage("Вы были перемещены в драгоценную шахту");
                    }
                }
            }
        }
    }

    @EventHandler
    public void updateItem(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item != null && item.getType() != Material.AIR) {
            if (event.getClickedInventory() != null) {
                Inventory currentInv = event.getClickedInventory();
                if (currentInv.getTitle().equalsIgnoreCase("прокачка")) {
                    event.setCancelled(true);
                    if (ItemUtil.isStartItem(item)) return;
                    ;
                    Player player = (Player) event.getWhoClicked();
                    Game game = gameManager.getGame(Game.playerGame.get(player));


                    ItemUtil.updateItem(item, game, player);

                    player.closeInventory();
                    UpdateItemGui inv = new UpdateItemGui(player);
                    player.openInventory(inv.getInventory());
                }
            }
        }
    }

    @EventHandler
    public void openGui(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (entity instanceof Villager) {
            Villager villager = (Villager) entity;

            if (villager.getCustomName().equalsIgnoreCase("продажа")) {
                SellItemGui sellItemGui = new SellItemGui();

                player.openInventory(sellItemGui.getInventory());
            }
            if (villager.getCustomName().equalsIgnoreCase("прокачка")) {
                UpdateItemGui updateItemGui = new UpdateItemGui(player);

                player.openInventory(updateItemGui.getInventory());
            }
        }
    }

    @EventHandler
    public void load(WorldLoadEvent event) {
        World world = event.getWorld();
        world.setAutoSave(false);

        for (Entity e : world.getEntities())
            if (e.getType() != EntityType.PLAYER && e.getUniqueId() != startGameVillager) e.remove();

        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                world.loadChunk(x, z, true);
            }
        }
    }

    @EventHandler
    public void deleteWorldInStopServer(PluginDisableEvent event) {
        for (File file : Objects.requireNonNull(Bukkit.getWorldContainer().listFiles())) {
            if (file.getName().contains("map_active_")) {
                World world = Bukkit.getWorld(file.getName());
                WorldUtil.unloadWorld(world);
                WorldUtil.deleteWorld(file);
            }
        }
    }

    @EventHandler
    public void craftEvent(CraftItemEvent event) {
        if (!CraftUtil.isCraftingItem(event.getCurrentItem())) {
            event.setCancelled(true);
            return;
        }

        ItemStack item = event.getRecipe().getResult();
        if (!ItemUtil.isProtectedItem(item)) return;
        CraftingInventory inventory = event.getInventory();
        Player player = (Player) event.getWhoClicked();

        ItemUtil.setUnbreakable(item);

        if (ItemUtil.isChestplate(item)) {
            if (player.getInventory().getChestplate() != null && player.getInventory().getChestplate().getType() == item.getType())
                return;
            player.getInventory().setChestplate(item);
        }
        if (ItemUtil.isLeggings(item)) {
            if (player.getInventory().getLeggings() != null && player.getInventory().getLeggings().getType() == item.getType())
                return;
            player.getInventory().setLeggings(item);
        }
        if (ItemUtil.isHelmet(item)) {
            if (player.getInventory().getHelmet() != null && player.getInventory().getHelmet().getType() == item.getType())
                return;
            player.getInventory().setHelmet(item);
        }
        if (ItemUtil.isBoots(item)) {
            if (player.getInventory().getBoots() != null && player.getInventory().getBoots().getType() == item.getType())
                return;
            player.getInventory().setBoots(item);
        }
        if (ItemUtil.isSword(item)) {
            if (player.getInventory().getItem(0).getType() == item.getType()) return;
            player.getInventory().setItem(0, item);
        }
        if (ItemUtil.isPickaxe(item)) {
            if (player.getInventory().getItem(1).getType() == item.getType()) return;
            player.getInventory().setItem(1, item);
        }
        if (ItemUtil.isAxe(item)) {
            if (player.getInventory().getItem(2).getType() == item.getType()) return;
            player.getInventory().setItem(2, item);
        }
        event.setCurrentItem(null);
        CraftUtil.craftingInventoryUpdate(inventory);
    }

    @EventHandler
    public void playerOffDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Game game = gameManager.getGame(Game.playerGame.get(player));
            if (game != null) {
                if (game.getGameState() == GameState.WAIT) event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void sellItem(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            Inventory currentInv = event.getClickedInventory();
            if (currentInv.getTitle().equalsIgnoreCase("продажа")) {
                Player player = (Player) event.getWhoClicked();
                ItemUtil.givePlayerMoney(gameManager.getGame(Game.playerGame.get(player)), player, event.getCurrentItem());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void openSelectTeamInventory(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().toString().contains("RIGHT_CLICK") &&
                player.getInventory() != null &&
                player.getInventory().getItemInMainHand().getType() == Material.FIREBALL &&
                player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("Выбор команды")) {
            SelectTeamGui selectTeamGui = new SelectTeamGui(gameManager, player);
            event.setCancelled(true);

            player.openInventory(selectTeamGui.getInventory());
        }
    }

    @EventHandler
    public void selectTeamPlayer(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getTitle().equalsIgnoreCase("Выбор команды")) {
            event.setCancelled(true);
            Game game = gameManager.getGame(Game.playerGame.get(player));
            ItemStack currentItem = event.getCurrentItem();
            if (currentItem == null || currentItem.getType() == Material.AIR) return;

            short colorGlass = currentItem.getDurability();

            if (!game.getColorTeamPlayer().isEmpty()) {
                for (String color : game.getColorTeamPlayer().keySet()) {
                    if (game.getColorTeamPlayer().get(color) == player) {

                        String preColor = color;
                        game.getColorTeamPlayer().remove(preColor);
                        game.getAvailableTeam().add(preColor);
                        break;
                    }
                }
            }
            switch (colorGlass) {
                case 14:
                    game.selectPlayerTeam("red", player);
                    break;
                case 13:
                    game.selectPlayerTeam("green", player);
                    break;
                case 2:
                    game.selectPlayerTeam("purple", player);
                    break;
                case 9:
                    game.selectPlayerTeam("cyan", player);
                    break;
            }
            player.closeInventory();
        }
    }

    @EventHandler
    public void offFireEntity(EntityCombustEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void inventorySwapProtected(PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void food(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}
