package com.github.icecheesecat.kantaicraft.client.model.renderer;

import com.github.icecheesecat.kantaicraft.client.model.ShipGeoEntityRenderer;
import com.github.icecheesecat.kantaicraft.client.model.model.IkazuchiModel;
import com.github.icecheesecat.kantaicraft.client.model.model.InazumaModel;
import com.github.icecheesecat.kantaicraft.entity.ship.destroyer.Ikazuchi;
import com.github.icecheesecat.kantaicraft.entity.ship.destroyer.Inazuma;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.core.object.Color;

public class IkazuchiRenderer extends ShipGeoEntityRenderer<Ikazuchi> {
    public IkazuchiRenderer(EntityRendererProvider.Context renderManager, Color color) {
        super(renderManager, new IkazuchiModel<>(), 0.78f, 0.4f, color);
    }

}
