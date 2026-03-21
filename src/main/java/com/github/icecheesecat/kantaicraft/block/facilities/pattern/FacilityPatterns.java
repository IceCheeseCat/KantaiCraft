package com.github.icecheesecat.kantaicraft.block.facilities.pattern;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.block.facilities.FacilityBlock;
import com.github.icecheesecat.kantaicraft.block.facilities.FacilityCoreBlock;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class FacilityPatterns {
    private static FacilityPattern.Getter FUEL_STATION_PATTERN =  () ->
            FacilityPattern.Builder.start(3,2,2)
            .addAll((FacilityBlock) ModBlock.FACILITY.get())
            .addCoreBlock((FacilityCoreBlock) ModBlock.FUEL_STATION.get())
            .setName(Component.translatable("facility.kantaicraft.fuel_station")).build();
    private static FacilityPattern.Getter SHIPYARD_PATTERN = () ->
            FacilityPattern.Builder.start(2,2,2)
            .addAll((FacilityBlock) ModBlock.FACILITY.get())
            .addCoreBlock((FacilityCoreBlock) ModBlock.SHIPYARD.get())
            .setName(Component.translatable("facility.kantaicraft.shipyard")).build();

    public static final DeferredRegister<FacilityPattern.Getter> PATTERN_GETTERS = DeferredRegister.create(new ResourceLocation(KantaiCraft.MODID, "facility_getters"), KantaiCraft.MODID);
    public static final Supplier<IForgeRegistry<FacilityPattern.Getter>> REGISTRY = PATTERN_GETTERS.makeRegistry(RegistryBuilder::new);
    public static final RegistryObject<FacilityPattern.Getter> FUEL_STATION = PATTERN_GETTERS.register("fsp", () -> FUEL_STATION_PATTERN);
    public static final RegistryObject<FacilityPattern.Getter> SHIPYARD = PATTERN_GETTERS.register("sp", () -> SHIPYARD_PATTERN);

}
