package com.github.icecheesecat.kantaicraft.network.packet;

import com.github.icecheesecat.kantaicraft.capability.EquipmentHandlerCapability;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentManager;
import com.github.icecheesecat.kantaicraft.menu.ship.ShipScreen;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class TogglePlayerShipPacket {

    SyncType syncType;
    int entityId;
    byte index;
    Object value;

    public TogglePlayerShipPacket(SyncType type, int entityId, Object value) {
        this.syncType = type;
        this.entityId = entityId;
        this.value = value;
    }

    public TogglePlayerShipPacket(SyncType type, int entityId, Object value, byte index) {
        this.syncType = type;
        this.entityId = entityId;
        this.value = value;
        this.index = index;
    }

    public static void encode(TogglePlayerShipPacket packet, FriendlyByteBuf buf) {
        buf.writeEnum(packet.syncType);
        buf.writeInt(packet.entityId);
        switch (packet.syncType) {
            case GUARD, MELEE -> buf.writeBoolean((Boolean) packet.value);
        }

    }

    public static TogglePlayerShipPacket decode(FriendlyByteBuf buf) {
        SyncType syncType = buf.readEnum(SyncType.class);
        int entityId = buf.readInt();

        switch (syncType) {
            case GUARD, MELEE -> {
                return new TogglePlayerShipPacket(syncType, entityId, buf.readBoolean());
            }
            default -> {
                return null;
            }
        }

    }

    public static void handle(TogglePlayerShipPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                SyncType syncType = packet.syncType;
                int entityId = packet.entityId;
                Entity entity = ctx.get().getSender().level().getEntity(entityId);
                if (entity instanceof EntityShip entityShip) {
                    switch (syncType) {
                        case GUARD -> entityShip.setGuarding((Boolean) packet.value);
                        case MELEE -> entityShip.setForceMelee((Boolean) packet.value);
                    }
                }
            }

        });
        ctx.get().setPacketHandled(true);
    }

}
