package com.github.icecheesecat.kantaicraft.brain.ship;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;


// TO-DO bug fixing: sometimes can't correctly calculate path to the itemEntity
public class PickUpKilledMobDrops extends Behavior<BasicEntityShip> {

    private ItemEntity itemEntity;
    private static final int TIMEOUT = 200;
    private boolean stopped;

    public PickUpKilledMobDrops() {
        super(
                ImmutableMap.of(ModMemoryModuleType.KILLED_ENTITY_DROPS.get(), MemoryStatus.VALUE_PRESENT,
                        MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED,
                        MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED,
                        MemoryModuleType.PATH, MemoryStatus.REGISTERED,
                        MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT),
                TIMEOUT);
    }

    @Override
    protected void start(ServerLevel pLevel, BasicEntityShip pEntity, long pGameTime) {
//        this.tickReCalcPath = 0;
        this.stopped = false;
        pEntity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(itemEntity, 1.0f, 0));
    }

    @Override
    protected void tick(ServerLevel pLevel, BasicEntityShip pOwner, long pGameTime) {
        if (this.itemEntity == null || this.itemEntity.isRemoved()) {
            this.stopped = true;
            return;
        }

        if (pOwner.distanceTo(itemEntity) < 1.4d) {
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

    }

    @Override
    protected void stop(ServerLevel pLevel, BasicEntityShip pEntity, long pGameTime) {
        this.stopped = true;
        this.itemEntity = null;
    }

    @Override
    protected boolean canStillUse(ServerLevel pLevel, BasicEntityShip pEntity, long pGameTime) {
        return !this.stopped && !pEntity.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET);
    }

    /**
     * Check whether there is room for a {@link net.minecraft.world.item.ItemStack} to insert into the ship inventory
     */
    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, BasicEntityShip pOwner) {
        var optional = pOwner.getBrain().getMemory(ModMemoryModuleType.KILLED_ENTITY_DROPS.get());
        if (optional.isPresent()) {
            var list = optional.get();

            if (list.isEmpty()) return false;
            this.eraseRemovedItem(list); // handle removed itemEntity

            for (var ele: list) {
                if (canInsetToInventory(ele, pOwner) && ele.onGround()) {
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

    // at least part of the itemStack can be inserted into inventory
    private boolean canInsetToInventory(ItemEntity t_itemEntity, BasicEntityShip pOwner) {
        ItemStack originStack = t_itemEntity.getItem();
        ItemStack testStack = t_itemEntity.getItem().copy();

        IItemHandler itemHandler = pOwner.getShipInventory();
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            testStack = itemHandler.insertItem(i, testStack, true);
            if (testStack.getCount() != originStack.getCount()) {
                this.itemEntity = t_itemEntity;
                return true;
            }
        }

        return false;
    }

    private void eraseRemovedItem(List<ItemEntity> itemEntities) {

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

    }
}
