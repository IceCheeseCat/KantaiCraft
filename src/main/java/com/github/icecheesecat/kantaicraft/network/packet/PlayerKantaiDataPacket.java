package com.github.icecheesecat.kantaicraft.network.packet;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.capability.ClientPlayerKantaiDataCacheCapability;
import com.github.icecheesecat.kantaicraft.playerkantaidata.PlayerKantaiData;
import com.github.icecheesecat.kantaicraft.capability.PlayerKantaiDataCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PlayerKantaiDataPacket {

    PlayerKantaiData playerKantaiData;
    UUID playerUUID;

    public PlayerKantaiDataPacket(UUID playerUUID, PlayerKantaiData data) {
        this.playerUUID = playerUUID;
        this.playerKantaiData = data;
    }

    public static void encode(PlayerKantaiDataPacket packet, FriendlyByteBuf buf) {
        buf.writeUUID(packet.playerUUID);
        buf.writeNbt(packet.playerKantaiData.serializeNBT());
    }

    public static PlayerKantaiDataPacket decode(FriendlyByteBuf buf) {
        UUID playerUUID = buf.readUUID();
        CompoundTag nbt = buf.readNbt();
        PlayerKantaiData data = new PlayerKantaiData(Minecraft.getInstance().player);

        PlayerKantaiDataPacket packet = new PlayerKantaiDataPacket(playerUUID, null);
        if (nbt != null) {
            data.deserializeNBT(nbt);
            packet.playerKantaiData = data;
        }
        else {
            KantaiCraft.LOGGER.warning("PlayerKantaiDataPacket => Player's Kantai data missing. (Can be ignore)");
        }

        return packet;
    }

    /**
     *  Sync {@link PlayerKantaiData} from server to client
     */
    public static void handle(PlayerKantaiDataPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                if (Minecraft.getInstance().level != null) {
                    var clientPlayer = Minecraft.getInstance().level.getPlayerByUUID(packet.playerUUID);
                    if (clientPlayer != null) {
                        clientPlayer.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(playerKantaiData1 -> {

                            playerKantaiData1.setDataOnClient(packet.playerKantaiData);

                            // prepare cache for entity
                            clientPlayer.getCapability(ClientPlayerKantaiDataCacheCapability.TOKEN).ifPresent(cache -> {
                                cache.prepareCache(packet.playerKantaiData);
                            });

                        });
                    }
                }

            });
        });
        ctx.get().setPacketHandled(true);
    }


}
