package com.github.icecheesecat.kantaicraft.entity.plane.brain.behavior;

import com.github.icecheesecat.kantaicraft.entity.plane.BasicEntityPlane;
import com.github.icecheesecat.kantaicraft.init.ModBrain;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;

public class PlaneGlide extends Behavior<BasicEntityPlane> {

    private GlideArea glideArea;
    private static final double THRESHOLD = 3.0d;
    private int turnTimer = 0;
    private final int TURNTIMEOUT = 100;
    private Vec3 velocity;
    private Vec3 acceleration;
    private final double speed;

    public PlaneGlide(double speed) {
        super(ImmutableMap.of(ModBrain.OWNERSHIP.get(), MemoryStatus.VALUE_PRESENT));
        this.speed = speed; // calculate blocks per tick
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane) {
        var mem = basicEntityPlane.getBrain().getMemory(ModBrain.OWNERSHIP.get());
        if (mem.isPresent() && ((ServerLevel) basicEntityPlane.level()).getEntity(mem.get()) != null) {
            return true;
        }

        return false;
    }

    @Override
    protected void start(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane, long gametime) {
//        basicEntityPlane.getBrain().getMemory(ModBrain.OWNERSHIP.get()).ifPresentOrElse((e) -> this.velocity = e.getLookAngle(), () -> this.velocity = Vec3.ZERO);
        this.velocity = new Vec3(0.25d, 0.0d, 0.7d).scale(this.speed/20.0d);
        basicEntityPlane.getBrain().getMemory(ModBrain.OWNERSHIP.get()).ifPresentOrElse(
                m -> this.glideArea = new GlideArea(serverLevel.getEntity(m).position(), 20.0d),
                () -> this.glideArea = new GlideArea(Vec3.ZERO, 0.0d)
        );
        this.acceleration = Vec3.ZERO;
        this.turnTimer = 0;
    }

    @Override
    protected void tick(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane, long gametime) {
        Vec3 currPosition = basicEntityPlane.position();
        double r = currPosition.distanceTo(glideArea.getCenter());

        if (r > glideArea.getDiameter() - THRESHOLD) {
            double coeff = 0.1d;
//            F = Gmm/r/r;
            Vec3 towards_center = glideArea.getCenter().subtract(currPosition).normalize();
            double dis = r - glideArea.getDiameter();
            this.acceleration = towards_center.scale(coeff / dis / dis);
        }
        else {
            if (this.turnTimer <= 0) {
                turnTimer = TURNTIMEOUT;

                double rand_0 = basicEntityPlane.getRandom().nextDouble() * 2.0d - 1.0d;
//                double rand_1 = basicEntityPlane.getRandom().nextDouble() * 2.0d - 1.0d;
                double rand_2 = basicEntityPlane.getRandom().nextDouble() * 2.0d - 1.0d;

                double TurnAcceleration = 0.5d;
                this.acceleration = new Vec3(rand_0, 0.0d, rand_2).normalize().scale(0.1d);
            }
        }

        this.velocity = this.velocity.add(this.acceleration).normalize().scale(speed/20.0d);
        basicEntityPlane.setDeltaMovement(this.velocity);
//        this.glideArea.setCenter(this.glideArea.getCenter().add(0.1d, 0.0d, 0.0d));
//        this.glideArea.setDiameter(10.0d);
        turnTimer--;
    }

    @Override
    protected void stop(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane, long gametime) {
        this.turnTimer = 0;
        this.velocity = Vec3.ZERO;
        this.glideArea = new GlideArea(Vec3.ZERO, 0);
    }

    @Override
    protected boolean canStillUse(ServerLevel p_22545_, BasicEntityPlane p_22546_, long p_22547_) {
        return true;
    }

    @Override
    protected boolean timedOut(long p_22537_) {
        return false;
    }
}
