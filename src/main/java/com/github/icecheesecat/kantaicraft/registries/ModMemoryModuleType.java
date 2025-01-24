package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.util.tickable.EquipmentActionHandler;
import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;

public class ModMemoryModuleType {

    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, KantaiCraft.MODID);

    public static final RegistryObject<MemoryModuleType<Unit>> PLANE_TIMEOUT = MEMORY_MODULE_TYPES.register("plane_timeout", () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<Unit>> STRIKE_COOLDOWN = MEMORY_MODULE_TYPES.register("strike_cooldown", () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<UUID>> OWNERSHIP = MEMORY_MODULE_TYPES.register("ownership", () -> new MemoryModuleType<>(Optional.of(UUIDUtil.CODEC)));
    public static final RegistryObject<MemoryModuleType<Unit>> OUT_OF_FUEL = MEMORY_MODULE_TYPES.register("out_of_fuel", () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<Unit>> IS_GUARDING = MEMORY_MODULE_TYPES.register("is_guarding", () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<List<LivingEntity>>> NEARBY_DIFFERENT_FACTION_SHIPS = MEMORY_MODULE_TYPES.register("nearby_different_faction_ships", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<List<LivingEntity>>> NEARBY_MONSTERS = MEMORY_MODULE_TYPES.register("nearby_monsters", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<EquipmentActionHandler>> ACTION_HANDLER = MEMORY_MODULE_TYPES.register("action_handler", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<Unit>> ROUND_ROBIN_COOLDOWN = MEMORY_MODULE_TYPES.register("round_robin_cooldown", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<List<ItemEntity>>> KILLED_ENTITY_DROPS = MEMORY_MODULE_TYPES.register("killed_entity_drops", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<Unit>> IS_FOLLOWING = MEMORY_MODULE_TYPES.register("is_following", () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<LivingEntity>> VISIBLE_ENMEY = MEMORY_MODULE_TYPES.register("visible_enemy", () -> new MemoryModuleType<>(Optional.empty()));

}
