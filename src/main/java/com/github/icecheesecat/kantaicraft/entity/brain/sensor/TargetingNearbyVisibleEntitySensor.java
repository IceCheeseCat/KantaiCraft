package com.github.icecheesecat.kantaicraft.entity.brain.sensor;

import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModAttribute;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.phys.AABB;

import java.util.*;
import java.util.function.Predicate;

public class TargetingNearbyVisibleEntitySensor extends Sensor<EntityShip> {

//    final Class<?> c;
    final Predicate<Entity> entityPredicate;

    public TargetingNearbyVisibleEntitySensor(Predicate<Entity> entityPredicate) {
        super();
        this.entityPredicate = entityPredicate;
    }

    /**
     * Add new entities to the target set
     */
    @Override
    protected void doTick(ServerLevel serverLevel, EntityShip entityShip) {
        entityShip.getBrain().setMemory(ModMemoryModuleType.NEARBY_TARGETS.get(), this.findVisibleTargets(entityShip));
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(ModMemoryModuleType.NEARBY_TARGETS.get());
    }

    protected List<LivingEntity> findVisibleTargets(EntityShip entity) {
        double sensorRange = entity.getAttributeValue(ModAttribute.LOS.get());
        AABB area = AABB.ofSize(entity.getEyePosition(), sensorRange, sensorRange, sensorRange);
        List<LivingEntity> livings = entity.level().getEntitiesOfClass(LivingEntity.class, area, this.entityPredicate.and(entity::hasLineOfSight));

        return livings;
    }

}
