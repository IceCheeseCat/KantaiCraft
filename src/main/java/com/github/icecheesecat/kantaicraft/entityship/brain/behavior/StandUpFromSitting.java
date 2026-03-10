package com.github.icecheesecat.kantaicraft.entityship.brain.behavior;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.OneShot;

public abstract class StandUpFromSitting extends OneShot<EntityShip> {
    @Override
    public boolean trigger(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        if (!pEntity.isSitDown()) return false;
        // do stand up
        pEntity.toggleSitDown();
        return true;
    }

    public static class WhenAttacked extends StandUpFromSitting {
        @Override
        public boolean trigger(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
            LivingEntity target = pEntity.getTarget();
            if (target == null) {
                return false;
            }
            else {
                return (!target.isRemoved() || target.isAlive()) && super.trigger(pLevel, pEntity, pGameTime);
            }
        }
    }
}
