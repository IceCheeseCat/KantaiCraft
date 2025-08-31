package com.github.icecheesecat.kantaicraft.entity.ship.destroyer.hostile;

import com.github.icecheesecat.kantaicraft.entity.ship.destroyer.DestroyerRoClass;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class HostileDestroyerRoClass extends DestroyerRoClass {
    public HostileDestroyerRoClass(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isHostileShip() {
        return true;
    }
}
