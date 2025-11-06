package com.github.icecheesecat.kantaicraft.network.packet;

import com.github.icecheesecat.kantaicraft.capability.PlayerKantaiDataCapability;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * Send from server to client, then request packet send back to server for data
 */
public class PlayerKantaiDataUpdatedPacket {

    public PlayerKantaiDataUpdatedPacket() {
    }

    public static void encode(PlayerKantaiDataUpdatedPacket packet, FriendlyByteBuf buf) {
    }

    public static PlayerKantaiDataUpdatedPacket decode(FriendlyByteBuf buf) {
        return new PlayerKantaiDataUpdatedPacket();
    }

    // request from client
    public static void handle(PlayerKantaiDataUpdatedPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ModPacketHandler.INSTANCE.sendToServer(new RequestPlayerKantaiDataPacket(Minecraft.getInstance().player.getUUID()));
            });

        });

        ctx.get().setPacketHandled(true);
    }




}