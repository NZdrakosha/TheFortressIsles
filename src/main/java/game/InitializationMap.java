package game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import static game.Game.*;
import static game.mob.CreateEntity.*;
import static game.gui.InventorySelectTeam.*;
import static game.mob.Lighthouse.*;
import static game.mob.SpawnMob.*;
import static game.mine.Mine.*;
public class InitializationMap {
    public static void init(){
        World world = Bukkit.getWorlds().get(0);
        world.setAutoSave(false);

        materialMineList.add(0, Material.STONE);
        materialMineList.add(1, Material.COAL_ORE);
        materialMineList.add(2, Material.IRON_ORE);
        materialMineList.add(3, Material.LOG);
        materialMineList.add(4, Material.LAPIS_ORE);
        materialMineList.add(5, Material.DIRT);
        materialMineList.add(6, Material.REDSTONE_ORE);

        materialListSuperMine.add(0, Material.STONE);
        materialListSuperMine.add(1, Material.GOLD_ORE);
        materialListSuperMine.add(2, Material.EMERALD_ORE);
        materialListSuperMine.add(3, Material.DIAMOND_ORE);
        materialListSuperMine.add(4, Material.OBSIDIAN);

        zoneSuperMine1 = new Location(world, 129, 78, 114);
        zoneSuperMine2 = new Location(world, 142, 89, 100);

        locationSign.add(new Location(world, 80, 125, 50));
        locationSign.add(new Location(world, 80, 125, 165));
        locationSign.add(new Location(world, 205, 125, 164));
        locationSign.add(new Location(world, 205, 125, 49 ));

        zoneMineTeam1.put("red", new Location(world, 44, 122, 23));
        zoneMineTeam2.put("red", new Location(world, 101, 93, 69));

        zoneMineTeam1.put("cyan", new Location(world, 226, 122,68));
        zoneMineTeam2.put("cyan", new Location(world, 169, 93, 22));

        zoneMineTeam1.put("purple", new Location(world, 226, 122, 183));
        zoneMineTeam2.put("purple", new Location(world, 169, 93,137));

        zoneMineTeam1.put("green", new Location(world, 101, 122, 184));
        zoneMineTeam2.put("green", new Location(world, 44, 93, 138));

        teamColor.add(0, "red");
        teamColor.add(1, "cyan");
        teamColor.add(2, "purple");
        teamColor.add(3, "green");

        coordinatesArmorStand.put("red", new Location(world, 83.70, 124.28, 46.14));
        coordinatesArmorStand.put("cyan", new Location(world, 209.32, 124.28, 44.96));
        coordinatesArmorStand.put("purple", new Location(world, 208.90, 124.28, 160.14));
        coordinatesArmorStand.put("green", new Location(world, 84.24, 124.28, 160.84));

        for (int i = 0; i < teamColor.size(); i++){
            boolean value = true;
            boolean valueEquipped = false;
            double health = 200;
            canRepairMine.put(teamColor.get(i), value);
            equippedTeam.put(teamColor.get(i), valueEquipped);
            healthTeam.put(teamColor.get(i), health);
        }

        glassList.put("red", redGlass);
        glassList.put("cyan", cyanGlass);
        glassList.put("green", greenGlass);
        glassList.put("purple", purpleGlass);

        locationSpawnPlayer.put("red", new Location(world, 76.16, 124.00, 45.97));
        locationSpawnPlayer.put("cyan", new Location(world, 201.33, 124.00, 45.01));
        locationSpawnPlayer.put("green", new Location(world, 76.40, 124.00, 161.00));
        locationSpawnPlayer.put("purple", new Location(world, 201.40, 124.00, 159.96));

        villagerSpawnLocationUpdate.put("red", new Location(world, 78.65, 124.00, 57.08));
        villagerSpawnLocationUpdate.put("cyan", new Location(world, 203.93, 124.00, 56.17));
        villagerSpawnLocationUpdate.put("green", new Location(world, 78.75, 124.00, 172.34));
        villagerSpawnLocationUpdate.put("purple", new Location(world, 203.72, 124.00, 171.21));

        villagerSpawnLocationSeller.put("red", new Location(world,78.97, 124.00, 34.35));
        villagerSpawnLocationSeller.put("cyan", new Location(world,203.58, 124.00, 33.50));
        villagerSpawnLocationSeller.put("green", new Location(world, 78.80, 124.00, 149.75));
        villagerSpawnLocationSeller.put("purple", new Location(world,203.82, 124.00, 148.64));

        lighthouseLocationTeam.put("red", new Location(world, 65.98, 127.00, 46.00));
        lighthouseLocationTeam.put("cyan", new Location(world, 190.97, 127.00, 45.00));
        lighthouseLocationTeam.put("green", new Location(world, 65.92, 127.00, 161.02));
        lighthouseLocationTeam.put("purple", new Location(world, 190.97, 127.00, 160.03));

        signTeleportSuperMine.add(new Location(world, 174, 125, 45));
        signTeleportSuperMine.add(new Location(world, 174, 125, 44));
        signTeleportSuperMine.add(new Location(world, 49, 125, 46));
        signTeleportSuperMine.add(new Location(world, 49, 125, 45));
        signTeleportSuperMine.add(new Location(world, 49, 125, 161));
        signTeleportSuperMine.add(new Location(world, 49, 125, 160));
        signTeleportSuperMine.add(new Location(world, 174, 125, 160));
        signTeleportSuperMine.add(new Location(world, 174, 125, 159));


        locationSpawnZombieRed.add(new Location(world, 73.63, 124.00, 60.21));
        locationSpawnZombieRed.add(new Location(world, 59.65, 124.00, 60.38));
        locationSpawnZombieRed.add(new Location(world, 51.79, 124.00, 52.58));
        locationSpawnZombieRed.add(new Location(world, 51.53, 124.00, 39.32));
        locationSpawnZombieRed.add(new Location(world, 59.60, 124.00, 31.65));
        locationSpawnZombieRed.add(new Location(world, 72.81, 124.00, 31.82));
        locationSpawnZombie.put("red", locationSpawnZombieRed);

        locationSpawnZombieCyan.add(new Location(world, 196.74, 124.50, 30.08));
        locationSpawnZombieCyan.add(new Location(world, 184.27, 124.00, 30.62));
        locationSpawnZombieCyan.add(new Location(world, 176.69, 124.00, 38.71));
        locationSpawnZombieCyan.add(new Location(world, 176.48, 124.00, 51.89));
        locationSpawnZombieCyan.add(new Location(world, 184.01, 124.50, 59.72));
        locationSpawnZombieCyan.add(new Location(world, 197.36, 124.00, 59.42));
        locationSpawnZombie.put("cyan", locationSpawnZombieCyan);

        locationSpawnZombiePurple.add(new Location(world, 197.85, 124.00, 145.63));
        locationSpawnZombiePurple.add(new Location(world, 184.72, 124.00, 145.80));
        locationSpawnZombiePurple.add(new Location(world, 176.55, 124.00, 154.00));
        locationSpawnZombiePurple.add(new Location(world, 176.41, 124.00, 166.32));
        locationSpawnZombiePurple.add(new Location(world, 184.86, 124.00, 174.68));
        locationSpawnZombiePurple.add(new Location(world, 197.78, 124.00, 174.09));
        locationSpawnZombie.put("purple", locationSpawnZombiePurple);


        locationSpawnZombieGreen.add(new Location(world, 71.74, 124.50, 175.71));
        locationSpawnZombieGreen.add(new Location(world, 59.61, 124.50, 175.76));
        locationSpawnZombieGreen.add(new Location(world, 51.49, 124.00, 166.92));
        locationSpawnZombieGreen.add(new Location(world, 51.56, 124.00, 154.83));
        locationSpawnZombieGreen.add(new Location(world, 59.50, 124.00, 146.59));
        locationSpawnZombieGreen.add(new Location(world, 72.92, 124.50, 146.26));
        locationSpawnZombie.put("green", locationSpawnZombieGreen);


    }
}
