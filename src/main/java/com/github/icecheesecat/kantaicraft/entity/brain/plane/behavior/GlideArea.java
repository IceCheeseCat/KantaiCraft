package com.github.icecheesecat.kantaicraft.entity.brain.plane.behavior;

import net.minecraft.world.phys.Vec3;

public class GlideArea {

    private Vec3 center;
    private double diameter;

    public GlideArea(Vec3 c, double d) {
        this.center = c;
        this.diameter = d;
    }

    public Vec3 getCenter() {
        return center;
    }

    public double getDiameter() {
        return diameter;
    }

    public void setCenter(Vec3 n_c) {
        this.center = n_c;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }
}
