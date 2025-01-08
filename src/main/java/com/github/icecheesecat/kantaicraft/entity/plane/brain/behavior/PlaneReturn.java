package com.github.icecheesecat.kantaicraft.entity.plane.brain.behavior;

import com.github.icecheesecat.kantaicraft.entity.plane.BasicEntityPlane;
import com.github.icecheesecat.kantaicraft.init.ModBrain;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class PlaneReturn extends PhysicalMovementBehavior {

    Entity owner;

    public PlaneReturn(BasicEntityPlane plane) {
        super(ImmutableMap.of(ModBrain.OWNERSHIP.get(), MemoryStatus.REGISTERED, ModBrain.PLANE_TIMEOUT.get(), MemoryStatus.VALUE_ABSENT),
            plane.getPlaneAttributes().getFlySpeed(),
            plane.getPlaneAttributes().getTurnAcceleration(),
        200);
    }

    @Override
    protected void start(ServerLevel serverLevel, LivingEntity basicEntityPlane, long p_22542_) {
        owner = serverLevel.getEntity(basicEntityPlane.getBrain().getMemory(ModBrain.OWNERSHIP.get()).get());
    }

    @Override
    protected void tick(ServerLevel serverLevel, LivingEntity basicEntityPlane, long gametime) {
        this.setVelocityDirection(owner.position().subtract(basicEntityPlane.position()));
        super.tick(serverLevel, basicEntityPlane, gametime);
    }

    @Override
    protected void stop(ServerLevel serverLevel, LivingEntity livingEntity, long gametime) {
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel, LivingEntity basicEntityPlane, long p_22547_) {
        if (owner.position().distanceTo(basicEntityPlane.position()) < 0.5d) {
            return false;
        }
        return true;
    }

    @Override
    protected boolean timedOut(long p_22537_) {
        return false;
    }
}
