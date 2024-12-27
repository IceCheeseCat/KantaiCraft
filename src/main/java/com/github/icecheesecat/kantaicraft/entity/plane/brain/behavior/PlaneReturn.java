package com.github.icecheesecat.kantaicraft.entity.plane.brain.behavior;

import com.github.icecheesecat.kantaicraft.entity.plane.BasicEntityPlane;
import com.github.icecheesecat.kantaicraft.init.ModBrain;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;

public class PlaneReturn extends Behavior<BasicEntityPlane> {

    boolean isReturnToOwner;
    Vec3 velocity;
    double flySpeedInTick;
    Entity owner;

    public PlaneReturn() {
        super(ImmutableMap.of(ModBrain.PLANE_TIMEOUT.get(), MemoryStatus.VALUE_ABSENT));
    }

    @Override
    protected void start(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane, long p_22542_) {
        flySpeedInTick = basicEntityPlane.getFlySpeed() / 20.0d;

        if (basicEntityPlane.getBrain().hasMemoryValue(ModBrain.OWNERSHIP.get())) {
            this.isReturnToOwner = true;
            owner = serverLevel.getEntity(basicEntityPlane.getBrain().getMemory(ModBrain.OWNERSHIP.get()).get());
            this.velocity = owner.position().subtract(basicEntityPlane.position()).normalize().scale(flySpeedInTick);
        }
        else {
            this.isReturnToOwner = false;
        }
    }

    @Override
    protected void tick(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane, long gametime) {
        if (isReturnToOwner) {
            velocity = owner.position().subtract(basicEntityPlane.position()).normalize().scale(flySpeedInTick);
        }

        basicEntityPlane.setDeltaMovement(velocity);
    }

    @Override
    protected void stop(ServerLevel p_22548_, BasicEntityPlane p_22549_, long p_22550_) {
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane, long p_22547_) {
        if (owner.position().distanceTo(basicEntityPlane.position()) < 0.5d) {
            return false;
        }
        return true;
    }

    @Override
    protected boolean timedOut(long p_22537_) {
        return false;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel p_22538_, BasicEntityPlane p_22539_) {
        return true;
    }
}
