package com.github.icecheesecat.kantaicraft.brain.sensor;

import com.github.icecheesecat.kantaicraft.registries.ModAttribute;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class NearbyEntityOfClassSensor<T extends LivingEntity> extends Sensor<BasicEntityShip> {

    final Class<T> c;
    final MemoryModuleType<List<LivingEntity>> MEMORY_MODULE_TYPE;

    public NearbyEntityOfClassSensor(Class<T> c, MemoryModuleType<List<LivingEntity>> memoryModuleType) {
        super();
        this.c = c;
        this.MEMORY_MODULE_TYPE = memoryModuleType;
    }

    @Override
    protected void doTick(ServerLevel serverLevel, BasicEntityShip basicEntityShip) {
        List<T> list = this.findTargets(basicEntityShip);
        basicEntityShip.getBrain().setMemory(this.MEMORY_MODULE_TYPE, (List<LivingEntity>) list);
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return Set.of(this.MEMORY_MODULE_TYPE);
    }

    protected List<T> findTargets(BasicEntityShip entity) {

        Stream<T> livings = entity.level().getEntitiesOfClass(c, entity.getBoundingBox().inflate(entity.getAttributeValue(ModAttribute.LOS.get()))).stream();

        return livings.sorted(Comparator.comparingDouble(entity::distanceToSqr)).toList();
    }

}
