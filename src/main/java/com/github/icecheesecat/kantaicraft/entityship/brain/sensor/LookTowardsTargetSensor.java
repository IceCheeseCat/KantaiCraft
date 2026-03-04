package com.github.icecheesecat.kantaicraft.entityship.brain.sensor;

import com.google.common.collect.ImmutableSet;
import com.mojang.math.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Set;

public class LookTowardsTargetSensor extends Sensor<LivingEntity> {

    public LookTowardsTargetSensor() {
        super(1);
    }

    @Override
    protected void doTick(ServerLevel pLevel, LivingEntity pEntity) {



    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.LOOK_TARGET);
    }
}
