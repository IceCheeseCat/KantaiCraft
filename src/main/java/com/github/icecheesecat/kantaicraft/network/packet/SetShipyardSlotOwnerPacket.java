package com.github.icecheesecat.kantaicraft.network.packet;

import com.github.icecheesecat.kantaicraft.block.shipyard.ShipyardBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SetShipyardSlotOwnerPacket {

    BlockPos blockPos;
    int index;

    public SetShipyardSlotOwnerPacket(int index, BlockPos blockPos) {
        this.blockPos = blockPos;
        this.index = index;
    }

    public static void encode(SetShipyardSlotOwnerPacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.blockPos);
        buf.writeInt(packet.index);
    }

    public static SetShipyardSlotOwnerPacket decode(FriendlyByteBuf buf) {
        BlockPos blockpos = buf.readBlockPos();
        int index = buf.readInt();

        return new SetShipyardSlotOwnerPacket(index, blockpos);
    }

    public static void handle(SetShipyardSlotOwnerPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayer serverPlayer = ctx.get().getSender();
            UUID playerUUID = serverPlayer.getUUID();
            if (serverPlayer.level().getBlockEntity(packet.blockPos) instanceof ShipyardBlockEntity shipyardBlockEntity) {
                if (!shipyardBlockEntity.setOwnerAt(packet.index, playerUUID)) {
                    serverPlayer.sendSystemMessage(Component.literal("Failed to set owner on " + shipyardBlockEntity + " at slot " + packet.index));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
