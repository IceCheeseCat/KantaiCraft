package com.github.icecheesecat.kantaicraft.network.packet.equipment;

import com.github.icecheesecat.kantaicraft.config.ConfigEquipmentTree;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.function.Supplier;

public class RequestEquipmentOptionsPacket {

    int entityId;
    int index;
    int equipmentId;

    public RequestEquipmentOptionsPacket(int entityId, int index, int equipmentId) {
        this.entityId = entityId;
        this.index = index;
        this.equipmentId = equipmentId;
    }

    public static void encode(RequestEquipmentOptionsPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.entityId);
        buf.writeInt(packet.index);
        buf.writeInt(packet.equipmentId);
    }

    public static RequestEquipmentOptionsPacket decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        int index = buf.readInt();
        int equipmentId= buf.readInt();

        return new RequestEquipmentOptionsPacket(entityId, index, equipmentId);
    }

    // ask from client what equipments are available
    public static void handle(RequestEquipmentOptionsPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();

            List<Integer> ids = (List<Integer>) ConfigEquipmentTree.getEquipmentById(packet.equipmentId);
            ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> sender), new EquipmentOptionsPacket(packet.entityId, packet.index, ids));
        });

        ctx.get().setPacketHandled(true);
    }
    
}
