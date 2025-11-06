package com.github.icecheesecat.kantaicraft.model.ship.model;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entityship.entity.destroyer.Akatsuki;
import com.github.icecheesecat.kantaicraft.entityship.entity.destroyer.Hibiki;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class AkatsukiModel extends BasicEntityShipModel<Akatsuki> {
    public AkatsukiModel() {
        super(new ResourceLocation(KantaiCraft.MODID, "akatsuki"));
    }

}
