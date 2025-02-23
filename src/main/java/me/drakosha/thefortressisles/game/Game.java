package me.drakosha.thefortressisles.game;

import lombok.Getter;
import me.drakosha.thefortressisles.TheFortressIsles;
import me.drakosha.thefortressisles.game.map.LocalMap;
import me.drakosha.thefortressisles.game.mine.TimerRepairMine;
import me.drakosha.thefortressisles.game.mob.WaveMob;
import me.drakosha.thefortressisles.game.player.PlayerCreateTimerForStartSpawnMob;
import me.drakosha.thefortressisles.game.player.lighthouse.Lighthouse;
import me.drakosha.thefortressisles.game.util.FileUtil;
import me.drakosha.thefortressisles.game.util.GameUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;


@Getter
public class Game {
    private final WaveMob waveMob;
    public static final ArrayList<String> allColor = GameUtil.initListAllColor();
    public static Map<Player, UUID> playerGame = new HashMap<>();


    private final UUID gameId;
    private final List<Player> playerInGame;

    private final World world;
    private GameState gameState;
    private final Map<Player, Integer> balance;
    private final Map<String, Player> colorTeamPlayer;
    private final ArrayList<String> availableTeam;

    private final Lighthouse lighthouse;
    private final PlayerCreateTimerForStartSpawnMob startTimer;

    public Game(UUID gameId, World world) {
        availableTeam = GameUtil.initListAllColor();
        lighthouse = new Lighthouse();
        waveMob = new WaveMob(lighthouse);
        playerInGame = new ArrayList<>();
        colorTeamPlayer = new HashMap<>();
        balance = new HashMap<>();
        startTimer = new PlayerCreateTimerForStartSpawnMob(this);
        gameState = GameState.WAIT;

        this.gameId = gameId;
        this.world = world;
    }

    public void addPlayerInGame(Player player, GameManager gameManager) {
        playerInGame.add(player);

        playerGame.put(player, gameId);
        GameUtil.preparationPlayer(player, world);
        balance.put(player, 0);
        if (playerInGame.size() == 2) startGame(gameManager);

    }

    public String getColorPlayer(Player player) {
        return colorTeamPlayer.entrySet()
                .stream()
                .filter(color -> color.getValue().equals(player))
                .map(Map.Entry::getKey)
                .findFirst().orElse(null);
    }

    private void selectTeamPlayerIfNotTeam() {
        if (colorTeamPlayer.size() == playerInGame.size()) {
            return;
        }
        playerInGame.stream()
                .filter(player -> getColorPlayer(player) == null)
                .forEach(player -> {
                    String randomColor = GameUtil.randomColor(availableTeam);
                    selectPlayerTeam(randomColor, player);
                });
    }

    public void selectPlayerTeam(String color, Player player) {
        if (colorTeamPlayer.get(color) != null) {
            player.sendMessage("Команда уже занята");
            return;
        }
        colorTeamPlayer.put(color, player);
        availableTeam.remove(color);
        switch (color) {
            case "red":
                player.sendMessage("Вы выбрали красную команду");
                break;
            case "cyan":
                player.sendMessage("Вы выбрали бирюзовую команду");
                break;
            case "purple":
                player.sendMessage("Вы выбрали фиолетовую команду");
                break;
            case "green":
                player.sendMessage("Вы выбрали зеленую команду");
                break;
        }
    }

    public void addMoneyPlayer(Player player, int count) {
        if (!playerInGame.contains(player)) {
            return;
        }

        int currentBalance = balance.get(player) + count;
        player.setLevel(currentBalance);
        balance.put(player, currentBalance);
    }

    public void removeMonetPlayer(Player player, int count) {
        if (!playerInGame.contains(player)) {
            return;
        }
        if (balance.get(player) - count < 0) return;

        int currentBalance = balance.get(player) - count;
        player.setLevel(currentBalance);
        balance.put(player, currentBalance);
    }

    public void removePlayerInGame(Player player, String winOrLose) {
        if (!playerInGame.contains(player)) return;
        if (getColorPlayer(player) != null) {
            String color = getColorPlayer(player);
            colorTeamPlayer.remove(color);
        }
        player.setLevel(0);
        playerInGame.remove(player);
        playerGame.remove(player);
        player.getInventory().clear();

        String message = winOrLose.equals("win") ? "Вы выиграли" : "Вы проиграли";
        player.sendMessage(message);
        player.teleport(Bukkit.getWorld("lobby").getSpawnLocation());
    }

    public void startGame(GameManager gameManager) {
        timerForStartGame(gameManager);
    }

    public void endGame() {
        LocalMap localMap = new LocalMap(new File(TheFortressIsles.getInstance().getDataFolder().getPath() + "/maps"),
                "map", true, gameId.toString());
        gameState = GameState.STOP;
        if (!playerInGame.isEmpty()) {
            for (Player p : playerInGame) {
                if (p.getWorld() != localMap.getWorld()) continue;
                removePlayerInGame(p, "win");
                p.sendMessage("Игра окончена");
                p.teleport(Bukkit.getWorld("lobby").getSpawnLocation());
            }
        }
        localMap.unload();
    }

    private void timerForStartGame(GameManager gameManager) {
        TimerRepairMine timerRepairMine = new TimerRepairMine();

        new BukkitRunnable() {
            int second = 15;

            @Override
            public void run() {
                if (second <= 0) {
                    timerRepairMine.startTimerForRepairMine(Game.this);
                    selectTeamPlayerIfNotTeam();
                    gameState = GameState.RUN;
                    for (String color : colorTeamPlayer.keySet()) {
                        getColorTeamPlayer().get(color).sendTitle("Игра началась", null, 10, 20, 10);
                        lighthouse.setHealthTeam(color, 200);

                        Player p = colorTeamPlayer.get(color);
                        p.getInventory().setItem(8, new ItemStack(Material.AIR));
                        p.teleport(FileUtil.getLocationSpawnPlayer(color, world));
                        p.setBedSpawnLocation(FileUtil.getLocationSpawnPlayer(color, world), true);

                    }
                    GameUtil.checkLighthouseDeath(Game.this, gameManager);
                    startTimer.startBossBarTimer();
                    cancel();
                    return;
                }
                for (Player p : playerInGame) {
                    if (second <= 5) p.sendTitle("До начала " + second, null, 10, 100, 10);

                    p.sendMessage("До начала игры " + second);
                }
                second--;
            }
        }.runTaskTimer(TheFortressIsles.getInstance(), 0L, 20L);
    }
}
