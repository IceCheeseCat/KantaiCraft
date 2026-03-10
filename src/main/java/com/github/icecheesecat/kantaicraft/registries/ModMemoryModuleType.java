package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.tickable.EquipmentActionHandler;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
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
import java.util.UUID;

public class ModMemoryModuleType {


    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, KantaiCraft.MODID);
    public static final RegistryObject<MemoryModuleType<BlockPos>> LAST_SAW_TARGET_POS = register("last_saw_target_pos", BlockPos.CODEC);
    public static final RegistryObject<MemoryModuleType<EquipmentActionHandler>> ACTION_HANDLER = registerEmpty("action_handler");
    public static final RegistryObject<MemoryModuleType<List<ItemEntity>>> ITEMS_TO_PICK_UP = registerEmpty("items_to_pick_up");
    public static final RegistryObject<MemoryModuleType<Long>> CANT_SEE_TARGET_SINCE = register("cant_see_target_since", Codec.LONG);
    public static final RegistryObject<MemoryModuleType<Unit>> ATTACK_TARGET_IN_SIGHT = registerUnit("got_target_in_sight");
    public static final RegistryObject<MemoryModuleType<Unit>> IS_GUARDING = registerUnit("is_guarding");
    public static final RegistryObject<MemoryModuleType<Unit>> IS_HOSTILE_SHIP = registerUnit("is_hostile_ship");
    public static final RegistryObject<MemoryModuleType<Unit>> IS_PLAYER_SHIP = registerUnit("is_player_ship");
    public static final RegistryObject<MemoryModuleType<Unit>> IS_SITTING = registerUnit("sitting");
    public static final RegistryObject<MemoryModuleType<List<LivingEntity>>> NEARBY_TARGETS = MEMORY_MODULE_TYPES.register("nearby_targets", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<ItemEntity>> NEAREST_WANTED_ITEM = MEMORY_MODULE_TYPES.register("nearest_wanted_item", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<Unit>> OUT_OF_AIRCRAFT = registerUnit("out_of_aircraft");
    public static final RegistryObject<MemoryModuleType<Unit>> OUT_OF_AMMO = registerUnit("out_of_ammo");
    public static final RegistryObject<MemoryModuleType<Unit>> OUT_OF_FUEL = registerUnit("out_of_fuel");
    public static final RegistryObject<MemoryModuleType<UUID>> OWNERSHIP = register("ownership", UUIDUtil.CODEC);
    public static final RegistryObject<MemoryModuleType<Integer>> PICK_UP_COOLDOWN = register("pick_up_cooldown", Codec.INT);
    public static final RegistryObject<MemoryModuleType<Unit>> PLANE_TIMEOUT = registerUnit("plane_timeout");
    public static final RegistryObject<MemoryModuleType<Integer>> SIT_BACK_DOWN_COUNTDOWN = register("sit_back_down_countdown", Codec.INT);
    public static final RegistryObject<MemoryModuleType<Unit>> STRIKE_COOLDOWN = registerUnit("strike_cooldown");

    private static <T> RegistryObject<MemoryModuleType<T>> register(String name, Codec<T> codec) {
        return MEMORY_MODULE_TYPES.register(name, () -> new MemoryModuleType<>(Optional.of(codec)));
    }
    private static <T> RegistryObject<MemoryModuleType<T>> registerEmpty(String name) {
        return MEMORY_MODULE_TYPES.register(name, () -> new MemoryModuleType<>(Optional.empty()));
    }
    private static RegistryObject<MemoryModuleType<Unit>> registerUnit(String name) {
        return register(name, Codec.unit(Unit.INSTANCE));
    }

}
