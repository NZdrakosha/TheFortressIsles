package me.drakosha.thefortressisles.game.player.lighthouse;

import me.drakosha.thefortressisles.game.util.FileUtil;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

public class Lighthouse {
    private final Map<String, Integer> healthTeam = new HashMap<>();

    public Location getLocation(String color, World world) {
        return FileUtil.getLocationLighthouse(color, world);
    }

    public double getHealth(String color) {
        if (healthTeam.get(color) == null) return 0.0;
        return healthTeam.get(color);
    }

    public void setHealthTeam(String color, int health) {
        this.healthTeam.put(color, health);
    }

    public void reduceHealth(String color, int health) {
        if (healthTeam.isEmpty() || healthTeam.get(color) == null) return;

        if (healthTeam.get(color) <= 0) {
            healthTeam.remove(color);
            return;
        }
        int currentHealth = healthTeam.get(color) - health;
        healthTeam.put(color, currentHealth);
    }
}
