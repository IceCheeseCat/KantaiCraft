package com.github.icecheesecat.kantaicraft.model.ship.model.destroyer;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entityship.entity.cannoship.DestroyerEntityShip;
import com.github.icecheesecat.kantaicraft.model.ship.model.BasicEntityShipModel;
import net.minecraft.resources.ResourceLocation;

public class DestroyerModel<T extends DestroyerEntityShip> extends BasicEntityShipModel<T> {
    protected final ResourceLocation[] fallbackAnimationResources = new ResourceLocation[2];
    public DestroyerModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
        this.fallbackAnimationResources[0] = new ResourceLocation(KantaiCraft.MODID, "animations/entity/entityship.animation.json");
        this.fallbackAnimationResources[1] = new ResourceLocation(KantaiCraft.MODID, "animations/entity/destroyer.animation.json");
    }

    @Override
    public ResourceLocation[] getAnimationResourceFallbacks(T animatable) {
        return this.fallbackAnimationResources;
    }
}
