package com.github.icecheesecat.kantaicraft.event;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicDestroyerShip;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentProvider;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.SyncShipS2CPacket;
import com.github.icecheesecat.kantaicraft.network.packet.SyncType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

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
                    ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new SyncShipS2CPacket(SyncType.EQUIPMENT, ship.getId(), equipment, i));
                }
            });
        }

    }

    @SubscribeEvent
    public static void onAttachingCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof BasicEntityShip ship) {
            if (!ship.getCapability(EquipmentProvider.EQUIPMENT_HANDLER_CAPABILITY).isPresent()) {
                event.addCapability(new ResourceLocation(KantaiCraft.MODID, "properties"), new EquipmentProvider(4, ship.getShipClass().getDefaultEquipment()));
            }
        }
    }

}
