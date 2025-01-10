package com.github.icecheesecat.kantaicraft.customObjects;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.util.tickable.EquipmentActionHandler;
import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ModMemoryModuleType {

    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, KantaiCraft.MODID);

    public static final RegistryObject<MemoryModuleType<Unit>> PLANE_TIMEOUT = MEMORY_MODULE_TYPES.register("memory_module_type.plane_timeout", () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<Unit>> STRIKE_COOLDOWN = MEMORY_MODULE_TYPES.register("memory_module_type.strike_cooldown", () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<UUID>> OWNERSHIP = MEMORY_MODULE_TYPES.register("memory_module_type.ownership", () -> new MemoryModuleType<>(Optional.of(UUIDUtil.CODEC)));
    public static final RegistryObject<MemoryModuleType<Unit>> OUT_OF_FUEL = MEMORY_MODULE_TYPES.register("memory_module_type.out_of_fuel", () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<Unit>> IS_GUARDING = MEMORY_MODULE_TYPES.register("memory_module_type.is_guarding", () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<List<LivingEntity>>> NEARBY_DIFFERENT_FACTION_SHIPS = MEMORY_MODULE_TYPES.register("memory_module_type.nearby_different_faction_ships", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<List<LivingEntity>>> NEARBY_MONSTERS = MEMORY_MODULE_TYPES.register("memory_module_type.nearby_monsters", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<EquipmentActionHandler>> ACTION_HANDLER = MEMORY_MODULE_TYPES.register("memory_module_type.action_handler", () -> new MemoryModuleType<>(Optional.empty()));


}
