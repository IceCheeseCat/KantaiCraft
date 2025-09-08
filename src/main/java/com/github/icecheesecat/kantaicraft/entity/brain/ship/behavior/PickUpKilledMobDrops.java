package com.github.icecheesecat.kantaicraft.entity.brain.ship.behavior;

import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.item.ItemEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Control Ship to pick up Mob drops they killed.
 * TODO pick up drops nearby if target is out of certain range
 */

public class PickUpKilledMobDrops extends Behavior<EntityShip> {

    private ItemEntity itemEntity;
    private static final int TIMEOUT = 200;
//    private int hasAttackTargetCountdown;
//    private static final int MAX_HAS_ATTACK_TARGET_COUNTDOWN = 5 * 20;
    private WalkTarget walkTarget;
    private boolean stopped;

    public PickUpKilledMobDrops() {
        super(
                ImmutableMap.of(ModMemoryModuleType.KILLED_ENTITY_DROPS.get(), MemoryStatus.VALUE_PRESENT,
                        MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT,
                        MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED,
                        MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED,
                        MemoryModuleType.PATH, MemoryStatus.REGISTERED),
                TIMEOUT);
    }

    @Override
    protected void start(@NotNull ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        this.stopped = false;
        this.walkTarget = new WalkTarget(itemEntity.blockPosition(), 1.0f, 0);
        pEntity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, this.walkTarget);
    }

    @Override
    protected void tick(@NotNull ServerLevel pLevel, @NotNull EntityShip pOwner, long pGameTime) {
        if (this.itemEntity == null || this.itemEntity.isRemoved()) {
            this.stopped = true;
            return;
        }

        double dis = pOwner.distanceTo(itemEntity);
        if (dis < 1.5d) {
            // memory remove same itemEntity
            pOwner.getBrain().getMemory(ModMemoryModuleType.KILLED_ENTITY_DROPS.get()).ifPresent(
                    itemEntities -> {
                        itemEntities.remove(itemEntity);
                    }
            );

            pOwner.shipPickUpItem(this.itemEntity);
            this.stopped = true;
        }

    }

    @Override
    protected void stop(@NotNull ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        this.stopped = true;
        this.itemEntity = null;
        pEntity.getBrain().getMemory(ModMemoryModuleType.KILLED_ENTITY_DROPS.get()).ifPresent(
                itemEntities -> itemEntities.remove(this.itemEntity)
        ); // remove itemEntity from memory (for timeout)
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel pLevel, @NotNull EntityShip pEntity, long pGameTime) {
        if (changedWalkTarget(pEntity)) {
            return false;
        }
        return !this.stopped && this.itemEntity != null && !this.itemEntity.isRemoved();
    }

    private boolean changedWalkTarget(LivingEntity entity) {
        if (entity.getBrain().getMemory(MemoryModuleType.WALK_TARGET).isPresent()) {
            var memWalkTarget = entity.getBrain().getMemory(MemoryModuleType.WALK_TARGET).get();
            return !memWalkTarget.equals(this.walkTarget);
        }

        return true;
    }
    /**
     * Check whether there is room for a {@link net.minecraft.world.item.ItemStack} to insert into the ship inventory
     */
    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel pLevel, EntityShip pOwner) {
        if (!pOwner.hasInventory()) {
            return false;
        }

        var optional = pOwner.getBrain().getMemory(ModMemoryModuleType.KILLED_ENTITY_DROPS.get());
        if (optional.isEmpty()) return false;
        var itemEntities = optional.get();
        if (itemEntities.isEmpty()) return false;

        // check ship inventory has room for the items on the ground
        for (var ele: itemEntities) {
            if (pOwner.getShipInventory().canAddItem(ele.getItem()) && ele.onGround()) {
                this.itemEntity = ele;
                return true;
            }
        }

        return this.itemEntity != null;
    }

}
