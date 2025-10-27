package com.github.icecheesecat.kantaicraft.entityship.brain.sensor;

import com.github.icecheesecat.kantaicraft.entityplane.BasicEntityPlane;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.*;

public class NearestEnemyPlaneSensor extends Sensor<LivingEntity> {

    private static final double SENSING_RANGE = 100.0d;

    /*
        Find nearest plane to set memory of attack target if attack target is not valid
     */
    @Override
    protected void doTick(ServerLevel serverLevel, LivingEntity livingEntity) {

        if (targetIsValid(livingEntity)) return;

        List<BasicEntityPlane> l = serverLevel.getEntitiesOfClass(BasicEntityPlane.class, livingEntity.getBoundingBox().inflate(SENSING_RANGE));

        BasicEntityPlane nearest = null;
        double dis = Double.MAX_VALUE;
        for (int i = 0; i < l.size(); i++) {
            BasicEntityPlane plane = l.get(i);
//            if (plane.isEnemy((BasicEntityPlane)livingEntity) && plane.distanceToSqr(livingEntity) < dis) {
//                nearest = plane;
//            }
        }

        if (nearest != null) {
            livingEntity.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, nearest);
        }
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return Set.of(MemoryModuleType.ATTACK_TARGET);
    }

    private static boolean targetIsValid(LivingEntity livingEntity) {
        if (livingEntity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
            LivingEntity target = livingEntity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
            return target.isAlive();
        }

        return false;
    }
}
