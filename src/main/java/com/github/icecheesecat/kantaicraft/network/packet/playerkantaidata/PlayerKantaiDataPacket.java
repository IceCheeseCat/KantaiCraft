package com.github.icecheesecat.kantaicraft.network.packet.playerkantaidata;

import com.github.icecheesecat.kantaicraft.capability.kantaidata.PlayerKantaiDataCapability;
import com.github.icecheesecat.kantaicraft.menu.Refreshable;
import com.github.icecheesecat.kantaicraft.playerkantaidata.PlayerKantaiData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class PlayerKantaiDataPacket {

    private int m_playerEntityId;
    private CompoundTag m_data;

    public PlayerKantaiDataPacket(int p_playerEntityId, PlayerKantaiData p_data) {
        this(p_playerEntityId, p_data.serializeNBT());
    }

    private PlayerKantaiDataPacket(int p_playerEntityId, CompoundTag p_data) {
        setPlayerEntityId(p_playerEntityId);
        setData(p_data);
    }

    public int getPlayerEntityId() {
        return m_playerEntityId;
    }

    public void setPlayerEntityId(int p_playerEntityId) {
        m_playerEntityId = p_playerEntityId;
    }

    public CompoundTag getData() {
        return m_data.copy();
    }

    public void setData(CompoundTag p_data) {
        m_data = Objects.requireNonNull(p_data).copy();
    }

    public static void encode(PlayerKantaiDataPacket p_packet, FriendlyByteBuf p_buf) {
        p_buf.writeInt(p_packet.getPlayerEntityId());
        p_buf.writeNbt(p_packet.getData());
    }

    public static PlayerKantaiDataPacket decode(FriendlyByteBuf p_buf) {
        int playerEntityId = p_buf.readInt();
        CompoundTag data = p_buf.readNbt();

        return new PlayerKantaiDataPacket(playerEntityId, data);
    }

    public static void handle(PlayerKantaiDataPacket p_packet, Supplier<NetworkEvent.Context> p_context) {
        p_context.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> applyToClient(p_packet)));
        p_context.get().setPacketHandled(true);
    }

    private static void applyToClient(PlayerKantaiDataPacket p_packet) {
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;

        if (player == null || player.getId() != p_packet.getPlayerEntityId()) return;

        player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(p_data -> {
            p_data.deserializeNBT(p_packet.getData());
            if (minecraft.screen instanceof Refreshable refreshable) {
                refreshable.refresh();
            }
        });
    }

    public static class Request {

        private int m_playerId;

        public Request(int p_playerId) {
            setPlayerId(p_playerId);
        }

        public int getPlayerId() {
            return m_playerId;
        }

        public void setPlayerId(int p_playerId) {
            m_playerId = p_playerId;
        }

        public static void encode(Request p_packet, FriendlyByteBuf p_buf) {
            p_buf.writeInt(p_packet.getPlayerId());
        }

        public static Request decode(FriendlyByteBuf p_buf) {
            return new Request(p_buf.readInt());
        }

        public static void handle(Request p_packet, Supplier<NetworkEvent.Context> p_context) {
            p_context.get().enqueueWork(() -> {
                var sender = p_context.get().getSender();

                // Keep the legacy ID on the wire, but only the authenticated sender may request their data.
                if (sender == null) return;

                sender.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(PlayerKantaiData::updateToSelf);
            });
            p_context.get().setPacketHandled(true);
        }
    }
}
