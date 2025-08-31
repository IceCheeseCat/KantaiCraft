package com.github.icecheesecat.kantaicraft.client.model.model;

import com.github.icecheesecat.kantaicraft.client.model.MaskModelColor;
import com.github.icecheesecat.kantaicraft.entity.ship.destroyer.hostile.HostileInazuma;
import net.minecraft.client.model.geom.ModelPart;

public class HostileInazumaModel<T extends HostileInazuma> extends InazumaModel<T> implements MaskModelColor {

    public HostileInazumaModel(ModelPart root) {
        super(root);
    }

    @Override
    public float getRed() {
        return 0.1f;
    }

    @Override
    public float getGreen() {
        return 0.1f;
    }

    @Override
    public float getBlue() {
        return 0.1f;
    }
}
