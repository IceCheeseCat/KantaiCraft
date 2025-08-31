package com.github.icecheesecat.kantaicraft.entity.brain.ship;

import com.github.icecheesecat.kantaicraft.entity.brain.Util;
import com.github.icecheesecat.kantaicraft.entity.ship.CannonShip;
import com.github.icecheesecat.kantaicraft.registries.ModActitvity;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.registries.ModSensor;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.*;

public class CannonShipBrain {

    private static final List<SensorType<? extends Sensor<? super CannonShip>>> SENSOR_TYPES;
    private static final List<MemoryModuleType<?>> MEMORY_TYPES;

    static {
        SENSOR_TYPES = List.of(
                SensorType.NEAREST_ITEMS,
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS,
                SensorType.IS_IN_WATER,
                ModSensor.SHIP_RESOURCES_SENSOR.get(),
                ModSensor.SHIP_EQUIPMENT_SENSOR.get(),
                ModSensor.PLAYER_SHIP_TARGETING_SENSOR.get());
        MEMORY_TYPES = List.of(
                ModMemoryModuleType.OWNERSHIP.get(),
                ModMemoryModuleType.OUT_OF_FUEL.get(),
                ModMemoryModuleType.OUT_OF_AMMO.get(),
                ModMemoryModuleType.OUT_OF_AIRCRAFT.get(),
                ModMemoryModuleType.IS_GUARDING.get(),
                ModMemoryModuleType.NEARBY_TARGETS.get(),
                ModMemoryModuleType.ACTION_HANDLER.get(),
                MemoryModuleType.NEAREST_LIVING_ENTITIES,
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
                MemoryModuleType.NEAREST_VISIBLE_PLAYER,
                MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
                MemoryModuleType.LOOK_TARGET,
                MemoryModuleType.WALK_TARGET,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
                MemoryModuleType.PATH,
                MemoryModuleType.ATTACK_TARGET,
                ModMemoryModuleType.KILLED_ENTITY_DROPS.get());
    }

    public static Brain<CannonShip> makeBrain(CannonShip cannonShip, Dynamic<?> dyn) {

        Brain.Provider<CannonShip> brainProvider = Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
        Brain<CannonShip> brain = brainProvider.makeBrain(dyn);
        Util.initCoreActivity(brain);
        Util.initBurnOutActivity(cannonShip, brain);
        Util.initFightActivity(cannonShip, brain);
        Util.PlayerShip.initIdleActivity(cannonShip, brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();

        return brain;
    }

}
