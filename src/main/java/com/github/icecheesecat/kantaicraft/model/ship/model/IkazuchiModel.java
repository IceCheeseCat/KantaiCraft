package com.github.icecheesecat.kantaicraft.model.ship.model;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entityship.entity.destroyer.Ikazuchi;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class IkazuchiModel extends BasicEntityShipModel<Ikazuchi> {
    public IkazuchiModel() {
        super(new ResourceLocation(KantaiCraft.MODID, "ikazuchi"));
    }

}
