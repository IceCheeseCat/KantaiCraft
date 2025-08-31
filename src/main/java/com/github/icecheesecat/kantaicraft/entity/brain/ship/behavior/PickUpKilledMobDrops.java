package com.github.icecheesecat.kantaicraft.entity.brain.ship.behavior;

import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Control Ship to pick up Mob drops they killed.
 * TODO pick up drops nearby if target is out of certain range
 */

public class PickUpKilledMobDrops extends Behavior<EntityShip> {

    private ItemEntity itemEntity;
    private static final int TIMEOUT = 200;
    private int hasAttackTargetCountdown;
    private static final int MAX_HAS_ATTACK_TARGET_COUNTDOWN = 5 * 20;
    private WalkTarget walkTarget;
    private boolean stopped;

    public PickUpKilledMobDrops() {
        super(
                ImmutableMap.of(ModMemoryModuleType.KILLED_ENTITY_DROPS.get(), MemoryStatus.VALUE_PRESENT,
                        MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED,
                        MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED,
                        MemoryModuleType.PATH, MemoryStatus.REGISTERED),
                TIMEOUT);
        this.hasAttackTargetCountdown = MAX_HAS_ATTACK_TARGET_COUNTDOWN;
    }

    @Override
    protected void start(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
//        this.tickReCalcPath = 0;
        this.stopped = false;
        this.walkTarget = new WalkTarget(itemEntity.blockPosition(), 1.0f, 0);
        pEntity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, this.walkTarget);
    }

    @Override
    protected void tick(ServerLevel pLevel, EntityShip pOwner, long pGameTime) {
        if (this.itemEntity == null || this.itemEntity.isRemoved()) {
            this.stopped = true;
            return;
        }

        double dis = pOwner.distanceTo(itemEntity);
        if (dis < 1.5d) {
            // memory remove same itemEntity
            pOwner.getBrain().getMemory(ModMemoryModuleType.KILLED_ENTITY_DROPS.get()).ifPresent(
                    itemEntities -> {
                        if (itemEntities.contains(itemEntity)) {
                            itemEntities.remove(itemEntity);
                        }
                    }
            );

            pOwner.shipPickUpItem(this.itemEntity);
            this.stopped = true;
        }

//        Path path;
//        if (pOwner.getBrain().getMemory(MemoryModuleType.PATH).isPresent()) {
//            path = pOwner.getBrain().getMemory(MemoryModuleType.PATH).get();
//            System.out.println(path);
//            for (var node: path.getClosedSet()) {
//                System.out.println(" " + node.asBlockPos());
//            }
//        }

    }

    @Override
    protected void stop(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        this.stopped = true;
        this.itemEntity = null;
    }

    @Override
    protected boolean canStillUse(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        if (pEntity.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
            this.hasAttackTargetCountdown = MAX_HAS_ATTACK_TARGET_COUNTDOWN;
            return false;
        }
        if (changedWalkTarget(pEntity)) {
            return false;
        }
        return !this.stopped && this.itemEntity != null && !this.itemEntity.isRemoved();
    }

    @Override
    protected boolean timedOut(long pGameTime) {
        return false;
    }

    private boolean changedWalkTarget(LivingEntity entity) {
        if (entity.getBrain().getMemory(MemoryModuleType.WALK_TARGET).isPresent()) {
            var memWalkTarget = entity.getBrain().getMemory(MemoryModuleType.WALK_TARGET).get();
            if (memWalkTarget.equals(this.walkTarget)) {
                return false;
            }
        }

        return true;
    }
    /**
     * Check whether there is room for a {@link net.minecraft.world.item.ItemStack} to insert into the ship inventory
     */
    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, EntityShip pOwner) {
        if (!pOwner.hasInventory()) {
            return false;
        }
        if (pOwner.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
            this.hasAttackTargetCountdown = MAX_HAS_ATTACK_TARGET_COUNTDOWN;
            return false;
        }
        if (this.hasAttackTargetCountdown > 0) {
            this.hasAttackTargetCountdown--;
            return false;
        }

        var optional = pOwner.getBrain().getMemory(ModMemoryModuleType.KILLED_ENTITY_DROPS.get());
        if (optional.isPresent()) {
            var list = optional.get();

            if (list.isEmpty()) return false;
            this.eraseRemovedItemThanSort(list, pOwner); // handle removed itemEntity

            // check ship inventory has room for the items on the ground
            for (var ele: list) {
                if (pOwner.getShipInventory().canAddItem(ele.getItem()) && ele.onGround()) {
                    this.itemEntity = ele;
                    return true;
                }
            }

            if (this.itemEntity == null) {
                return false;
            }

        }

        return false;
    }

    private void eraseRemovedItemThanSort(List<ItemEntity> itemEntities, Entity owner) {

        List<ItemEntity> removed = new ArrayList<>();
        for (var itemEntity: itemEntities) {
            if (itemEntity == null) {
                removed.add(itemEntity);
            }
            else if (itemEntity.isRemoved()) {
                removed.add(itemEntity);
            }
            else if (itemEntity.getItem().isEmpty()) {
                removed.add(itemEntity);
            }
        }

        itemEntities.removeAll(removed);
        itemEntities.sort((ie1, ie2) -> {
            if (ie1.distanceTo(owner) < ie2.distanceTo(owner)) return -1;
            else if (ie1.distanceTo(owner) > ie2.distanceTo(owner)) return 1;
            return 0;
        });

    }
}
