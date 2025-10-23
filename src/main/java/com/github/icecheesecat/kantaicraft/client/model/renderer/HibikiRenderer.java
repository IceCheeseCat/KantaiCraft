package com.github.icecheesecat.kantaicraft.client.model.renderer;

import com.github.icecheesecat.kantaicraft.client.model.ShipGeoEntityRenderer;
import com.github.icecheesecat.kantaicraft.client.model.model.HibikiModel;
import com.github.icecheesecat.kantaicraft.client.model.model.IkazuchiModel;
import com.github.icecheesecat.kantaicraft.entity.ship.destroyer.Hibiki;
import com.github.icecheesecat.kantaicraft.entity.ship.destroyer.Ikazuchi;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.core.object.Color;

public class HibikiRenderer extends ShipGeoEntityRenderer<Hibiki> {
    public HibikiRenderer(EntityRendererProvider.Context renderManager, Color color) {
        super(renderManager, new HibikiModel<>(), 0.78f, 0.4f, color);
    }

}
