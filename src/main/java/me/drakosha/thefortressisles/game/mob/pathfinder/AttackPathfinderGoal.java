package me.drakosha.thefortressisles.game.mob.pathfinder;

import me.drakosha.thefortressisles.game.Game;
import me.drakosha.thefortressisles.game.player.lighthouse.Lighthouse;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class AttackPathfinderGoal<T extends EntityInsentient>
        extends DefaultPathfinderGoal {
    String color;
    World world;
    EntityHuman targetPlayer = null;
    Game game;

    public AttackPathfinderGoal(T entity, Lighthouse lighthouse, String color, World world, Game game) {
        super(entity, lighthouse);
        this.color = color;
        this.game = game;
        this.world = world;
        entity.goalSelector.a();
        entity.goalSelector.a(0, this);

    }

    @Override
    public boolean a() {
        if (lighthouse.getHealth(color) <= 0) {
            game.getWaveMob().getAliveZombie().get(color).remove(entity.getUniqueID());
            this.entity.die();
            return false;
        }
        return entity.isAlive();
    }

    @Override
    public void e() {
        findPlayer();

        if (this.targetPlayer != null) {
            entity.getNavigation().a(targetPlayer.locX, targetPlayer.locY, targetPlayer.locZ, 1.2f);
        } else {
            if (this.tick++ % 10 == 0) {
                Location nearPos = getNearest(lighthouse.getLocation(color, world));
                entity.getNavigation().a(nearPos.getX(), nearPos.getY(), nearPos.getZ(), 1.0f);
            }
            if (this.tick % 50 == 0) {
                Location gl = lighthouse.getLocation(color, world);
                if (!world.getNearbyEntities(gl, 5, 5, 5).isEmpty()) {
                    lighthouse.reduceHealth(color, 2);
                }
            }
        }
    }

    private void findPlayer() {
        List<Player> players = world.getPlayers().stream()
                .filter(player -> player.getLocation().distanceSquared(entity.getBukkitEntity().getLocation()) <= 5 * 5)
                .collect(Collectors.toList());

        if (!players.isEmpty()) this.targetPlayer = ((CraftPlayer) players.get(0)).getHandle();
        else this.targetPlayer = null;

    }
}
