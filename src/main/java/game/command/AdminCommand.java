package game.command;

import game.gui.InventorySelectTeam;
import game.mob.Lighthouse;
import me.drakosha.thefortressisles.TheFortressIsles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import static game.Game.*;
import static game.mine.Mine.*;
import static game.mob.CreateEntity.*;
import static game.mob.Lighthouse.reduceHealth;


public class AdminCommand implements CommandExecutor {
    private static ArrayList<Player> listTeamEquippedPlayer = new ArrayList<>();
    private static boolean activeTimer = false;
    public static boolean activeGame = false;
    static Random random = new Random();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (args.length < 1) {
            return false;
        }
        Player player = (Player) sender;
        if (args[0].equalsIgnoreCase("start") && player.hasPermission("op")) {
            if (activeGame) {
                return false;
            }
            activeGame = true;

            spawnNpcUpdate();
            spawnNpcSeller();
            fillSuperMine();
            teleportPlayerSpawnLocation();
            fillMine();
            checkTeamPlayer();
            startTimerForRepairMine();
            return true;
        }
        if (args[0].equalsIgnoreCase("health")){
            player.sendMessage("Здоровье маяка = " + Lighthouse.getHealth(activeTeam.get(player.getUniqueId())));
        }
        if (args[0].equalsIgnoreCase("team")) {
            if (!activeGame){
                return false;
            }
            InventorySelectTeam inventory = new InventorySelectTeam();
            player.openInventory(inventory.getInventory());
            return true;
        }

        if (args[0].equalsIgnoreCase("copy")) {

            double x = player.getLocation().getX();
            double y = player.getLocation().getY();
            double z = player.getLocation().getZ();

            String coords = String.format(Locale.ENGLISH, "%.2f, %.2f, %.2f", x, y, z);

            try {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(coords), null);

                player.sendMessage("§aВаши координаты скопированы в буфер обмена: " + coords);
            } catch (Exception e) {
                player.sendMessage("§cОшибка при копировании координат.");
                e.printStackTrace();
            }


        }
        return true;
    }
    private static void teleportPlayerSpawnLocation(){
        Location locationSpawn = new Location(Bukkit.getWorlds().get(0), 134.5, 205, 102.5);
        locationSpawn.setYaw(90.0f);
        for(Player p : Bukkit.getOnlinePlayers()){
            p.teleport(locationSpawn);
            giveStartLot(p);
        }
    }

    private static void checkTeamPlayer() {
        if (activeTimer) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                int playerOnStartLobby = 0;
                int playersInTeam = 0;

                for (Player p : Bukkit.getOnlinePlayers()) {
                    playerOnStartLobby++;

                    if (activeTeam.containsKey(p.getUniqueId())) {
                        listTeamEquippedPlayer.add(p);
                        playersInTeam++;
                    }
                }

                if (playerOnStartLobby == playersInTeam) {
                    timerForStartGame();
                    activeTimer = true;
                    cancel();
                } else {
                    Bukkit.getLogger().info("Не все игроки выбрали команду.");
                }
            }
        }.runTaskTimer(TheFortressIsles.getInstance(), 0L,20L);
    }

    public static void timerForStartGame() {


        for (Player player : Bukkit.getOnlinePlayers()) {
            new BukkitRunnable() {
                int second = 15;

                @Override
                public void run() {


                    player.sendMessage("До начала игры " + second + " секунд");
                    if (second <= 5) {
                        player.sendTitle(String.valueOf(second), null, 10, 100, 10);
                    }

                    if (second <= 0) {
                        player.sendTitle("Игра началась", null, 10, 100, 10);
                        teleportPlayerBase();
                        reduceHealth();
                        cancel();
                    }
                    second--;
                }
            }.runTaskTimer(TheFortressIsles.getInstance(), 0L, 20L);
        }
    }
    private static void teleportPlayerBase() {
        for (String color : teamColor) {
            Player p = Bukkit.getPlayer(colorTeamPlayer.get(color));
            if (p != null) {
                Location spawnLocation = locationSpawnPlayer.get(color);
                spawnLocation.setYaw(90.0f) ;
                if (spawnLocation != null && spawnLocation.getWorld() != null) {
                    p.teleport(spawnLocation);
                    startBossBarTimer(60, p, "До начала волны");
                    p.setBedSpawnLocation(spawnLocation, true);
                }
            }
        }
    }
    }