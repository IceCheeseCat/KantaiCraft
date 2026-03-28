package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entityship.attribute.shipAttributes.SyncableAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModAttribute {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, KantaiCraft.MODID);

    public static final RegistryObject<Attribute> FIREPOWER = ATTRIBUTES.register("firepower", () -> new SyncableAttribute("firepower", 10.0d));
    public static final RegistryObject<Attribute> TORPEDO = ATTRIBUTES.register("torpedo", () -> new SyncableAttribute("torpedo", 5.0d));
    public static final RegistryObject<Attribute> ANTIAIR = ATTRIBUTES.register("antiair", () -> new SyncableAttribute("antiair", 20.0d));
    public static final RegistryObject<Attribute> ASW = ATTRIBUTES.register("asw", () -> new SyncableAttribute("asw", 30.0d));
    public static final RegistryObject<Attribute> LOS = ATTRIBUTES.register("los", () -> new SyncableAttribute("los", 30.0d));
    public static final RegistryObject<Attribute> LUCK = ATTRIBUTES.register("luck", () -> new SyncableAttribute("luck", 10.0d));
    public static final RegistryObject<Attribute> ARMOR = ATTRIBUTES.register("armor", () -> new SyncableAttribute("armor", 50.0d));
    public static final RegistryObject<Attribute> EVASION = ATTRIBUTES.register("evasion", () -> new SyncableAttribute("evasion", 10.0d));
    public static final RegistryObject<Attribute> MAX_AIRCRAFT = ATTRIBUTES.register("max_aircraft", () -> new SyncableAttribute("max_aircraft", 0.0d));
    public static final RegistryObject<Attribute> MAX_FUEL = ATTRIBUTES.register("max_fuel", () -> new SyncableAttribute("max_fuel", 10.0d));
    public static final RegistryObject<Attribute> MAX_AMMO = ATTRIBUTES.register("max_ammo", () -> new SyncableAttribute("max_ammo", 12.0d));

}
