package com.github.icecheesecat.kantaicraft.init;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.stats.ShipAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModShipAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, KantaiCraft.MODID);

    public static final RegistryObject<Attribute> FIREPOWER = ATTRIBUTES.register("shipstats.firepower", () -> new ShipAttribute("shipstats.firepower", 10.0d));
    public static final RegistryObject<Attribute> TORPEDO = ATTRIBUTES.register("shipstats.torpedo", () -> new ShipAttribute("shipstats.torpedo", 5.0d));
    public static final RegistryObject<Attribute> ANTIAIR = ATTRIBUTES.register("shipstats.antiair", () -> new ShipAttribute("shipstats.antiair", 20.0d));
    public static final RegistryObject<Attribute> ASW = ATTRIBUTES.register("shipstats.asw", () -> new ShipAttribute("shipstats.asw", 30.0d));
    public static final RegistryObject<Attribute> LOS = ATTRIBUTES.register("shipstats.los", () -> new ShipAttribute("shipstats.los", 30.0d));
    public static final RegistryObject<Attribute> LUCK = ATTRIBUTES.register("shipstats.luck", () -> new ShipAttribute("shipstats.luck", 10.0d));
    public static final RegistryObject<Attribute> ARMOR = ATTRIBUTES.register("shipstats.armor", () -> new ShipAttribute("shipstats.armor", 50.0d));
    public static final RegistryObject<Attribute> EVASION = ATTRIBUTES.register("shipstats.evasion", () -> new ShipAttribute("shipstats.evasion", 10.0d));
    public static final RegistryObject<Attribute> AIRCRAFT = ATTRIBUTES.register("shipstats.aircraft", () -> new ShipAttribute("shipstats.aircraft", 0.0d));
    public static final RegistryObject<Attribute> FUEL = ATTRIBUTES.register("shipstats.fuel", () -> new ShipAttribute("shipstats.fuel", 10.0d));
    public static final RegistryObject<Attribute> AMMO = ATTRIBUTES.register("shipstats.ammo", () -> new ShipAttribute("shipstats.ammo", 12.0d));
    public static final RegistryObject<Attribute> SLOT_SIZE = ATTRIBUTES.register("shipstats.slotsize", () -> new ShipAttribute("shipstats.slotsize", 4.0d));

}
