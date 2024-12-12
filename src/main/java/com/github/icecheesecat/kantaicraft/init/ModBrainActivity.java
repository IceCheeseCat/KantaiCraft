package com.github.icecheesecat.kantaicraft.init;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.mojang.serialization.Codec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.checkerframework.checker.units.qual.A;

import java.util.Optional;

public class ModBrainActivity {

    public static final DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(ForgeRegistries.ACTIVITIES, KantaiCraft.MODID);
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, KantaiCraft.MODID);

    public static final RegistryObject<Activity> CIRCLE = ACTIVITIES.register("activity.circle", () -> new Activity("circle"));
    public static final RegistryObject<Activity> STRIKE = ACTIVITIES.register("activity.strike", () -> new Activity("strike"));
    public static final RegistryObject<Activity> RETURN = ACTIVITIES.register("activity.return", () -> new Activity("return"));

    public static final RegistryObject<MemoryModuleType<Boolean>> PLANE_TIMEOUT = MEMORY_MODULE_TYPES.register("memory_module_type.plane_timeout", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<LivingEntity>> OWNERSHIP = MEMORY_MODULE_TYPES.register("memory_module_type.ownership", () -> new MemoryModuleType<>(Optional.empty()));

}
