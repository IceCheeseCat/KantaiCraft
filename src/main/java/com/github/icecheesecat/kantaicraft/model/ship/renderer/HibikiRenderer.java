package com.github.icecheesecat.kantaicraft.model.ship.renderer;

import com.github.icecheesecat.kantaicraft.entityship.entity.destroyer.Hibiki;
import com.github.icecheesecat.kantaicraft.model.EntityShipRenderer;
import com.github.icecheesecat.kantaicraft.model.equipment.ClientEquippableSlotDetails;
import com.github.icecheesecat.kantaicraft.model.equipment.EquippableDetailSlots;
import com.github.icecheesecat.kantaicraft.model.ship.model.HibikiModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.core.object.Color;

public class HibikiRenderer extends EntityShipRenderer<Hibiki> {
    public HibikiRenderer(EntityRendererProvider.Context renderManager, Color color) {
        super(renderManager, new HibikiModel<>(), 0.78f, 0.4f, color);
    }

    @Override
    protected EquippableDetailSlots defineDetailSlots() {
        return ClientEquippableSlotDetails.DESTROYER_DIVISION_6;
    }
}
