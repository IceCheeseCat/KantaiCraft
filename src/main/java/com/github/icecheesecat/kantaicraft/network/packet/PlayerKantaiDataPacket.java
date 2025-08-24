package com.github.icecheesecat.kantaicraft.network.packet;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.capability.PlayerKantaiData;
import com.github.icecheesecat.kantaicraft.network.Cache.Cache;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PlayerKantaiDataPacket {

    PlayerKantaiData playerKantaiData;
    UUID playerUUID;

    public PlayerKantaiDataPacket(PlayerKantaiData data) {
        this.playerKantaiData = data;
    }

    public static void encode(PlayerKantaiDataPacket packet, FriendlyByteBuf buf) {
        buf.writeNbt(packet.playerKantaiData.serializeNBT());
        buf.writeUUID(packet.playerUUID);
    }

    public static PlayerKantaiDataPacket decode(FriendlyByteBuf buf) {
        PlayerKantaiData data = new PlayerKantaiData();
        PlayerKantaiDataPacket packet = new PlayerKantaiDataPacket(null);
        var nbt = buf.readNbt();
        var uuid = buf.readUUID();
        if (nbt != null) {
            data.deserializeNBT(nbt);
            packet.playerKantaiData = data;
        }
        else {
            KantaiCraft.LOGGER.warning("PlayerKantaiDataPacket => Player's Kantai data missing. (Can be ignore)");
        }

        if (uuid != null) {
            packet.playerUUID = uuid;
        }
        else {
            KantaiCraft.LOGGER.warning("PlayerKantaiDataPacket => Packet owner uuid missing. (Can be ignore)");
        }

        return packet;
    }

    // Server to client
    public static void handle(PlayerKantaiDataPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                PlayerKantaiData playerKantaiData1 = packet.playerKantaiData;
                Cache.allPlayerKantaiDataCache.setPlayerKantaiDataCache(playerKantaiData1, packet.playerUUID);
            });
        });
        ctx.get().setPacketHandled(true);
    }


}
