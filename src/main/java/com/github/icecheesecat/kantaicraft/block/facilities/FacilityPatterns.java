package com.github.icecheesecat.kantaicraft.block.facilities;

import com.github.icecheesecat.kantaicraft.block.facilities.pattern.FacilityPattern;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.network.chat.Component;

public class FacilityPatterns {
    public static FacilityPattern.Getter FUEL_STATION =  () ->
            FacilityPattern.Builder.start(3,2,2)
            .addAll((FacilityBlock) ModBlock.FACILITY.get())
            .addCoreBlock((FacilityCoreBlock) ModBlock.FUEL_STATION.get())
            .setName(Component.translatable("facility.kantaicraft.fuel_station")).build();
    public static FacilityPattern.Getter SHIPYARD = () ->
            FacilityPattern.Builder.start(2,2,2)
            .addAll((FacilityBlock) ModBlock.FACILITY.get())
            .addCoreBlock((FacilityCoreBlock) ModBlock.SHIPYARD.get())
            .setName(Component.translatable("facility.kantaicraft.shipyard")).build();
}
