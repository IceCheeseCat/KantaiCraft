package com.github.icecheesecat.kantaicraft.entity.brain.ship.behavior;

import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.Optional;

public class FollowOwner extends SingleBehaviour<EntityShip> {
    int closeEnough, tooClose;
    public FollowOwner(int cooldown, int closeEnough, int tooClose) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED), cooldown);
        this.closeEnough = closeEnough;
        this.tooClose = tooClose;
    }

    @Override
    protected void singlePerform(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        var optional = playerPositionTracker(pEntity);
        if (!optional.isEmpty()) {
            PositionTracker positiontracker = optional.get();
            if (!pEntity.position().closerThan(positiontracker.currentPosition(), tooClose)) {
                PositionTracker positiontracker1 = optional.get();
                pEntity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, positiontracker1);
                pEntity.getBrain().setMemory(MemoryModuleType.WALK_TARGET,  new WalkTarget(positiontracker1, pEntity.getRunSpeedModifier(), tooClose));
            }
        }
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

}
