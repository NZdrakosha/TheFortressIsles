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
    private static Map<String, ArmorStand> activeArmorStand = new HashMap<>();

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

    public static void spawnNpcUpdate(){
        World world = Bukkit.getWorlds().get(0);
        for (String color : teamColor) {
            Villager villager = (Villager) world.spawnEntity(villagerSpawnLocationUpdate.get(color), EntityType.VILLAGER);
            activeEntity.add(villager.getUniqueId());
            villagerUpdate.add(villager.getUniqueId());

            villager.setAI(false);
            villager.setInvulnerable(true);
            villager.setCustomNameVisible(true);
            villager.setCustomName("Прокачка");
            villager.setSilent(true);
        }
    }
    public static void spawnNpcSeller(){
        World world = Bukkit.getWorlds().get(0);
        for (String color : teamColor){
            Villager villager = (Villager) world.spawnEntity(villagerSpawnLocationSeller.get(color), EntityType.VILLAGER);
            activeEntity.add(villager.getUniqueId());
            villagerSeller.add(villager.getUniqueId());

            villager.setAI(false);
            villager.setInvulnerable(true);
            villager.setCustomNameVisible(true);
            villager.setCustomName("Продажа");
            villager.setSilent(true);
        }
    }
}
