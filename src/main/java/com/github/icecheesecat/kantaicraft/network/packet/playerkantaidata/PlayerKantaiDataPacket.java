package com.github.icecheesecat.kantaicraft.network.packet.playerkantaidata;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.capability.kantaidata.PlayerKantaiDataCapability;
import com.github.icecheesecat.kantaicraft.menu.Refreshable;
import com.github.icecheesecat.kantaicraft.playerkantaidata.PlayerKantaiData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerKantaiDataPacket {

    private int playerEntityId;
    private PlayerKantaiData playerKantaiData;

    public PlayerKantaiDataPacket(int playerEntityId, PlayerKantaiData data) {
        this.playerEntityId = playerEntityId;
        this.playerKantaiData = data;
    }

    public static void encode(PlayerKantaiDataPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.playerEntityId);
        buf.writeNbt(packet.playerKantaiData.serializeNBT());
    }

    public static PlayerKantaiDataPacket decode(FriendlyByteBuf buf) {
        int playerEntityId = buf.readInt();
        CompoundTag nbt = buf.readNbt();
        PlayerKantaiData data;
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.getEntity(playerEntityId) instanceof Player player) {
            data = new PlayerKantaiData(player);
        }
        else {
            data = new PlayerKantaiData(null);
        }

        PlayerKantaiDataPacket packet = new PlayerKantaiDataPacket(playerEntityId, null);
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
                    var clientPlayer = packet.playerKantaiData.getPlayer();
                    if (clientPlayer != null) {
                        clientPlayer.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(playerKantaiData1 -> {

                            playerKantaiData1.setData(packet.playerKantaiData);
                            if (Minecraft.getInstance().screen instanceof Refreshable refreshable) {
                                refreshable.refresh();
                            }

                        });
                    }
                }

            });
        });
        ctx.get().setPacketHandled(true);
    }


}
