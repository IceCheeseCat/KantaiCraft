package com.github.icecheesecat.kantaicraft.network.packet;

import com.github.icecheesecat.kantaicraft.capability.EquipmentHandlerCapability;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.equipment.handler.ArmedEquipment;
import com.github.icecheesecat.kantaicraft.equipment.handler.EquipmentHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Update EntityShip's EquipmentHandler from server to client
 */
public class S2CEquipmentHandlerPacket {

    int entityId;
    Map<Integer, ArmedEquipment> armedEquipmentMap = new HashMap<>();

    private S2CEquipmentHandlerPacket(int entityId, Map<Integer, ArmedEquipment> armedEquipmentMap) {
        this.entityId = entityId;
        this.armedEquipmentMap = armedEquipmentMap;
    }

    private S2CEquipmentHandlerPacket() {

    }

    public static S2CEquipmentHandlerPacket wholeHandlerPacket(int entityId, EquipmentHandler equipmentHandler) {
        S2CEquipmentHandlerPacket packet = new S2CEquipmentHandlerPacket();
        packet.entityId = entityId;

        for (int i = 0; i < equipmentHandler.getArmedEquipments().size(); i++) {
            packet.armedEquipmentMap.put(i, equipmentHandler.getArmedEquipment(i));
        }

        return packet;
    }

    public static S2CEquipmentHandlerPacket partialHandlerPacket(int entityId, EquipmentHandler equipmentHandler, int... indexes) {
        S2CEquipmentHandlerPacket packet = new S2CEquipmentHandlerPacket();
        packet.entityId = entityId;

        for (int index : indexes) {
            packet.armedEquipmentMap.put(index, equipmentHandler.getArmedEquipment(index));
        }

        return packet;
    }

    public static S2CEquipmentHandlerPacket dirtyHandlerPacket(int entityId, EquipmentHandler equipmentHandler) {
        S2CEquipmentHandlerPacket packet = new S2CEquipmentHandlerPacket();
        packet.entityId = entityId;

        for (int i = 0; i < equipmentHandler.getArmedEquipments().size(); i++) {
            if (equipmentHandler.isDirty(i)) {
                packet.armedEquipmentMap.put(i, equipmentHandler.getArmedEquipment(i));
            }
        }

        return packet;
    }

    public static void encode(S2CEquipmentHandlerPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.entityId);
        buf.writeMap(packet.armedEquipmentMap, FriendlyByteBuf::writeInt, (fb, armed) -> fb.writeNbt(armed.serializeNBT()));
    }

    public static S2CEquipmentHandlerPacket decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        var readMap = buf.readMap(FriendlyByteBuf::readInt, (fb) -> {
            var nbt = fb.readNbt(); assert nbt != null;
            var nArmed = new ArmedEquipment();
            nArmed.deserializeNBT(nbt);
            return nArmed;
        });
        return new S2CEquipmentHandlerPacket(entityId, readMap);
    }

    public static void handle(S2CEquipmentHandlerPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().level == null) return;
            if (Minecraft.getInstance().level.getEntity(packet.entityId) == null) return;
            if (Minecraft.getInstance().level.getEntity(packet.entityId) instanceof EntityShip entityShip) {
                entityShip.getCapability(EquipmentHandlerCapability.TOKEN).ifPresent(equipmentHandler -> {
                    packet.armedEquipmentMap.forEach((equipmentHandler::setOnClient));
                });
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
