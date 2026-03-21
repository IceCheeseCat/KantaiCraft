package com.github.icecheesecat.kantaicraft.model.block.model;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.block.shipyard.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.model.block.FacilityGeoModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ShipyardModel extends FacilityGeoModel<ShipyardBlockEntity> {

    private static final ResourceLocation location = new ResourceLocation(KantaiCraft.MODID, "shipyard");
    public ShipyardModel(BlockEntityRendererProvider.Context context) {
        super(context, location);
    }

}
