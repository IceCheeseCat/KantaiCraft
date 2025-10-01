package com.github.icecheesecat.kantaicraft.client.model.renderer;

import com.github.icecheesecat.kantaicraft.client.model.ShipGeoEntityRenderer;
import com.github.icecheesecat.kantaicraft.client.model.model.InazumaModel;
import com.github.icecheesecat.kantaicraft.entity.ship.destroyer.Inazuma;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class InazumaRenderer extends ShipGeoEntityRenderer<Inazuma> {
    public InazumaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new InazumaModel<>(), 0.78f, 0.4f);
    }

}
