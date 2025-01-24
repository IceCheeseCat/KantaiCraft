package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.stats.shipAttributes.SyncableAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModShipAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, KantaiCraft.MODID);

    public static final RegistryObject<Attribute> FIREPOWER = ATTRIBUTES.register("firepower", () -> new SyncableAttribute("firepower", 10.0d));
    public static final RegistryObject<Attribute> TORPEDO = ATTRIBUTES.register("torpedo", () -> new SyncableAttribute("torpedo", 5.0d));
    public static final RegistryObject<Attribute> ANTIAIR = ATTRIBUTES.register("antiair", () -> new SyncableAttribute("antiair", 20.0d));
    public static final RegistryObject<Attribute> ASW = ATTRIBUTES.register("asw", () -> new SyncableAttribute("asw", 30.0d));
    public static final RegistryObject<Attribute> LOS = ATTRIBUTES.register("los", () -> new SyncableAttribute("los", 30.0d));
    public static final RegistryObject<Attribute> LUCK = ATTRIBUTES.register("luck", () -> new SyncableAttribute("luck", 10.0d));
    public static final RegistryObject<Attribute> ARMOR = ATTRIBUTES.register("armor", () -> new SyncableAttribute("armor", 50.0d));
    public static final RegistryObject<Attribute> EVASION = ATTRIBUTES.register("evasion", () -> new SyncableAttribute("evasion", 10.0d));
    public static final RegistryObject<Attribute> AIRCRAFT = ATTRIBUTES.register("aircraft", () -> new SyncableAttribute("aircraft", 0.0d));
    public static final RegistryObject<Attribute> FUEL = ATTRIBUTES.register("fuel", () -> new SyncableAttribute("fuel", 10.0d));
    public static final RegistryObject<Attribute> AMMO = ATTRIBUTES.register("ammo", () -> new SyncableAttribute("ammo", 12.0d));
    public static final RegistryObject<Attribute> SLOT_SIZE = ATTRIBUTES.register("slotsize", () -> new SyncableAttribute("slotsize", 4.0d));

}
