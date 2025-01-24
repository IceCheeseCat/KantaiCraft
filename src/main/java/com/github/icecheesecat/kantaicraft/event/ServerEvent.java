package com.github.icecheesecat.kantaicraft.event;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.capability.EquipmentProvider;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.SyncShipPacket;
import com.github.icecheesecat.kantaicraft.network.packet.SyncType;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvent {

    @SubscribeEvent
    public static void livingEntityTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level().isClientSide) return;
        Entity entity = event.getEntity();
        if (entity instanceof BasicEntityShip ship && ship.level().getGameTime() % 200 == 0) {
            ship.getCapability(EquipmentProvider.EQUIPMENT_HANDLER_CAPABILITY).ifPresent((handler) -> {
                for (int i = 0; i < handler.getSlotSize(); i++) {
                    Equipment equipment = handler.getEquipment(i);
                    ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new SyncShipPacket(SyncType.EQUIPMENT, ship.getId(), equipment, (byte) i));
                }
            });
        }

    }

    static long FIVE_MINUTE = 20 * 60 * 5;
    @SubscribeEvent
    public static void onLivingDeathDrops(LivingDropsEvent event) {

        if (event.getSource().getEntity() instanceof BasicEntityShip ship) {
            IItemHandler handler = ship.getShipInventory();
            if (handler == null) return;

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



    @SubscribeEvent
    public static void onAttachingCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof BasicEntityShip ship) {
            if (!ship.getCapability(EquipmentProvider.EQUIPMENT_HANDLER_CAPABILITY).isPresent()) {
                event.addCapability(new ResourceLocation(KantaiCraft.MODID, "properties"), new EquipmentProvider(4));
            }
        }
    }

}
