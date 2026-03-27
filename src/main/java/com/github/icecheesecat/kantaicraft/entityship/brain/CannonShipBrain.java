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

public class CannonShipBrain {

    public static final List<MemoryModuleType<?>> MEMORY_TYPES;
    public static final List<SensorType<? extends Sensor<? super CannonEntityShip>>> SENSOR_TYPES;

    static {
        SENSOR_TYPES = List.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS,
                SensorType.IS_IN_WATER,
                ModSensor.SHIP_RESOURCES_SENSOR.get(),
                ModSensor.PLAYER_SHIP_TARGETING_SENSOR.get(),
                ModSensor.ATTACK_TARGET_VISIBILITY_SENSOR.get(),
                ModSensor.MOB_DROPS_SENSOR.get(),
                ModSensor.LOOK_TOWARDS_TARGET_SENSOR.get());
        MEMORY_TYPES = List.of(
                ModMemoryModuleType.IS_PLAYER_SHIP.get(),
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
                ModMemoryModuleType.ITEMS_TO_PICK_UP.get(),
                ModMemoryModuleType.CANT_SEE_TARGET_SINCE.get(),
                ModMemoryModuleType.LAST_SAW_TARGET_POS.get(),
                MemoryModuleType.GAZE_COOLDOWN_TICKS,
                ModMemoryModuleType.ATTACK_TARGET_IN_SIGHT.get(),
                ModMemoryModuleType.PICK_UP_COOLDOWN.get(),
                ModMemoryModuleType.NEAREST_WANTED_ITEM.get(),
                ModMemoryModuleType.IS_SITTING.get(),
                ModMemoryModuleType.SIT_BACK_DOWN_COUNTDOWN.get());
    }

    public static Brain<CannonEntityShip> makeBrain(CannonEntityShip cannonShip, Dynamic<?> dyn) {

        Brain.Provider<CannonEntityShip> brainProvider = Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
        Brain<CannonEntityShip> brain = brainProvider.makeBrain(dyn);
        BrainActivities.initCoreActivity(brain);
        BrainActivities.initBurnOutActivity(cannonShip, brain);
        BrainActivities.initFightActivity(cannonShip, brain);
        BrainActivities.PlayerShip.initIdleActivity(cannonShip, brain);
        BrainActivities.PlayerShip.initSittingActivity(cannonShip, brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();

        return brain;
    }

}
