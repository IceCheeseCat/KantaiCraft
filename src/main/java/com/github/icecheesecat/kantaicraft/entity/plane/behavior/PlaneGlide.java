package com.github.icecheesecat.kantaicraft.entity.plane.behavior;

import com.github.icecheesecat.kantaicraft.entity.plane.BasicEntityPlane;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class PlaneGlide extends Behavior<BasicEntityPlane> {

    private float glideSpeedInTick;
    private GlideArea glideArea;
    private static final double THRESHOLD = 1.5d;
    private int turnTimer = 0;
    private Vec3 glideVector;

    public PlaneGlide(float glideSpeed, GlideArea area) {
        super(ImmutableMap.of());
        this.glideSpeedInTick = glideSpeed / 20.0f; // calculate blocks per tick
        this.glideArea = area;
    }

    @Override
    protected void start(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane, long gametime) {
        double rand_0 = basicEntityPlane.getRandom().nextDouble() * 2.0d - 1.0d;
//        double rand_1 = basicEntityPlane.getRandom().nextDouble();
        double rand_2 = basicEntityPlane.getRandom().nextDouble() * 2.0d - 1.0d;
        this.glideVector = new Vec3(rand_0, 0, rand_2).normalize().multiply(glideSpeedInTick, glideSpeedInTick, glideSpeedInTick);
    }

    @Override
    protected void tick(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane, long gametime) {
        Vec3 currPosition = basicEntityPlane.position();
        double r = currPosition.distanceTo(glideArea.getCenter());
        if (r > glideArea.getDiameter()) {
            Vec3 vec = glideArea.getCenter().subtract(currPosition).normalize();
            glideVector = vec.multiply(glideSpeedInTick, glideSpeedInTick, glideSpeedInTick);
        }
        else if (r < glideArea.getDiameter() + THRESHOLD && r > glideArea.getDiameter() && turnTimer > 0) {
            Vec3 vec = glideArea.getCenter().subtract(currPosition).normalize();
            float rand = basicEntityPlane.getRandom().nextFloat() * 2.0f - 1.0f;
            float theta = rand * Mth.PI / 2.0f;

            Vec3 n_path = new Vec3(Mth.cos(theta) * vec.x - Mth.sin(theta) * vec.y, 0, Mth.sin(theta) * vec.x + Mth.cos(theta) * vec.y);
            this.glideVector = n_path;
            this.turnTimer = 30;
        }

        basicEntityPlane.setDeltaMovement(this.glideVector);
        turnTimer--;
    }


}
