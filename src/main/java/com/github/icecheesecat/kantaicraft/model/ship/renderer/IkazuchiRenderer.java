package com.github.icecheesecat.kantaicraft.model.ship.renderer;

import com.github.icecheesecat.kantaicraft.entityship.entity.cannoship.destroyer.Ikazuchi;
import com.github.icecheesecat.kantaicraft.model.EntityShipRenderer;
import com.github.icecheesecat.kantaicraft.model.ship.model.destroyer.IkazuchiModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.core.object.Color;

public class IkazuchiRenderer extends EntityShipRenderer<Ikazuchi> {
    public IkazuchiRenderer(EntityRendererProvider.Context renderManager, Color color) {
        super(renderManager, new IkazuchiModel(), 0.78f, 0.4f, color);
    }
}
