package com.github.icecheesecat.kantaicraft.client.animation.util;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class AnimationUtil {

    public static void headLooking(ModelPart head, float pitch, float yaw) {
        head.xRot = -pitch * Mth.DEG_TO_RAD;
        head.yRot = yaw * Mth.DEG_TO_RAD - Mth.PI;
    }

}
