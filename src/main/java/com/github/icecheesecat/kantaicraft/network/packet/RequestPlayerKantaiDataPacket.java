package com.github.icecheesecat.kantaicraft.network.packet;

import com.github.icecheesecat.kantaicraft.capability.PlayerKantaiDataCapability;
import com.github.icecheesecat.kantaicraft.config.ConfigEquipmentTree;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class RequestPlayerKantaiDataPacket {

    UUID playerUUID;

    public RequestPlayerKantaiDataPacket(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public static void encode(RequestPlayerKantaiDataPacket packet, FriendlyByteBuf buf) {
        buf.writeUUID(packet.playerUUID);
    }

    public static RequestPlayerKantaiDataPacket decode(FriendlyByteBuf buf) {
        return new RequestPlayerKantaiDataPacket(buf.readUUID());
    }

    // request from client
    public static void handle(RequestPlayerKantaiDataPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            if (ctx.get().getSender().serverLevel() != null) {
                if (ctx.get().getSender().serverLevel().getPlayerByUUID(packet.playerUUID) != null) {
                    ctx.get().getSender().getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
                        playerKantaiData -> {
                            ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()), new PlayerKantaiDataPacket(packet.playerUUID, playerKantaiData));
                        }
                    );
                }
            }

        });

        ctx.get().setPacketHandled(true);
    }

}
