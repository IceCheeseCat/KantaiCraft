package com.github.icecheesecat.kantaicraft.entityship.brain;

import com.github.icecheesecat.kantaicraft.entityship.entity.cannoship.CannonEntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.registries.ModSensor;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;

public class HostileCannonShipBrain {

    private static final List<SensorType<? extends Sensor<? super CannonEntityShip>>> SENSOR_TYPES;
    private static final List<MemoryModuleType<?>> MEMORY_TYPES;

    static {
        SENSOR_TYPES = List.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS,
                SensorType.IS_IN_WATER,
                ModSensor.SHIP_RESOURCES_SENSOR.get(),
                ModSensor.SHIP_EQUIPMENT_SENSOR.get(),
                ModSensor.HOSTILE_SHIP_TARGETING_SENSOR.get(),
                ModSensor.ATTACK_TARGET_VISIBILITY_SENSOR.get());
        MEMORY_TYPES = List.of(
                ModMemoryModuleType.IS_HOSTILE_SHIP.get(),
                ModMemoryModuleType.OUT_OF_FUEL.get(),
                ModMemoryModuleType.OUT_OF_AMMO.get(),
                ModMemoryModuleType.OUT_OF_AIRCRAFT.get(),
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
                ModMemoryModuleType.CANT_SEE_TARGET_SINCE.get(),
                ModMemoryModuleType.LAST_SAW_TARGET_POS.get(),
                MemoryModuleType.GAZE_COOLDOWN_TICKS);
    }

    public static Brain<CannonEntityShip> makeBrain(CannonEntityShip cannonShip, Dynamic<?> dyn) {

        Brain.Provider<CannonEntityShip> brainProvider = Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
        Brain<CannonEntityShip> brain = brainProvider.makeBrain(dyn);
        BrainActivities.initCoreActivity(brain);
        BrainActivities.initBurnOutActivity(cannonShip, brain);
        BrainActivities.initFightActivity(cannonShip, brain);
        BrainActivities.HostileShip.initIdleActivity(cannonShip, brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.CORE);
        brain.useDefaultActivity();

        return brain;
    }
    


}
