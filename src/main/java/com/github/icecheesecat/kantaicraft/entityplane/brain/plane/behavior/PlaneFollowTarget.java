package com.github.icecheesecat.kantaicraft.entityplane.brain.plane.behavior;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class PlaneFollowTarget extends PhysicalMovementBehavior {

    LivingEntity target;
    double startTime;

    public PlaneFollowTarget(double speed, double turnAcceleration) {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), speed, turnAcceleration);
    }

    @Override
    protected void start(ServerLevel serverLevel, LivingEntity livingEntity, long gametime) {
        livingEntity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent(
                e -> target = e
        );
        this.startTime = gametime;
    }

    @Override
    protected void tick(ServerLevel serverLevel, LivingEntity livingEntity, long gametime) {
        Vec3 targetPos = this.target.position();
        Vec3 currPos = livingEntity.position();

        Vec3 towardsTargetDirection = targetPos.subtract(currPos).normalize();
        this.setAccelerationDirection(towardsTargetDirection);
        super.tick(serverLevel, livingEntity,gametime);
    }

    @Override
    protected void stop(ServerLevel serverLevel, LivingEntity livingEntity, long gametime) {

//        basicEntityPlane.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, Optional.empty());

    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel, LivingEntity basicEntityPlane, long gametime) {
        Optional<LivingEntity> optionalLivingEntity = basicEntityPlane.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if (optionalLivingEntity.isPresent()) {
            LivingEntity livingEntity = optionalLivingEntity.get();
            if (livingEntity.isAlive() && livingEntity.level() == basicEntityPlane.level()) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected boolean timedOut(long p_22537_) {
        return false;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, LivingEntity basicEntityPlane) {
        return target.isAlive() && target.level() == basicEntityPlane.level();
    }

}
