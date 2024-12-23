package game.mob.pathFinder;

import game.mob.Lighthouse;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import org.bukkit.Location;

public class LighthouseAttackPathfinderGoal<T extends EntityInsentient>
        extends DefaultPathfinderGoal {
    String color;

    public LighthouseAttackPathfinderGoal(T entity, Lighthouse goal, String color) {
        super(entity, goal);
        this.color = color;
        entity.goalSelector.a();
        entity.goalSelector.a(0, this);

    }
    @Override
    public boolean a() {
        if(goal.getHealth(color) <= 0) {
            this.entity.die();
            return false;
        }
        return entity.isAlive();
    }

    @Override
    public void e() {
        if(this.tick++ % 10 == 0) {
            Location nearPos = getNearest(goal.getLocation(color));
            entity.getNavigation().a(nearPos.getX(), nearPos.getY(), nearPos.getZ(), 1.0f);
        }
        if(this.tick % 50 == 0) {
            Location gl = goal.getLocation(color);
            if( entity.getBukkitEntity().getLocation().distanceSquared(gl) <= 9) {
            }
        }
    }
}


