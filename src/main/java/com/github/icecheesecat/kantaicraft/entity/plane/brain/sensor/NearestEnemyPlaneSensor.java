package com.github.icecheesecat.kantaicraft.entity.plane.brain.sensor;

import com.github.icecheesecat.kantaicraft.entity.plane.BasicEntityPlane;
import com.github.icecheesecat.kantaicraft.entity.plane.EntityFighterPlane;
import com.github.icecheesecat.kantaicraft.init.ModBrain;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class NearestEnemyPlaneSensor extends Sensor<LivingEntity> {

    private long cooldown = 100;
    private static final double SENSING_RANGE = 100.0d;

    @Override
    protected void doTick(ServerLevel serverLevel, LivingEntity livingEntity) {
        List<BasicEntityPlane> l = serverLevel.getEntitiesOfClass(BasicEntityPlane.class, livingEntity.getBoundingBox().inflate(SENSING_RANGE));

        BasicEntityPlane nearest = null;
        double dis = Double.MAX_VALUE;
        for (int i = 0; i < l.size(); i++) {
            BasicEntityPlane plane = l.get(i);
            if (plane.isEnemy((BasicEntityPlane)livingEntity) && plane.distanceToSqr(livingEntity) < dis) {
                nearest = plane;
            }
        }

        if (nearest != null) {
            livingEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, nearest, 60);
        }
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return Set.of(MemoryModuleType.ATTACK_TARGET);
    }
}
