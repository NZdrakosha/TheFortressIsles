package game.mob;

import game.mob.pathFinder.LighthouseAttackPathfinderGoal;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftZombie;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static game.Game.equippedTeam;
import static game.mob.Lighthouse.*;
public class SpawnMob{
    public static Map<String, UUID> aliveZombieTeam = new HashMap<>();
    public static Map<String, ArrayList<Location>> locationSpawnZombie = new HashMap<>();
    public static ArrayList<Location> locationSpawnZombieRed = new ArrayList<>();
    public static ArrayList<Location> locationSpawnZombieCyan = new ArrayList<>();
    public static ArrayList<Location> locationSpawnZombieGreen = new ArrayList<>();
    public static ArrayList<Location> locationSpawnZombiePurple = new ArrayList<>();


    public static void teamSpawnMob(){
        for (String color : equippedTeam.keySet()) {
            for (int i = 0; i < locationSpawnZombie.get(color).size(); i++) {
                spawnMob(color, locationSpawnZombie.get(color).get(i));
            }
        }
    }

    private static void spawnMob(String color, Location loc){
        if (equippedTeam.get(color)){

        }
        Location targetLocationTeam =  lighthouseLocationTeam.get(color);
        EntityZombie zombie = ((CraftZombie) targetLocationTeam.getWorld().spawnEntity(loc, EntityType.ZOMBIE)).getHandle();

        aliveZombieTeam.put(color, zombie.getUniqueID());
        zombie.setNoAI(false);
        zombie.setBaby(false);
        zombie.setHealth(6.0f);
        zombie.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.2);


        Lighthouse lighthouse = new Lighthouse();

        zombie.goalSelector.a(0, new LighthouseAttackPathfinderGoal<>(zombie, lighthouse, color));
    }
}
