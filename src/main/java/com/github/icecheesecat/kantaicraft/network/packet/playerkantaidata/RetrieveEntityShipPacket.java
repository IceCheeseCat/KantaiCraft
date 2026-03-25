package com.github.icecheesecat.kantaicraft.network.packet.playerkantaidata;

import com.github.icecheesecat.kantaicraft.capability.kantaidata.PlayerKantaiDataCapability;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class RetrieveEntityShipPacket {

    private final int playerId;
    private final UUID uuid;

    public RetrieveEntityShipPacket(int playerId, UUID uuid) {
        this.playerId = playerId;
        this.uuid = uuid;
    }

    public static void encode(RetrieveEntityShipPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.playerId);
        buf.writeUUID(packet.uuid);
    }

    public static RetrieveEntityShipPacket decode(FriendlyByteBuf buf) {
        return new RetrieveEntityShipPacket(buf.readInt(), buf.readUUID());
    }

    /**
     *  Sync from server to client
     */
    public static void handle(RetrieveEntityShipPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerLevel serverLevel = ctx.get().getSender().serverLevel();
            if (serverLevel.getEntity(packet.playerId) instanceof Player player) {
                player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(playerKantaiData -> {
                    EntityShip.handleRetrieveFromLevel(serverLevel, packet.uuid, playerKantaiData);
                });
            }
        });

        ctx.get().setPacketHandled(true);
    }

}
