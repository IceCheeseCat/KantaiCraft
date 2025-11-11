package com.github.icecheesecat.kantaicraft.network.packet;

import com.github.icecheesecat.kantaicraft.capability.PlayerKantaiDataCapability;
import com.github.icecheesecat.kantaicraft.capability.SerializedEntityShip;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * Summon the target entity from command center request
 */
public class CommandCenterRequestSummonPacket {

    UUID summonUUID;
    BlockPos summonLocation;

    public CommandCenterRequestSummonPacket(UUID summonUUID, BlockPos summonLocation) {
        this.summonUUID = summonUUID;
        this.summonLocation = summonLocation;
    }

    public static void encode(CommandCenterRequestSummonPacket packet, FriendlyByteBuf buf) {
        buf.writeUUID(packet.summonUUID);
        buf.writeBlockPos(packet.summonLocation);
    }

    public static CommandCenterRequestSummonPacket decode(FriendlyByteBuf buf) {
        return new CommandCenterRequestSummonPacket(buf.readUUID(), buf.readBlockPos());
    }

    // handle summon to server level
    public static void handle(CommandCenterRequestSummonPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayer serverPlayer = ctx.get().getSender();
            serverPlayer.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
                    playerKantaiData -> {
                        var ses = playerKantaiData.getSerializedEntityShipByUUID(packet.summonUUID);
                        if (ses.isEmpty()) return;
                        playerKantaiData.summonToLevel(serverPlayer, ses.get(), packet.summonLocation);
                    }
            );


        });

        ctx.get().setPacketHandled(true);
    }
}
