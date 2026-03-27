package com.github.icecheesecat.kantaicraft.entityship.brain.behavior;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * {@link EntityShip} of not hostile follow Owner Player
 */
public class FollowOwner extends Behavior<EntityShip> {
    int closeEnough, tooClose;
    @Nullable LivingEntity entityOwner;
    @Nullable Path path;
    public FollowOwner(int closeEnough, int tooClose) {
        super(ImmutableMap.of(
                ModMemoryModuleType.IS_PLAYER_SHIP.get(), MemoryStatus.REGISTERED,
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.PATH, MemoryStatus.VALUE_ABSENT,
                ModMemoryModuleType.NEAREST_WANTED_ITEM.get(), MemoryStatus.VALUE_ABSENT), 400);
        this.closeEnough = closeEnough;
        this.tooClose = tooClose;
    }

    @Override
    protected void start(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        this.path = null;
    }

    @Override
    protected void tick(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        pEntity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(this.entityOwner, true));

        if (teleportWhenDistanceGreaterThan(this.entityOwner.position(), pEntity.position(), 144.0d)) {
            this.teleportToOwner(pLevel, this.entityOwner.blockPosition(), pEntity);
        }
        else if (!pEntity.position().closerThan(this.entityOwner.position(), tooClose)) {
            this.path = pEntity.getNavigation().createPath(this.entityOwner, tooClose);
            this.submitNewPath(pEntity, this.path);

        }
    }

    @Override
    protected void stop(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        pEntity.getBrain().eraseMemory(MemoryModuleType.PATH);
        pEntity.getNavigation().stop();
    }

    private void submitNewPath(EntityShip entityShip, Path path) {
        entityShip.getBrain().setMemory(MemoryModuleType.PATH, path);
        entityShip.getNavigation().moveTo(path, entityShip.getRunSpeedModifier());
    }

    protected boolean teleportWhenDistanceGreaterThan(Vec3 playerPosition, Vec3 shipPosition, double distance) {
        return shipPosition.distanceTo(playerPosition) > distance;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, EntityShip pOwner) {
        if (pOwner.isSitDown()) return false;

        this.entityOwner = pOwner.getOwnerEntity();
        if (this.entityOwner == null) return false;

        return pOwner.distanceTo(this.entityOwner) > this.closeEnough;
    }

    @Override
    protected boolean canStillUse(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        if (this.entityOwner == null) return false;
        boolean isTooClose = pEntity.distanceTo(this.entityOwner) < this.tooClose;
        boolean finishedPathing = pEntity.getNavigation().isDone();
        boolean hasAttackTarget = pEntity.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET);
        boolean hasWantedItem = pEntity.getBrain().hasMemoryValue(ModMemoryModuleType.NEAREST_WANTED_ITEM.get());

        return (!isTooClose || !finishedPathing) && !hasAttackTarget && !hasWantedItem;
    }

    /**
     * References from {@link FollowOwnerGoal} <p></p>
     * finds a stand able block around player's current blockPos 3 * 3 * 3
     */
    private void teleportToOwner(Level level, BlockPos playerPosition, EntityShip entityShip) {

        for(int i = -3; i <= 3; ++i) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -3; k <= 3; k++) {
                    boolean flag = this.maybeTeleportTo(playerPosition.getX() + i, playerPosition.getY() + j, playerPosition.getZ() + k, level, playerPosition, entityShip);
                    if (flag) {
                        return;
                    }
                }
            }

        }

    }

    private boolean maybeTeleportTo(int pX, int pY, int pZ, Level level, BlockPos playerPosition, EntityShip entityShip) {
        if (Math.abs((double)pX - playerPosition.getX()) < 2.0D && Math.abs((double)pZ - playerPosition.getZ()) < 2.0D) {
            return false;
        } else if (!this.canTeleportTo(new BlockPos(pX, pY, pZ), level, entityShip)) {
            return false;
        } else {
            entityShip.moveTo((double)pX + 0.5D, (double)pY, (double)pZ + 0.5D, entityShip.getYRot(), entityShip.getXRot());
            entityShip.getNavigation().stop();
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos pPos, Level level, EntityShip entityShip) {
        BlockPathTypes blockpathtypes = WalkNodeEvaluator.getBlockPathTypeStatic(level, pPos.mutable());

        if (blockpathtypes != BlockPathTypes.WALKABLE) {
            return false;
        } else {
            BlockState blockstate = level.getBlockState(pPos.below());
            if (blockstate.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = pPos.subtract(entityShip.blockPosition());
                return level.noCollision(entityShip, entityShip.getBoundingBox().move(blockpos));
            }
        }
    }

    private int randomIntInclusive(int pMin, int pMax, EntityShip entityShip) {
        return entityShip.getRandom().nextInt(pMax - pMin + 1) + pMin;
    }

}
