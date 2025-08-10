package com.github.icecheesecat.kantaicraft.network.packet;

import com.github.icecheesecat.kantaicraft.menu.ship.ShipScreen;
import com.github.icecheesecat.kantaicraft.network.Cache.Cache;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class S2CEquipmentOptionsPacket {

    int entityId;
    int index;
    List<Integer> validEquipmentIds;

    public S2CEquipmentOptionsPacket(int entityId, int index, List<Integer> validEquipmentIds) {
        this.entityId = entityId;
        this.index = index;
        this.validEquipmentIds = validEquipmentIds;
    }

    public static void encode(S2CEquipmentOptionsPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.entityId);
        buf.writeInt(packet.index);
        buf.writeInt(packet.validEquipmentIds.size());
        for (int i = 0; i < packet.validEquipmentIds.size(); i++) {
            buf.writeInt(packet.validEquipmentIds.get(i));
        }
    }

    public static S2CEquipmentOptionsPacket decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        int index = buf.readInt();
        int size = buf.readInt();
        List<Integer> vei = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            vei.add(buf.readInt());
        }

        return new S2CEquipmentOptionsPacket(entityId, index, vei);
    }

    public static void handle(S2CEquipmentOptionsPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                if (Minecraft.getInstance().screen instanceof ShipScreen screen) {

                    Cache.selectionCache = packet.validEquipmentIds;
                    Cache.selectionIndexCache = packet.index;
                    Cache.selectionEntityId = packet.entityId;

                    screen.setSelectDirty(true);

                }
            });
        });
    }

}
