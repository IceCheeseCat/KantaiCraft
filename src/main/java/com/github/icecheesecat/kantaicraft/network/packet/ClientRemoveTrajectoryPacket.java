package com.github.icecheesecat.kantaicraft.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientRemoveTrajectoryPacket {

    int id;

    public ClientRemoveTrajectoryPacket(int id) {
        this.id = id;
    }

    public static void encode(ClientRemoveTrajectoryPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.id);
    }

    public static ClientRemoveTrajectoryPacket decode(FriendlyByteBuf buf) {
        return new ClientRemoveTrajectoryPacket(buf.readInt());
    }

    public static void handle(ClientRemoveTrajectoryPacket packet, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                TrajectoryPacket.ClientTrajectoryHandler.removeTrajectory(packet.id);
                ctx.get().setPacketHandled(true);
            });
        });

    }

}
