package com.github.icecheesecat.kantaicraft.entity.brain.ship.behavior;

import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.Optional;

/**
 * {@link EntityShip} of not hostile follow Owner Player
 */
public class FollowOwner extends SingleBehaviour<EntityShip> {
    int closeEnough, tooClose;
    public FollowOwner(int cooldown, int closeEnough, int tooClose) {
        super(ImmutableMap.of(
                ModMemoryModuleType.IS_PLAYER_SHIP.get(), MemoryStatus.REGISTERED,
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED), cooldown);
        this.closeEnough = closeEnough;
        this.tooClose = tooClose;
    }

    @Override
    protected void singlePerform(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        var optional = playerPositionTracker(pEntity);
        if (!optional.isEmpty()) {
            PositionTracker positiontracker = optional.get();
            if (teleportWhenDistanceGreaterThan(positiontracker.currentPosition(), pEntity.position(), 144.0d)) {
                this.teleportToOwner(pLevel, positiontracker, pEntity);
            }
            else if (!pEntity.position().closerThan(positiontracker.currentPosition(), tooClose)) {
                PositionTracker positiontracker1 = optional.get();
                pEntity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, positiontracker1);
                pEntity.getBrain().setMemory(MemoryModuleType.WALK_TARGET,  new WalkTarget(positiontracker1, pEntity.getRunSpeedModifier(), tooClose));
            }
        }
    }

    protected boolean teleportWhenDistanceGreaterThan(Vec3 playerPosition, Vec3 shipPosition, double distance) {
        return shipPosition.distanceTo(playerPosition) > distance;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, EntityShip pOwner) {
        var playerPositionTracker = this.playerPositionTracker(pOwner);
        return playerPositionTracker.isPresent() && pOwner.distanceToSqr(playerPositionTracker.get().currentPosition()) > this.closeEnough * this.closeEnough;
    }

    private Optional<PositionTracker> playerPositionTracker(LivingEntity livingEntity) {
        if (livingEntity instanceof EntityShip entityShip) {
            if (entityShip.getShipOwner().isPresent()) {
                Player player = entityShip.level().getPlayerByUUID(entityShip.getShipOwner().get());
                if (player != null) {
                    return Optional.of(new BlockPosTracker(player.position()));
                }
            }
        }

        return Optional.empty();
    }

    /**
     * References from {@link FollowOwnerGoal} <p></p>
     * finds a stand able block around player's current blockPos 3 * 3 * 3
     */
    private void teleportToOwner(Level level, PositionTracker playerPosition, EntityShip entityShip) {
        BlockPos blockpos = playerPosition.currentBlockPosition();

        for(int i = 0; i < 10; ++i) {
            int j = this.randomIntInclusive(-3, 3, entityShip);
            int k = this.randomIntInclusive(-1, 1, entityShip);
            int l = this.randomIntInclusive(-3, 3, entityShip);
            boolean flag = this.maybeTeleportTo(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l, level, playerPosition, entityShip);
            if (flag) {
                return;
            }
        }

    }

    private boolean maybeTeleportTo(int pX, int pY, int pZ, Level level, PositionTracker playerPosition, EntityShip entityShip) {
        if (Math.abs((double)pX - playerPosition.currentPosition().x) < 2.0D && Math.abs((double)pZ - playerPosition.currentPosition().z) < 2.0D) {
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

    private int randomIntInclusive(int pMin, int pMax, EntityShip ship) {
        return ship.getRandom().nextInt(pMax - pMin + 1) + pMin;
    }

}
