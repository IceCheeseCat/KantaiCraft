package com.github.icecheesecat.kantaicraft.model.ship.model;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.entityship.entity.destroyer.Akatsuki;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class BasicEntityShipModel <T extends EntityShip> extends DefaultedEntityGeoModel<T> {
    public BasicEntityShipModel(ResourceLocation assetSubpath) {
        super(assetSubpath, true);
    }
}
