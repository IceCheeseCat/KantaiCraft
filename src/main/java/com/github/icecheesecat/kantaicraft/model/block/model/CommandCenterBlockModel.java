package com.github.icecheesecat.kantaicraft.model.block.model;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.block.commandcenter.CommandCenterBlockEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.model.DefaultedGeoModel;

public class CommandCenterBlockModel<T extends GeoAnimatable> extends DefaultedGeoModel<T> {

    private static final ResourceLocation location = new ResourceLocation(KantaiCraft.MODID, "command_center_block");
    public CommandCenterBlockModel(BlockEntityRendererProvider.Context context) {
        super(location);
    }

    @Override
    protected String subtype() {
        return "block";
    }
}
