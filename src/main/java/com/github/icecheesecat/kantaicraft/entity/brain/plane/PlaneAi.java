package com.github.icecheesecat.kantaicraft.entity.brain.plane;

import com.github.icecheesecat.kantaicraft.registries.ModActitvity;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.entity.plane.BasicEntityPlane;
import com.github.icecheesecat.kantaicraft.entity.brain.plane.behavior.PlaneGlide;
import com.github.icecheesecat.kantaicraft.entity.brain.plane.behavior.PlaneReturn;
import com.github.icecheesecat.kantaicraft.registries.ModSensor;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;

public class PlaneAi {

    private static final List<SensorType<? extends Sensor<? super BasicEntityPlane>>> SENSOR_TYPES;
    private static final List<MemoryModuleType<?>> MEMORY_TYPES;

    static {
        SENSOR_TYPES = List.of(SensorType.NEAREST_ITEMS, ModSensor.NEAREST_ENEMY_PLANE_SENSOR.get());
        MEMORY_TYPES = List.of(ModMemoryModuleType.OWNERSHIP.get(), ModMemoryModuleType.PLANE_TIMEOUT.get(), MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN);
    }

    public static Brain<BasicEntityPlane> makeBrain(BasicEntityPlane plane, Dynamic<?> dyn) {

        Brain.Provider<BasicEntityPlane> brainProvider = Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
        Brain<BasicEntityPlane> brain = brainProvider.makeBrain(dyn);
        initCoreActivity(plane, brain);
        initReturnActivity(plane, brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.CORE);
        brain.useDefaultActivity();

        return brain;
    }

    private static void initCoreActivity(BasicEntityPlane plane, Brain<BasicEntityPlane> brain) {
        brain.addActivity(Activity.CORE, 0,
                ImmutableList.of(
                        new PlaneGlide(plane.getPlaneAttributes().getFlySpeed(), plane.getPlaneAttributes().getTurnAcceleration())
                        ));
    }

    private static void initReturnActivity(BasicEntityPlane plane, Brain<BasicEntityPlane> brain) {
        brain.addActivityWithConditions(ModActitvity.RETURN.get(),
                ImmutableList.of(Pair.of(0, new PlaneReturn(plane))),
                ImmutableSet.of(Pair.of(ModMemoryModuleType.PLANE_TIMEOUT.get(), MemoryStatus.VALUE_ABSENT)));
    }

}
