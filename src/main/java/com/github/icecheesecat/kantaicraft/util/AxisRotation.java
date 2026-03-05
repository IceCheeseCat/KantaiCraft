package com.github.icecheesecat.kantaicraft.util;

import com.mojang.math.Axis;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class AxisRotation {

    public static final Vec3 XP = new Vec3(1,0,0); // EAST
    public static final Vec3 XN = new Vec3(-1,0,0); // WEST
    public static final Vec3 YP = new Vec3(0,1,0); // UP
    public static final Vec3 YN = new Vec3(0,-1,0); // DOWN
    public static final Vec3 ZP = new Vec3(0,0,1); // SOUTH
    public static final Vec3 ZN = new Vec3(0,0,-1); // NORTH

    public static Vec3 getRotatedVectorAroundAnAxis(Axis axis, Vec3 toRotate, float degree) {

        Quaternionf q = axis.rotationDegrees(degree);
        Quaternionf iq = q.invert(new Quaternionf());
        q.mul((float) toRotate.x, (float) toRotate.y, (float) toRotate.z, 0).mul(iq);

        return new Vec3(q.x, q.y, q.z);

    }

    public static Vec3 getRotatedVectorAroundAnAxis(Vec3 axisVector, Vec3 toRotate, float degree) {

        return getRotatedVectorAroundAnAxis(Axis.of(axisVector.toVector3f()), toRotate, degree);

    }

}
