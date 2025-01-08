package com.github.icecheesecat.kantaicraft.entity.ship.brain;

import com.github.icecheesecat.kantaicraft.entity.plane.BasicEntityPlane;
import com.github.icecheesecat.kantaicraft.entity.ship.destroyer.BasicDestroyerShip;
import com.github.icecheesecat.kantaicraft.init.ModBrain;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;

public class DestroyerBrain {

    private static final List<SensorType<? extends Sensor<? super BasicDestroyerShip>>> SENSOR_TYPES;
    private static final List<MemoryModuleType<?>> MEMORY_TYPES;

    static {
        SENSOR_TYPES = List.of(
                SensorType.NEAREST_ITEMS,
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS);
        MEMORY_TYPES = List.of(
                ModBrain.OWNERSHIP.get(),
                MemoryModuleType.NEAREST_LIVING_ENTITIES,
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
                MemoryModuleType.NEAREST_VISIBLE_PLAYER,
                MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
                MemoryModuleType.LOOK_TARGET,
                MemoryModuleType.WALK_TARGET,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
                MemoryModuleType.PATH,
                MemoryModuleType.ATTACK_TARGET,
                MemoryModuleType.ATTACK_COOLING_DOWN);
    }

    public static Brain<BasicDestroyerShip> makeBrain(BasicDestroyerShip destroyerShip, Dynamic<?> dyn) {

        Brain.Provider<BasicDestroyerShip> brainProvider = Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
        Brain<BasicDestroyerShip> brain = brainProvider.makeBrain(dyn);
        initCoreActivity(destroyerShip, brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.CORE);
        brain.useDefaultActivity();

        return brain;
    }

    private static void initCoreActivity(BasicDestroyerShip destroyerShip, Brain<BasicDestroyerShip> brain) {
        brain.addActivity(Activity.CORE, 0,
                ImmutableList.of(

                ));
    }

}
