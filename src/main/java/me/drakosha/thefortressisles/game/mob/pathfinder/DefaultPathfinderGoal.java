package me.drakosha.thefortressisles.game.mob.pathfinder;

import me.drakosha.thefortressisles.game.player.lighthouse.Lighthouse;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import org.bukkit.Location;

public abstract class DefaultPathfinderGoal extends PathfinderGoal {
    EntityInsentient entity;
    Lighthouse lighthouse;
    int tick;

    public DefaultPathfinderGoal(EntityInsentient entity, Lighthouse lighthouse) {
        entity.targetSelector.a();
        this.entity = entity;
        this.lighthouse = lighthouse;
        this.tick = 0;
    }

    protected Location getNearest(Location locationLighthouse) {
        if (entity.getBukkitEntity().getLocation().distanceSquared(locationLighthouse) < 256) return locationLighthouse;
        else return getNearest(getMidpoint(entity.getBukkitEntity().getLocation(), locationLighthouse));
    }

    public static Location getMidpoint(Location loc1, Location loc2) {
        if (!loc1.getWorld().equals(loc2.getWorld())) {
            throw new IllegalArgumentException();
        }

        double midX = (loc1.getX() + loc2.getX()) / 2.0;
        double midY = (loc1.getY() + loc2.getY()) / 2.0;
        double midZ = (loc1.getZ() + loc2.getZ()) / 2.0;

        return new Location(loc1.getWorld(), midX, midY, midZ);
    }
}
