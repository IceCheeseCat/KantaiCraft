package com.github.icecheesecat.kantaicraft.model.ship.model;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entityship.entity.destroyer.Inazuma;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class InazumaModel extends BasicEntityShipModel<Inazuma> {
    public InazumaModel() {
        super(new ResourceLocation(KantaiCraft.MODID, "inazuma"));
    }

}
