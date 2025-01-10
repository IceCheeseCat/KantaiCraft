package com.github.icecheesecat.kantaicraft.brain.sensor;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.List;

public class NearbyDifferentFactionShipSensor extends NearbyEntityOfClassSensor<BasicEntityShip> {

    public NearbyDifferentFactionShipSensor(MemoryModuleType<List<LivingEntity>> memoryModuleType) {
        super(BasicEntityShip.class, memoryModuleType);
    }

    @Override
    protected List<BasicEntityShip> findTargets(BasicEntityShip entity) {
        List<BasicEntityShip> list = super.findTargets(entity);
        return list.stream().filter(entity::isEnemy).toList();
    }
}
