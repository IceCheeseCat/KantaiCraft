package com.github.icecheesecat.kantaicraft.entityplane;

import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.entityplane.brain.plane.PlaneAi;
import com.github.icecheesecat.kantaicraft.entityplane.fighter.EntityFighterPlane;
import com.github.icecheesecat.kantaicraft.entityship.attribute.planeAttributes.PlaneAttributes;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class BasicEntityPlane extends Mob {

    protected PlaneAttributes planeAttributes;

    public BasicEntityPlane(EntityType<? extends Mob> p_21683_, Level level) {
        super(p_21683_, level);
    }
    public static final EntityDataAccessor<Integer> DATA_FACTION = SynchedEntityData.defineId(EntityFighterPlane.class, EntityDataSerializers.INT);

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, @Nullable SpawnGroupData p_21437_, @Nullable CompoundTag p_21438_) {
        this.setNoGravity(true);
        if (!this.getBrain().hasMemoryValue(ModMemoryModuleType.OWNERSHIP.get())) {
            this.setOwnerShip(this.level().getNearestPlayer(this, 10.0d));
        }
        return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, p_21438_);
    }

    @Override
    protected void customServerAiStep() {
        ServerLevel serverLevel = (ServerLevel) this.level();
        this.getBrain().tick(serverLevel, this);
        this.updateActivity();
    }

    @Override
    public void tick() {
        super.tick();
        if (!isNoGravity()) {
            this.setNoGravity(true);
        }
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dyn) {
        return PlaneAi.makeBrain(this, dyn);
    }

    @Override
    public Brain<BasicEntityPlane> getBrain() {
        return (Brain<BasicEntityPlane>) super.getBrain();
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

    private void selfDestruction() {
        this.level().explode(this, this.getX(), this.getY((double) 0.0625F), this.getZ(), 0.1F, Level.ExplosionInteraction.MOB);
    }

    private void setAttackTarget(LivingEntity target) {
        this.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
    }

    private void setOwnerShip(LivingEntity ownerShip) {
        this.getBrain().setMemory(ModMemoryModuleType.OWNERSHIP.get(), ownerShip.getUUID());
    }

    public PlaneAttributes getPlaneAttributes() {
        return planeAttributes;
    }

    public abstract void planeHurtTarget(LivingEntity target, double dmgValue);

    protected abstract void updateActivity();
}
