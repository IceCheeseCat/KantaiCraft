package com.github.icecheesecat.kantaicraft.entityship.brain.sensor;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DroppedItemSensor extends Sensor<EntityShip> {

    public DroppedItemSensor() {
        super(1);
    }

    @Override
    protected void doTick(ServerLevel pLevel, EntityShip pEntity) {
        if (!pEntity.shouldPickUpItem()) {
            pEntity.getBrain().eraseMemory(ModMemoryModuleType.NEAREST_WANTED_ITEM.get());
            return;
        }
        var dropsOptional = pEntity.getBrain().getMemory(ModMemoryModuleType.ITEMS_TO_PICK_UP.get());
        if (dropsOptional.isEmpty()) return;
        var itemEntities = dropsOptional.get();
        eraseRemovedItemThanSort(itemEntities, pEntity);
        findClosestCanPickUpItem(itemEntities, pEntity);
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(ModMemoryModuleType.ITEMS_TO_PICK_UP.get());
    }

    private void eraseRemovedItemThanSort(List<ItemEntity> itemEntities, Entity owner) {

        List<ItemEntity> removed = new ArrayList<>();
        for (var it: itemEntities) {
            if (it.isRemoved()) {
                removed.add(it);
            }
            else if (it.getItem().isEmpty()) {
                removed.add(it);
            }
        }

        itemEntities.removeAll(removed);
        itemEntities.sort((ie1, ie2) -> {
            if (ie1.distanceTo(owner) < ie2.distanceTo(owner)) return -1;
            else if (ie1.distanceTo(owner) > ie2.distanceTo(owner)) return 1;
            return 0;
        });

    }

    private void findClosestCanPickUpItem(List<ItemEntity> itemEntities, EntityShip pEntity) {
        if (!itemEntities.isEmpty()) {
            // check ship inventory has room for the items on the ground
            for (var ele: itemEntities) {
                if (pEntity.getShipInventory().canAddItem(ele.getItem()) && ele.onGround()) {
                    var closestItem = itemEntities.get(0);
                    pEntity.getBrain().setMemory(ModMemoryModuleType.NEAREST_WANTED_ITEM.get(), closestItem);
                    return;
                }
            }

            // did not find can pick up item
            pEntity.getBrain().eraseMemory(ModMemoryModuleType.NEAREST_WANTED_ITEM.get());
        }
        else {
            pEntity.getBrain().eraseMemory(ModMemoryModuleType.NEAREST_WANTED_ITEM.get());
        }

    }


}
