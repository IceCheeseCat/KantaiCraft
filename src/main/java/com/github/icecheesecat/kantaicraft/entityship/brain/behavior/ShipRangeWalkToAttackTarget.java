package com.github.icecheesecat.kantaicraft.entityship.brain.behavior;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;

import java.util.Map;

public class ShipRangeWalkToAttackTarget extends SingleBehaviour<EntityShip> {
    public ShipRangeWalkToAttackTarget(int maxCooldown) {
        super(Map.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.PATH, MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleType.LAST_SAW_TARGET_POS.get(), MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleType.CANT_SEE_TARGET_SINCE.get(), MemoryStatus.VALUE_PRESENT), maxCooldown);
    }

    @Override
    protected void singlePerform(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        BlockPos lastSeenPos = pEntity.getBrain().getMemory(ModMemoryModuleType.LAST_SAW_TARGET_POS.get()).get();
        pEntity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(lastSeenPos), pEntity.getRunSpeedModifier(),0));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, EntityShip pOwner) {
        return pOwner.canRangeAttack() && !pOwner.forceMelee();
    }
}
