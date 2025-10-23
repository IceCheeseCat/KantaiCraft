package com.github.icecheesecat.kantaicraft.client.model.renderer;

import com.github.icecheesecat.kantaicraft.client.model.ShipGeoEntityRenderer;
import com.github.icecheesecat.kantaicraft.client.model.model.IkazuchiModel;
import com.github.icecheesecat.kantaicraft.client.model.model.InazumaModel;
import com.github.icecheesecat.kantaicraft.entity.ship.destroyer.hostile.HostileIkazuchi;
import com.github.icecheesecat.kantaicraft.entity.ship.destroyer.hostile.HostileInazuma;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class HostileIkazuchiRenderer extends ShipGeoEntityRenderer<HostileIkazuchi> {

    public HostileIkazuchiRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new IkazuchiModel<>(), 0.35f, 0.4f, HOSTILE_COLOR);
    }

}
