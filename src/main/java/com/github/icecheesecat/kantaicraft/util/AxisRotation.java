package com.github.icecheesecat.kantaicraft.util;

import com.mojang.math.Axis;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class AxisRotation {

    public static Vec3 getRotatedVectorAroundAnAxis(Axis axis, Vec3 toRotate, float degree) {

        Quaternionf q = axis.rotationDegrees(degree);
        Quaternionf iq = q.invert(new Quaternionf());
        q.mul((float) toRotate.x, (float) toRotate.y, (float) toRotate.z, 0).mul(iq);

        return new Vec3(q.x, q.y, q.z);

    }

}
