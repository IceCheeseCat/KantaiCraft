package com.github.icecheesecat.kantaicraft.model.block.model;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedGeoModel;

public class FuelStationModel<T extends GeoAnimatable> extends DefaultedGeoModel<T> {

    private static final ResourceLocation location = new ResourceLocation(KantaiCraft.MODID, "fuel_station");
    public FuelStationModel(BlockEntityRendererProvider.Context context) {
        super(location);
    }

    @Override
    protected String subtype() {
        return "facility";
    }
}
