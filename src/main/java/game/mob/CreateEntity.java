package game.mob;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static game.mine.Mine.teamColor;

public class CreateEntity {
    public static Map<String, ArmorStand> activeArmorStand = new HashMap<>();

    public static ArrayList<UUID> villagerUpdate = new ArrayList<>();
    public static ArrayList<UUID> villagerSeller = new ArrayList<>();

    public static Map<String, Location> villagerSpawnLocationUpdate = new HashMap<>();
    public static Map<String, Location> villagerSpawnLocationSeller = new HashMap<>();

    public static ArrayList<UUID> activeEntity = new ArrayList<>();

    public static void createArmorStand(Location location, String name, String color){
        if (activeArmorStand.get(color) != null && !activeArmorStand.get(color).isDead()){
            activeArmorStand.get(color).setCustomName(name);
            return;
        }

        World world = Bukkit.getWorlds().get(0);
        ArmorStand armorStand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);

        armorStand.setCustomName(name);
        armorStand.setCustomNameVisible(true);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.setSilent(true);
        armorStand.setVisible(false);
        armorStand.setSilent(true);

        activeArmorStand.put(color, armorStand);
    }

    public static void spawnNPC(String name) {
        World world = Bukkit.getWorlds().get(0);

        for (String color : teamColor) {
            if (name.equalsIgnoreCase("продажа")) {
                Villager sell = (Villager) world.spawnEntity(villagerSpawnLocationSeller.get(color), EntityType.VILLAGER);
                activeEntity.add(sell.getUniqueId());
                villagerSeller.add(sell.getUniqueId());
                sell.setCustomName(name);

                sell.setAI(false);
                sell.setInvulnerable(true);
                sell.setCustomNameVisible(true);
                sell.setSilent(true);
            }

            if (name.equalsIgnoreCase("прокачка")) {
                Villager update = (Villager) world.spawnEntity(villagerSpawnLocationUpdate.get(color), EntityType.VILLAGER);
                activeEntity.add(update.getUniqueId());
                villagerUpdate.add(update.getUniqueId());
                update.setCustomName(name);

                update.setAI(false);
                update.setInvulnerable(true);
                update.setCustomNameVisible(true);
                update.setSilent(true);
            }
        }
    }
}
