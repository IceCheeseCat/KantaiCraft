package com.github.icecheesecat.kantaicraft.client.model.renderer;

import com.github.icecheesecat.kantaicraft.client.model.ShipGeoEntityRenderer;
import com.github.icecheesecat.kantaicraft.client.model.model.HibikiModel;
import com.github.icecheesecat.kantaicraft.entityship.entity.destroyer.Hibiki;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.core.object.Color;

public class HibikiRenderer extends ShipGeoEntityRenderer<Hibiki> {
    public HibikiRenderer(EntityRendererProvider.Context renderManager, Color color) {
        super(renderManager, new HibikiModel<>(), 0.78f, 0.4f, color);
    }

}
