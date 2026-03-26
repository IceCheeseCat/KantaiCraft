package com.github.icecheesecat.kantaicraft.model.ship.renderer;

import com.github.icecheesecat.kantaicraft.entityship.entity.cannoship.destroyer.Inazuma;
import com.github.icecheesecat.kantaicraft.model.EntityShipRenderer;
import com.github.icecheesecat.kantaicraft.model.ship.model.InazumaModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.core.object.Color;

public class InazumaRenderer extends EntityShipRenderer<Inazuma> {
    public InazumaRenderer(EntityRendererProvider.Context renderManager, Color color) {
        super(renderManager, new InazumaModel(), 0.78f, 0.4f, color);
    }

}
