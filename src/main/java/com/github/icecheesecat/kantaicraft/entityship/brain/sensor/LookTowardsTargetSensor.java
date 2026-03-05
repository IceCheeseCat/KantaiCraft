package com.github.icecheesecat.kantaicraft.entityship.brain.sensor;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModAttribute;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.util.AxisRotation;
import com.google.common.collect.ImmutableSet;
import com.mojang.math.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class LookTowardsTargetSensor extends Sensor<EntityShip> {

    private static final float ConeDegree = 10.0f;
    private static final int num_of_ray = 8;

    public LookTowardsTargetSensor() {
        super(1);
    }

    @Override
    protected void doTick(ServerLevel pLevel, EntityShip pEntity) {

        double radius = pEntity.getAttributeValue(ModAttribute.LOS.get());
        var attackTarget = pEntity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);

        if (attackTarget.isPresent()) {
            
            if (attackTarget.get().getBoundingBox().clip(pEntity.getEyePosition(), pEntity.getViewVector(0)).isPresent()) {
                pEntity.getBrain().setMemory(ModMemoryModuleType.ATTACK_TARGET_IN_SIGHT.get(), Unit.INSTANCE);
                return;
            }

            Vec3 viewVector = pEntity.getViewVector(0);
            Vec3 lifted = AxisRotation.getRotatedVectorAroundAnAxis(Axis.of(viewVector.cross(AxisRotation.YP).toVector3f()), viewVector, ConeDegree/2.0f);

            // all ray vector check entity in sight
            boolean flag = false;
            for (int i = 0; i < num_of_ray; i++) {

                float deg = 360.f/num_of_ray * i;
                Vec3 rotated = AxisRotation.getRotatedVectorAroundAnAxis(viewVector, lifted, deg);
                Vec3 extend = rotated.multiply(radius, radius, radius);
                Vec3 eye = pEntity.getEyePosition();

                var hasSee = attackTarget.get().getBoundingBox().clip(eye, eye.add(extend));
                if (hasSee.isPresent()) {
                    pEntity.getBrain().setMemory(ModMemoryModuleType.ATTACK_TARGET_IN_SIGHT.get(), Unit.INSTANCE);
                    flag = true;
                }

            }

            if (!flag) {
                pEntity.getBrain().eraseMemory(ModMemoryModuleType.ATTACK_TARGET_IN_SIGHT.get());
            }
        }

    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of();
    }
}
