package com.github.icecheesecat.kantaicraft.model.ship.model;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entityship.entity.destroyer.Hibiki;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class HibikiModel<T extends Hibiki> extends DefaultedEntityGeoModel<T> {
    public HibikiModel() {
        super(new ResourceLocation(KantaiCraft.MODID, "hibiki"), true);
    }

    @Override
    public ResourceLocation getAnimationResource(Hibiki inazuma) {
        return new ResourceLocation(KantaiCraft.MODID, "animations/entity/hibiki.animation.json");
    }
}
