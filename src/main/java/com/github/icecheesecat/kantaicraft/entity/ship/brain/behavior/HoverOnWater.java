package com.github.icecheesecat.kantaicraft.entity.ship.brain.behavior;

import com.github.icecheesecat.kantaicraft.entity.plane.BasicEntityPlane;
import com.github.icecheesecat.kantaicraft.entity.plane.brain.behavior.PhysicalMovementBehavior;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;

public class HoverOnWater extends Behavior<LivingEntity> {
    public HoverOnWater() {
        super(ImmutableMap.of());
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel, LivingEntity livingEntity, long gametime) {
//        return livingEntity.level().getFluidState(livingEntity.getOnPos()).is(FluidTags.WATER);
        return livingEntity.isInWater();
    }

    @Override
    protected void tick(ServerLevel serverLevel, LivingEntity livingEntity, long gametime) {


        super.tick(serverLevel, livingEntity, gametime);
    }


}
