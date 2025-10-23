package com.github.icecheesecat.kantaicraft.client.model.model;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entity.ship.destroyer.Ikazuchi;
import com.github.icecheesecat.kantaicraft.entity.ship.destroyer.Inazuma;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class IkazuchiModel<T extends Ikazuchi> extends DefaultedEntityGeoModel<T> {
    public IkazuchiModel() {
        super(new ResourceLocation(KantaiCraft.MODID, "ikazuchi"), true);
    }
    
    @Override
    public ResourceLocation getAnimationResource(Ikazuchi ikazuchi) {
        return new ResourceLocation(KantaiCraft.MODID, "animations/entity/destroyer.animation.json");
    }
}
