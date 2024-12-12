package com.github.icecheesecat.kantaicraft.entity.plane;

import com.github.icecheesecat.kantaicraft.entity.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.init.ModBrainActivity;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public abstract class BasicEntityPlane extends Mob {

    public BasicEntityPlane(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_, BasicEntityShip owner, LivingEntity target, int liveTime) {
        super(p_21683_, p_21684_);
        this.setNoGravity(true);
        this.setAttackTarget(target);
        this.setOwnerShip(owner);
    }

    protected BasicEntityPlane(EntityType<? extends Mob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dyn) {
        return PlaneAi.makeBrain(this, dyn);
    }

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

    private void setAttackTarget(LivingEntity target) {
        this.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
    }

    private void setOwnerShip(LivingEntity ownerShip) {
        this.getBrain().setMemory(ModBrainActivity.OWNERSHIP.get(), ownerShip);
    }

}
