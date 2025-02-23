package me.drakosha.thefortressisles.game.mob;

import lombok.Getter;
import me.drakosha.thefortressisles.game.Game;
import me.drakosha.thefortressisles.game.mob.pathfinder.AttackPathfinderGoal;
import me.drakosha.thefortressisles.game.player.lighthouse.Lighthouse;
import me.drakosha.thefortressisles.game.util.FileUtil;
import net.minecraft.server.v1_12_R1.EntityZombie;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftZombie;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class SpawnEntities {
    private final Map<Game, ArrayList<ArmorStand>> activeArmorStand = new HashMap<>();
    private final ArrayList<ArmorStand> armorStands = new ArrayList<>();
    private UUID villagerUUID;

    public void spawnNPCStartGame() {
        Location location = FileUtil.getSpawnLocationNpc();


        location.setYaw(180);
        Villager villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        villager.setAI(false);
        villager.setInvulnerable(true);
        villager.setProfession(Villager.Profession.LIBRARIAN);
        villager.setCustomName("Начать игру");
        villager.setCustomNameVisible(true);
        villagerUUID = villager.getUniqueId();
    }

    public void spawnNpc(String name, Game game) {
        World world = game.getWorld();
        if (name.equalsIgnoreCase("прокачка")) {
            for (int i = 1; i <= 4; i++) {
                Villager update = (Villager) world.spawnEntity(FileUtil.getLocationSpawnNpcUpdate(i, world), EntityType.VILLAGER);
                update.setCustomName(name);

                update.setAI(false);
                update.setInvulnerable(true);
                update.setCustomNameVisible(true);
                update.setSilent(true);
            }
        }
        if (name.equalsIgnoreCase("продажа")) {
            for (int i = 1; i <= 4; i++) {
                Villager sell = (Villager) world.spawnEntity(FileUtil.getLocationSpawnNpcSell(i, world), EntityType.VILLAGER);
                sell.setCustomName(name);

                sell.setAI(false);
                sell.setInvulnerable(true);
                sell.setCustomNameVisible(true);
                sell.setSilent(true);
            }
        }
    }

    public void createArmorStand(World world, String name, Game game) {
        if (!armorStands.isEmpty()) {
            for (ArmorStand armorStand : activeArmorStand.get(game)) {
                if (armorStand != null && !armorStand.isDead()) {
                    armorStand.setCustomName(null);
                    armorStand.setCustomName(name);
                }
            }
        }

        if (activeArmorStand.isEmpty()) {
            for (int i = 1; i <= 4; i++) {
                ArmorStand armorStand = (ArmorStand) world.spawnEntity(FileUtil.getLocationArmorStand(i, world), EntityType.ARMOR_STAND);

                armorStand.setCustomName(name);
                armorStand.setCustomNameVisible(true);
                armorStand.setGravity(false);
                armorStand.setInvulnerable(true);
                armorStand.setSilent(true);
                armorStand.setVisible(false);

                armorStands.add(armorStand);
            }
            activeArmorStand.put(game, armorStands);
        }
    }

    public UUID createZombie(String color, int num, World world, Lighthouse lighthouse, Game game) {
        Location location = FileUtil.getLocationSpawnMob(color, num, world);
        EntityZombie zombie = ((CraftZombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE)).getHandle();


        zombie.setNoAI(false);
        zombie.setHealth(5.0f);
        Zombie zombieSpigot = (Zombie) Bukkit.getEntity(zombie.getUniqueID());
        zombieSpigot.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 9999 * 20, 1));
        zombieSpigot.setBaby(false);

        zombieSpigot.getEquipment().clear();
        zombie.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.2);

        zombie.goalSelector.a(0, new AttackPathfinderGoal<>(zombie, lighthouse, color, world, game));
        return zombie.getUniqueID();
    }
}