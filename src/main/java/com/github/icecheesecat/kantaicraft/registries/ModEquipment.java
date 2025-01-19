package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.Equipments;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModEquipment {

    public static final DeferredRegister<Equipment> EQUIPMENTS = DeferredRegister.create(new ResourceLocation(KantaiCraft.MODID, "deferredregister/equipment"), KantaiCraft.MODID);
    public static final Supplier<IForgeRegistry<Equipment>> REGISTRY_SUPPLIER = EQUIPMENTS.makeRegistry(RegistryBuilder::new);
    public static final RegistryObject<Equipment> EMPTY = EQUIPMENTS.register("empty", () -> Equipment.EMPTY);
    public static final RegistryObject<Equipment> __12cm_single_sun_mount__ = EQUIPMENTS.register("12cm_single_gun_mount", () -> Equipments.__12cm_single_gun_mount__);

    public static final Map<Integer, RegistryObject<Equipment>> storage = new HashMap<>();

    static {
    }

}
