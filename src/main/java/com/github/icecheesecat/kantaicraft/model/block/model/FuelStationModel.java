package com.github.icecheesecat.kantaicraft.model.block.model;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.block.facilities.fuelstation.FuelStationBlockEntity;
import com.github.icecheesecat.kantaicraft.model.block.FacilityGeoModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FuelStationModel extends FacilityGeoModel<FuelStationBlockEntity> {

    private static final ResourceLocation location = new ResourceLocation(KantaiCraft.MODID, "fuel_station");
    public FuelStationModel(BlockEntityRendererProvider.Context context) {
        super(context, location);
    }

    @Override
    protected String subtype() {
        return "facility";
    }
}
