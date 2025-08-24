package com.github.icecheesecat.kantaicraft.event;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.container.ShipContainer;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;

import java.util.*;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvent {

    @SubscribeEvent
    public static void livingEntityTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level().isClientSide) return;
        if (event.getEntity().level().getGameTime() % 100 != 0) return;
    }

    @SubscribeEvent
    public static void onLivingDeathDrops(LivingDropsEvent event) {

        if (event.getSource().getEntity() instanceof BasicEntityShip ship) {
            ShipContainer inventory = ship.getShipInventory();
            if (inventory == null) return;

            List<ItemEntity> drops = new ArrayList<>(event.getDrops());

            if (!ship.getBrain().hasMemoryValue(ModMemoryModuleType.KILLED_ENTITY_DROPS.get())) {
                 ship.getBrain().setMemory(ModMemoryModuleType.KILLED_ENTITY_DROPS.get(), drops);
            }
            else {
                ship.getBrain().getMemory(ModMemoryModuleType.KILLED_ENTITY_DROPS.get()).ifPresent(
                    itemEntities -> itemEntities.addAll(drops)
                );
            }


        }

    }
}
