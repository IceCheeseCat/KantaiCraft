package com.github.icecheesecat.kantaicraft.network.packet.commandcenter;

import com.github.icecheesecat.kantaicraft.capability.kantaidata.PlayerKantaiDataCapability;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.util.SerializedLivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * Summon the target entity from command center request
 */
public class DispatchEntityShipPacket {

    UUID summonUUID;
    BlockPos summonLocation;

    public DispatchEntityShipPacket(UUID summonUUID, BlockPos summonLocation) {
        this.summonUUID = summonUUID;
        this.summonLocation = summonLocation;
    }

    public static void encode(DispatchEntityShipPacket packet, FriendlyByteBuf buf) {
        buf.writeUUID(packet.summonUUID);
        buf.writeBlockPos(packet.summonLocation);
    }

    public static DispatchEntityShipPacket decode(FriendlyByteBuf buf) {
        return new DispatchEntityShipPacket(buf.readUUID(), buf.readBlockPos());
    }

    // handle summon to server level
    public static void handle(DispatchEntityShipPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayer serverPlayer = ctx.get().getSender();
            serverPlayer.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
                    playerKantaiData -> {
                        var optional = playerKantaiData.removeShipInDock(packet.summonUUID);
                        if (optional.isPresent()) {
                            summonToLevel(serverPlayer, optional.get(), packet.summonLocation);
                        }
                    }
            );


        });

        ctx.get().setPacketHandled(true);
    }

    public static void summonToLevel(ServerPlayer player, SerializedLivingEntity ses, BlockPos summonLocation) {
        EntityShip entityShip = (EntityShip) ses.getEntityType().create(player.level());
        if (entityShip == null) return;
        entityShip.load(ses.getEntityTag());
        entityShip.setShipOwner(player.getUUID());
        entityShip.setPos(summonLocation.getX() + 0.5f, summonLocation.getY(), summonLocation.getZ() + 0.5f);
        player.level().addFreshEntity(entityShip);
    }

}
