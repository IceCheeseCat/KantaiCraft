package com.github.icecheesecat.kantaicraft.entity.brain.sensor;

import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
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

public class MobDropsSensor extends Sensor<EntityShip> {

    public MobDropsSensor() {
        super(1);
    }

    @Override
    protected void doTick(ServerLevel pLevel, EntityShip pEntity) {
        var dropsOptional = pEntity.getBrain().getMemory(ModMemoryModuleType.KILLED_ENTITY_DROPS.get());
        if (dropsOptional.isEmpty()) return;
        var itemEntities = dropsOptional.get();
        eraseRemovedItemThanSort(itemEntities, pEntity);
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(ModMemoryModuleType.KILLED_ENTITY_DROPS.get());
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
}
