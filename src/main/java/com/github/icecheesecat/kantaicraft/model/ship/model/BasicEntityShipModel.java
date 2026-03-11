package com.github.icecheesecat.kantaicraft.model.ship.model;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class BasicEntityShipModel <T extends EntityShip> extends DefaultedEntityGeoModel<T> {

    protected final ResourceLocation[] fallbackAnimationResources = new ResourceLocation[1];

    public BasicEntityShipModel(ResourceLocation assetSubpath) {
        super(assetSubpath, true);
        this.fallbackAnimationResources[0] = new ResourceLocation(KantaiCraft.MODID, "animations/entity/entityship.animation.json");
    }

    @Override
    public ResourceLocation[] getAnimationResourceFallbacks(T animatable) {
        return this.fallbackAnimationResources;
    }
}
