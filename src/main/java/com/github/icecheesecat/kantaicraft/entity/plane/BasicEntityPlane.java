package com.github.icecheesecat.kantaicraft.entity.plane;

import com.github.icecheesecat.kantaicraft.entity.BasicEntityShip;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public abstract class BasicEntityPlane extends PathfinderMob {

    private BasicEntityShip owner;
    private LivingEntity target;
    private int liveTime;

    public BasicEntityPlane(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_, BasicEntityShip owner, LivingEntity target, int liveTime) {
        super(p_21683_, p_21684_);
        this.owner = owner;
        this.target = target;
        this.liveTime = liveTime;
        this.setNoGravity(true);
    }

    protected BasicEntityPlane(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public void setLiveTime(int liveTime) {
        this.liveTime = liveTime;
    }

    @Override
    public void tick() {
        this.liveTime--;
        if (this.liveTime < 0) {
            this.discard();
            return;
        }
        super.tick();

        this.run();
    }

    @Override
    public Brain<?> getBrain() {
        return super.getBrain();
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return super.brainProvider();
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> p_21069_) {
        return super.makeBrain(p_21069_);
    }

    // Start State
    protected void start() {

        departing();

    }

    // Run State
    protected void run() {

    }

    // Return State

    // When its health reaches zero, starts to fall and when hit ground explode and dsicard
    protected boolean isCrashed() {
        Vec3 a = this.position();
        Vec3 b = a.add(this.getDeltaMovement());
        BlockHitResult blockHitResult = this.level().clip(new ClipContext(a, b, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, this));
        if (blockHitResult.getType() != HitResult.Type.MISS) {
            return true;
        }

        return false;
    }

    private void touchedGround() {
        this.level().explode(this, this.getX(), this.getY((double)0.0625F), this.getZ(), 0.1F, Level.ExplosionInteraction.MOB);
    }

    protected void getPath() {

    }

}
