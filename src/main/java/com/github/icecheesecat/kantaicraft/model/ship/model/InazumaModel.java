package com.github.icecheesecat.kantaicraft.model.ship.model;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entityship.entity.destroyer.Inazuma;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class InazumaModel<T extends Inazuma> extends DefaultedEntityGeoModel<T> {
    public InazumaModel() {
        super(new ResourceLocation(KantaiCraft.MODID, "inazuma"), true);
    }

    @Override
    public ResourceLocation getAnimationResource(Inazuma inazuma) {
        return new ResourceLocation(KantaiCraft.MODID, "animations/entity/destroyer.animation.json");
    }

}
