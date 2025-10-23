package com.github.icecheesecat.kantaicraft.client.model.renderer;

import com.github.icecheesecat.kantaicraft.client.model.ShipGeoEntityRenderer;
import com.github.icecheesecat.kantaicraft.client.model.model.InazumaModel;
import com.github.icecheesecat.kantaicraft.entity.ship.destroyer.hostile.HostileInazuma;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.core.object.Color;

public class HostileInazumaRenderer extends ShipGeoEntityRenderer<HostileInazuma> {

    public HostileInazumaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new InazumaModel<>(), 0.35f, 0.4f, HOSTILE_COLOR);
    }

    @Override
    public Color getRenderColor(HostileInazuma animatable, float partialTick, int packedLight) {
        return super.getRenderColor(animatable, partialTick, packedLight);
    }
}
