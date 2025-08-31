package com.github.icecheesecat.kantaicraft.entity.brain.plane.behavior;

import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;


public class PlaneGlide extends PhysicalMovementBehavior {

    private GlideArea glideArea;
    private static final double THRESHOLD = 3.0d;
    private int turnTimer = 0;
    private final int TURNTIMEOUT = 100;

    public PlaneGlide(double speed, double turnAcceleration) {
        super(ImmutableMap.of(ModMemoryModuleType.OWNERSHIP.get(), MemoryStatus.VALUE_PRESENT), speed, turnAcceleration);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, LivingEntity livingEntity) {
        var mem = livingEntity.getBrain().getMemory(ModMemoryModuleType.OWNERSHIP.get());
        if (mem.isPresent() && ((ServerLevel) livingEntity.level()).getEntity(mem.get()) != null) {
            return true;
        }

        return false;
    }

    @Override
    protected void start(ServerLevel serverLevel, LivingEntity livingEntity, long gametime) {
        Entity owner = serverLevel.getEntity(livingEntity.getBrain().getMemory(ModMemoryModuleType.OWNERSHIP.get()).get());
        this.setVelocityDirection(owner.getLookAngle());
        this.setAccelerationDirection(Vec3.ZERO);
        livingEntity.getBrain().getMemory(ModMemoryModuleType.OWNERSHIP.get()).ifPresentOrElse(
                m -> this.glideArea = new GlideArea(serverLevel.getEntity(m).position(), 20.0d),
                () -> this.glideArea = new GlideArea(Vec3.ZERO, 0.0d)
        );
        this.turnTimer = 0;
    }

    @Override
    protected void tick(ServerLevel serverLevel, LivingEntity livingEntity, long gametime) {
        Vec3 currPosition = livingEntity.position();
        double r = currPosition.distanceTo(glideArea.getCenter());

        // Turn when near bound
        if (r > glideArea.getDiameter() - THRESHOLD) {
            double coeff = 0.1d;
//            F = Gmm/r/r;
            Vec3 towards_center = glideArea.getCenter().subtract(currPosition).normalize();
            double dis = r - glideArea.getDiameter();
//            this.acceleration = towards_center.scale(coeff / dis / dis);
            this.setAccelerationDirection(towards_center);
        }
        else {
            // random turns
            if (this.turnTimer <= 0) {
                turnTimer = TURNTIMEOUT;

                double rand_0 = livingEntity.getRandom().nextDouble() * 2.0d - 1.0d;
//                double rand_1 = basicEntityPlane.getRandom().nextDouble() * 2.0d - 1.0d;
                double rand_2 = livingEntity.getRandom().nextDouble() * 2.0d - 1.0d;

                this.setAccelerationDirection(new Vec3(rand_0, 0.0d, rand_2).normalize());
            }
        }
        turnTimer--;

        super.tick(serverLevel, livingEntity, gametime);
    }

    @Override
    protected void stop(ServerLevel serverLevel, LivingEntity livingEntity, long gametime) {
        super.stop(serverLevel, livingEntity, gametime);
        this.turnTimer = 0;
        this.glideArea = new GlideArea(Vec3.ZERO, 0);
    }

    @Override
    protected boolean canStillUse(ServerLevel p_22545_, LivingEntity p_22546_, long p_22547_) {
        return true;
    }

    @Override
    protected boolean timedOut(long p_22537_) {
        return false;
    }
}
