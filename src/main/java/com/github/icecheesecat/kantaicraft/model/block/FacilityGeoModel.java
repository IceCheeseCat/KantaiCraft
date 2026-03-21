package com.github.icecheesecat.kantaicraft.model.block;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedGeoModel;

public class FacilityGeoModel <T extends GeoAnimatable> extends DefaultedGeoModel<T> {

    public FacilityGeoModel(BlockEntityRendererProvider.Context context, ResourceLocation location) {
        super(location);
    }

    @Override
    protected String subtype() {
        return "facility";
    }

}
