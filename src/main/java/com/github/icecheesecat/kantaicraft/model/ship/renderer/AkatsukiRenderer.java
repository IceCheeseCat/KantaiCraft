package com.github.icecheesecat.kantaicraft.model.ship.renderer;

import com.github.icecheesecat.kantaicraft.entityship.entity.destroyer.Akatsuki;
import com.github.icecheesecat.kantaicraft.model.EntityShipRenderer;
import com.github.icecheesecat.kantaicraft.model.equipment.ClientEquippableSlotDetails;
import com.github.icecheesecat.kantaicraft.model.equipment.EquippableDetailSlots;
import com.github.icecheesecat.kantaicraft.model.ship.model.AkatsukiModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.core.object.Color;

public class AkatsukiRenderer extends EntityShipRenderer<Akatsuki> {
    public AkatsukiRenderer(EntityRendererProvider.Context renderManager, Color color) {
        super(renderManager, new AkatsukiModel(), 0.78f, 0.4f, color);
    }

    @Override
    protected EquippableDetailSlots defineDetailSlots() {
        return ClientEquippableSlotDetails.DESTROYER_DIVISION_6;
    }
}
