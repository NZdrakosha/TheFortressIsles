package me.drakosha.thefortressisles.game;

import lombok.Getter;
import me.drakosha.thefortressisles.TheFortressIsles;
import me.drakosha.thefortressisles.game.map.LocalMap;
import me.drakosha.thefortressisles.game.mine.Mine;
import me.drakosha.thefortressisles.game.mob.SpawnEntities;
import org.bukkit.Difficulty;
import org.bukkit.World;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class GameManager {
    private final Map<UUID, Game> activeGame;

    public GameManager() {
        activeGame = new HashMap<>();
    }

    public Game createGame() {
        UUID gameId = UUID.randomUUID();

        LocalMap localMap = new LocalMap(new File(TheFortressIsles.getInstance().getDataFolder().getPath() + "/maps"),
                "map", true, gameId.toString());
        localMap.load();

        World worldMap = localMap.getWorld();
        Mine mine = new Mine();
        SpawnEntities spawnEntities = new SpawnEntities();
        Game game = new Game(gameId, worldMap);

        activeGame.put(gameId, game);

        spawnEntities.spawnNpc("Прокачка", game);
        spawnEntities.spawnNpc("Продажа", game);

        mine.fillBaseMine(worldMap);
        mine.fillSuperMine(worldMap);

        worldMap.setDifficulty(Difficulty.HARD);
        return game;
    }

    public void removeGame(UUID gameId) {
        Game game = activeGame.remove(gameId);
        game.endGame();
    }

    public Game getGame(UUID gameId) {
        return activeGame.get(gameId);
    }

    public Collection<Game> getAllGames() {
        return activeGame.values();
    }
}
