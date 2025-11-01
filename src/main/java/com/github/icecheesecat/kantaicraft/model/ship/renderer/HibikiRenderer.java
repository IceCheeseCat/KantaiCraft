package com.github.icecheesecat.kantaicraft.model.ship.renderer;

import com.github.icecheesecat.kantaicraft.model.EntityShipRenderer;
import com.github.icecheesecat.kantaicraft.model.equipment.BodyPart;
import com.github.icecheesecat.kantaicraft.model.equipment.OffsetsMapping;
import com.github.icecheesecat.kantaicraft.model.ship.model.HibikiModel;
import com.github.icecheesecat.kantaicraft.entityship.entity.destroyer.Hibiki;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.joml.Vector3d;
import software.bernie.geckolib.core.object.Color;

import java.util.Map;

public class HibikiRenderer extends EntityShipRenderer<Hibiki> {
    public HibikiRenderer(EntityRendererProvider.Context renderManager, Color color) {
        super(renderManager, new HibikiModel<>(), 0.78f, 0.4f, color);
    }

    @Override
    protected Map<BodyPart, Vector3d> defineBodyPartOffsetToWeapon() {
        return OffsetsMapping.DESTROYER_DIVISION_6;
    }
}
