package com.github.icecheesecat.kantaicraft.network.packet.equipment;

import com.github.icecheesecat.kantaicraft.capability.EquipmentHandlerCapability;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.equipment.handler.ArmedEquipment;
import com.github.icecheesecat.kantaicraft.equipment.handler.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.menu.Refreshable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Update EntityShip's EquipmentHandler from server to client
 */
public class EquipmentHandlerPacket {

    int entityId;
    Map<Integer, ArmedEquipment> armedEquipmentMap = new HashMap<>();

    private EquipmentHandlerPacket(int entityId, Map<Integer, ArmedEquipment> armedEquipmentMap) {
        this.entityId = entityId;
        this.armedEquipmentMap = armedEquipmentMap;
    }

    private EquipmentHandlerPacket() {

    }

    public static EquipmentHandlerPacket wholeHandlerPacket(int entityId, EquipmentHandler equipmentHandler) {
        EquipmentHandlerPacket packet = new EquipmentHandlerPacket();
        packet.entityId = entityId;

        for (int i = 0; i < equipmentHandler.getArmedEquipments().size(); i++) {
            packet.armedEquipmentMap.put(i, equipmentHandler.getArmedEquipment(i));
        }

        return packet;
    }

    public static EquipmentHandlerPacket partialHandlerPacket(int entityId, EquipmentHandler equipmentHandler, int... indexes) {
        EquipmentHandlerPacket packet = new EquipmentHandlerPacket();
        packet.entityId = entityId;

        for (int index : indexes) {
            packet.armedEquipmentMap.put(index, equipmentHandler.getArmedEquipment(index));
        }

        return packet;
    }

    public static EquipmentHandlerPacket dirtyHandlerPacket(int entityId, EquipmentHandler equipmentHandler) {
        EquipmentHandlerPacket packet = new EquipmentHandlerPacket();
        packet.entityId = entityId;

        for (int i = 0; i < equipmentHandler.getArmedEquipments().size(); i++) {
            if (equipmentHandler.isDirty(i)) {
                packet.armedEquipmentMap.put(i, equipmentHandler.getArmedEquipment(i));
            }
        }

        return packet;
    }

    public static void encode(EquipmentHandlerPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.entityId);
        buf.writeMap(packet.armedEquipmentMap, FriendlyByteBuf::writeInt, (fb, armed) -> fb.writeNbt(armed.serializeNBT()));
    }

    public static EquipmentHandlerPacket decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        var readMap = buf.readMap(FriendlyByteBuf::readInt, (fb) -> {
            var nbt = fb.readNbt(); assert nbt != null;
            var nArmed = new ArmedEquipment();
            nArmed.deserializeNBT(nbt);
            return nArmed;
        });
        return new EquipmentHandlerPacket(entityId, readMap);
    }

    public static void handle(EquipmentHandlerPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().level == null) return;
            if (Minecraft.getInstance().level.getEntity(packet.entityId) == null) return;
            if (Minecraft.getInstance().level.getEntity(packet.entityId) instanceof EntityShip entityShip) {
                entityShip.getCapability(EquipmentHandlerCapability.TOKEN).ifPresent(equipmentHandler -> {
                    packet.armedEquipmentMap.forEach((equipmentHandler::setOnClient));
                });
            }

            if (Minecraft.getInstance().screen instanceof Refreshable refreshable) {
                refreshable.refresh();
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
