package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.util.tickable.EquipmentActionHandler;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class ModMemoryModuleType {

    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, KantaiCraft.MODID);

    public static final RegistryObject<MemoryModuleType<Unit>> PLANE_TIMEOUT = MEMORY_MODULE_TYPES.register("plane_timeout", () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<Unit>> STRIKE_COOLDOWN = MEMORY_MODULE_TYPES.register("strike_cooldown", () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<UUID>> OWNERSHIP = MEMORY_MODULE_TYPES.register("ownership", () -> new MemoryModuleType<>(Optional.of(UUIDUtil.CODEC)));
    public static final RegistryObject<MemoryModuleType<Unit>> OUT_OF_FUEL = MEMORY_MODULE_TYPES.register("out_of_fuel", () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<Unit>> OUT_OF_AMMO = MEMORY_MODULE_TYPES.register("out_of_ammo", () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<Unit>> OUT_OF_AIRCRAFT = MEMORY_MODULE_TYPES.register("out_of_aircraft", () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<Unit>> IS_GUARDING = MEMORY_MODULE_TYPES.register("is_guarding", () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<List<LivingEntity>>> NEARBY_TARGETS = MEMORY_MODULE_TYPES.register("nearby_targets", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<EquipmentActionHandler>> ACTION_HANDLER = MEMORY_MODULE_TYPES.register("action_handler", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<List<ItemEntity>>> KILLED_ENTITY_DROPS = MEMORY_MODULE_TYPES.register("killed_entity_drops", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<Unit>> IS_HOSTILE_SHIP = MEMORY_MODULE_TYPES.register("is_hostile_ship", () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<Unit>> IS_PLAYER_SHIP = MEMORY_MODULE_TYPES.register("is_player_ship", () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<BlockPos>> LAST_SAW_TARGET_POS = MEMORY_MODULE_TYPES.register("last_saw_target_pos", () -> new MemoryModuleType<>(Optional.of(BlockPos.CODEC)));
    public static final RegistryObject<MemoryModuleType<Long>> CANT_SEE_TARGET_SINCE = MEMORY_MODULE_TYPES.register("cant_see_target_since", () -> new MemoryModuleType<>(Optional.empty()));

}
