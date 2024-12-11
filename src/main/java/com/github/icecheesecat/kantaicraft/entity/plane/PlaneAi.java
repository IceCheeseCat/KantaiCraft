package com.github.icecheesecat.kantaicraft.entity.plane;

import com.github.icecheesecat.kantaicraft.init.ModBrainActivity;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.Unit;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.RandomLookAround;
import net.minecraft.world.entity.ai.behavior.SleepInBed;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.behavior.warden.SetWardenLookTarget;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;

public class PlaneAi {

    private static final List<SensorType<? extends Sensor<? super BasicEntityPlane>>> SENSOR_TYPES;
    private static final List<MemoryModuleType<?>> MEMORY_TYPES;

    public PlaneAi() {
    }

    protected static Brain<?> makeBrain(BasicEntityPlane plane, Dynamic<?> dyn) {

        Brain.Provider<BasicEntityPlane> brainProvider = Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
        Brain<BasicEntityPlane> brain = brainProvider.makeBrain(dyn);
        brain.addActivity(ModBrainActivity.CIRCLE.get(), 0, ImmutableList.of(.));
        brain.addActivityWithConditions(ModBrainActivity.SWOOP.get(), 1, ImmutableList.of(.));

    }

    private static void initCircleActivity(Brain<BasicEntityPlane> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(new DoNothing(10, 100), new RandomLookAround(ConstantInt.of(1), 0.1f, 0.1f, 0.1f)));
    }

    static {
        SENSOR_TYPES = List.of(SensorType.DUMMY);
        MEMORY_TYPES = List.of(MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.ROAR_TARGET, MemoryModuleType.DISTURBANCE_LOCATION, MemoryModuleType.RECENT_PROJECTILE, MemoryModuleType.IS_SNIFFING, MemoryModuleType.IS_EMERGING, MemoryModuleType.ROAR_SOUND_DELAY, MemoryModuleType.DIG_COOLDOWN, MemoryModuleType.ROAR_SOUND_COOLDOWN, MemoryModuleType.SNIFF_COOLDOWN, MemoryModuleType.TOUCH_COOLDOWN, MemoryModuleType.VIBRATION_COOLDOWN, MemoryModuleType.SONIC_BOOM_COOLDOWN, MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN, MemoryModuleType.SONIC_BOOM_SOUND_DELAY);
//        DIG_COOLDOWN_SETTER = BehaviorBuilder.create((p_258953_) -> p_258953_.group(p_258953_.registered(MemoryModuleType.DIG_COOLDOWN)).apply(p_258953_, (p_258960_) -> (p_258956_, p_258957_, p_258958_) -> {
//            if (p_258953_.tryGet(p_258960_).isPresent()) {
//                p_258960_.setWithExpiry(Unit.INSTANCE, 1200L);
//            }
//
//            return true;
//        }));
    }

}
