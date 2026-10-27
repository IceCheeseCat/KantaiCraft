package com.github.icecheesecat.kantaicraft.model.block.model;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class ExpeditionBlockModel<T extends GeoAnimatable> extends DefaultedBlockGeoModel<T> {

    private static final ResourceLocation MODEL_LOCATION =
            new ResourceLocation(KantaiCraft.MODID, "expedition_block");

    public ExpeditionBlockModel() {
        super(MODEL_LOCATION);
    }
}
