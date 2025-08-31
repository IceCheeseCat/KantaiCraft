package com.github.icecheesecat.kantaicraft.entity.brain.plane.behavior;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public abstract class PhysicalMovementBehavior extends Behavior<LivingEntity> {
    private Vec3 velocity;
    private Vec3 acceleration;
    private double speed;
    private double turnAcceleration;

    public PhysicalMovementBehavior(Map<MemoryModuleType<?>, MemoryStatus> p_22528_, double speed, double turnAcceleration) {
        super(p_22528_);
        this.speed = speed;
        this.turnAcceleration = turnAcceleration;
    }

    public PhysicalMovementBehavior(Map<MemoryModuleType<?>, MemoryStatus> p_22528_, double speed, double turnAcceleration, int duration) {
        super(p_22528_, duration);
        this.speed = speed;
        this.turnAcceleration = turnAcceleration;
    }

    public void setAccelerationDirection(Vec3 accelerationDirection) {
        Vec3 A = this.velocity.normalize();
        Vec3 B = accelerationDirection.normalize();

        this.acceleration = this.acceleration.subtract(A.scale(A.dot(B)/A.dot(A))).scale(this.turnAcceleration);
    }

    public void setVelocityDirection(Vec3 velocityDirection) {
        this.velocity = velocityDirection.normalize().scale(this.velocity.length());
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeedInTick() {
        return this.speed / 20.0d;
    }

    @Override
    protected void tick(ServerLevel serverLevel, LivingEntity livingEntity, long gametime) {
        this.velocity = this.velocity.add(this.acceleration).normalize().scale(this.speed/20.0d);
        livingEntity.setDeltaMovement(this.velocity);
    }

    @Override
    protected void stop(ServerLevel serverLevel, LivingEntity livingEntity, long gametime) {
        this.velocity = Vec3.ZERO;
    }
}
