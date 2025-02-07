package com.github.icecheesecat.kantaicraft.network.packet;

import com.github.icecheesecat.kantaicraft.config.ConfigEquipmentData;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipments;
import com.github.icecheesecat.kantaicraft.menu.screen.EquipmentWidget;
import com.github.icecheesecat.kantaicraft.menu.screen.SelectionWidget;
import com.github.icecheesecat.kantaicraft.menu.screen.ShipScreen;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class C2SEquipmentOptionsPacket {

    int entityId;
    int index;
    int equipmentId;

    public C2SEquipmentOptionsPacket(int entityId, int index, int equipmentId) {
        this.entityId = entityId;
        this.index = index;
        this.equipmentId = equipmentId;
    }

    public static void encode(C2SEquipmentOptionsPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.entityId);
        buf.writeInt(packet.index);
        buf.writeInt(packet.equipmentId);
    }

    public static C2SEquipmentOptionsPacket decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        int index = buf.readInt();
        int equipmentId= buf.readInt();

        return new C2SEquipmentOptionsPacket(entityId, index, equipmentId);
    }

    // ask from client what equipments are available
    public static void handle(C2SEquipmentOptionsPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();

            List<Integer> ids = (List<Integer>) ConfigEquipmentData.getEquipmentById(packet.equipmentId);
            ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> sender), new S2CEquipmentOptionsPacket(packet.entityId, packet.index, ids));
        });

        ctx.get().setPacketHandled(true);
    }
    
}
