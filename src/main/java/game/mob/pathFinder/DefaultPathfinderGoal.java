package game.mob.pathFinder;

import game.mob.Lighthouse;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import org.bukkit.Location;


public abstract class DefaultPathfinderGoal extends PathfinderGoal {

    EntityInsentient entity;
    Lighthouse goal;
    int tick;

    public DefaultPathfinderGoal(EntityInsentient entity, Lighthouse goal) {
        entity.targetSelector.a();
        this.entity = entity;
        this.goal = goal;
        this.tick = 0;
    }

    protected Location getNearest(Location goal) {
        if(entity.getBukkitEntity().getLocation().distanceSquared(goal) < 256) return goal;
        else return getNearest(getMidpoint(entity.getBukkitEntity().getLocation(), goal));
    }
    public static Location getMidpoint(Location loc1, Location loc2) {
        if (!loc1.getWorld().equals(loc2.getWorld())) {
            throw new IllegalArgumentException("Locations must be in the same world!");
        }

        double midX = (loc1.getX() + loc2.getX()) / 2.0;
        double midY = (loc1.getY() + loc2.getY()) / 2.0;
        double midZ = (loc1.getZ() + loc2.getZ()) / 2.0;

        return new Location(loc1.getWorld(), midX, midY, midZ);
    }

}
