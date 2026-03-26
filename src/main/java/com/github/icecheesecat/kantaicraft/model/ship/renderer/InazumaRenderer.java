package com.github.icecheesecat.kantaicraft.model.ship.renderer;

import com.github.icecheesecat.kantaicraft.entityship.entity.cannoship.destroyer.Inazuma;
import com.github.icecheesecat.kantaicraft.model.EntityShipRenderer;
import com.github.icecheesecat.kantaicraft.model.equipment.ClientEquippableSlotDetails;
import com.github.icecheesecat.kantaicraft.model.equipment.EquippableDetailSlots;
import com.github.icecheesecat.kantaicraft.model.ship.model.InazumaModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.List;

public class InazumaRenderer extends EntityShipRenderer<Inazuma> {
    public InazumaRenderer(EntityRendererProvider.Context renderManager, Color color) {
        super(renderManager, new InazumaModel(), 0.78f, 0.4f, color);
    }

    @Override
    public List<GeoRenderLayer<Inazuma>> getRenderLayers() {
        return super.getRenderLayers();
    }

    @Override
    protected EquippableDetailSlots defineDetailSlots() {
        return ClientEquippableSlotDetails.DESTROYER_DIVISION_6;
    }

}
