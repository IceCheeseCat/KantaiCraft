package com.github.icecheesecat.kantaicraft.client.model.model;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entity.ship.destroyer.Inazuma;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.GeoModel;

public class InazumaModel<T extends Inazuma> extends DefaultedEntityGeoModel<T> {
    public InazumaModel() {
        super(new ResourceLocation(KantaiCraft.MODID, "inazuma"), true);
    }

//    @Override
//    public ResourceLocation getModelResource(Inazuma inazuma) {
//        return new ResourceLocation(KantaiCraft.MODID, "geo/inazuma.geo.json");
//    }
//
//    @Override
//    public ResourceLocation getTextureResource(Inazuma inazuma) {
//        return new ResourceLocation(KantaiCraft.MODID, "textures/entity/inazuma.png");
//    }
//
//    @Override
//    public ResourceLocation getAnimationResource(Inazuma inazuma) {
//        return new ResourceLocation(KantaiCraft.MODID, "animations/inazuma.animation.json");
//    }
}
