package com.github.icecheesecat.kantaicraft.init;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entity.plane.brain.sensor.NearestEnemyPlaneSensor;
import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;
import java.util.UUID;

public class ModBrain {

    public static final DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(ForgeRegistries.ACTIVITIES, KantaiCraft.MODID);
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, KantaiCraft.MODID);
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, KantaiCraft.MODID);



    public static final RegistryObject<Activity> CIRCLE = ACTIVITIES.register("activity.circle", () -> new Activity("circle"));
    public static final RegistryObject<Activity> STRIKE = ACTIVITIES.register("activity.strike", () -> new Activity("strike"));
    public static final RegistryObject<Activity> RETURN = ACTIVITIES.register("activity.return", () -> new Activity("return"));
    public static final RegistryObject<Activity> MISSION = ACTIVITIES.register("activity.mission", () -> new Activity("mission"));



    public static final RegistryObject<MemoryModuleType<Unit>> PLANE_TIMEOUT = MEMORY_MODULE_TYPES.register("memory_module_type.plane_timeout", () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<Unit>> STRIKE_COOLDOWN = MEMORY_MODULE_TYPES.register("memory_module_type.strike_cooldown", () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<UUID>> OWNERSHIP = MEMORY_MODULE_TYPES.register("memory_module_type.ownership", () -> new MemoryModuleType<>(Optional.of(UUIDUtil.CODEC)));
    public static final RegistryObject<MemoryModuleType<UUID>> ATTACK_TARGET = MEMORY_MODULE_TYPES.register("memory_module_type.attack_target", () -> new MemoryModuleType<>(Optional.of(UUIDUtil.CODEC)));



    public static final RegistryObject<SensorType<NearestEnemyPlaneSensor>> NEAREST_ENEMY_PLANE_SENSOR = SENSOR_TYPES.register("sensor_type.nearest_enemy_plane", () -> new SensorType<>(NearestEnemyPlaneSensor::new));

}
