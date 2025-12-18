package com.github.icecheesecat.kantaicraft.network.packet.shipyard;

import com.github.icecheesecat.kantaicraft.block.shipyard.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.blueprint.Blueprint;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ShipyardSpawnEntityPacket {

    BlockPos blockPos;
    int index;

    public ShipyardSpawnEntityPacket(BlockPos blockPos, int index) {
        this.blockPos = blockPos;
        this.index = index;
    }

    public static void encode(ShipyardSpawnEntityPacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.blockPos);
        buf.writeInt(packet.index);
    }

    public static ShipyardSpawnEntityPacket decode(FriendlyByteBuf buf) {

        BlockPos pos = buf.readBlockPos();
        int index = buf.readInt();

        return new ShipyardSpawnEntityPacket(pos, index);
    }

    public static void handle(ShipyardSpawnEntityPacket packet, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            ServerPlayer serverPlayer = ctx.get().getSender();
            UUID playerUUID = serverPlayer.getUUID();
            if (serverPlayer.level().getBlockEntity(packet.blockPos) instanceof ShipyardBlockEntity shipyardBlockEntity) {

                if (!shipyardBlockEntity.hasCompletedBuilding(packet.index))  {
                    serverPlayer.sendSystemMessage(Component.literal("Building has not completed"));
                    return;
                }
                if (shipyardBlockEntity.getOwners().get(packet.index).compareTo(playerUUID) != 0) {
                    serverPlayer.sendSystemMessage(Component.literal("This is not your ship"));
                    return;
                }

                shipyardSpawn(packet.index, shipyardBlockEntity, serverPlayer);
                shipyardBlockEntity.removeProcessedItem(packet.index);

            }
            else {
                serverPlayer.sendSystemMessage(Component.literal("Failed to find blockEntity"));
            }

        });
        ctx.get().setPacketHandled(true);
    }

    private static void shipyardSpawn(int index, ShipyardBlockEntity shipyardBlockEntity, ServerPlayer serverPlayer) {
        var blueprint = Blueprint.createFromTag(shipyardBlockEntity.getItem(index).getTag());
        var entityTypeOptional = blueprint.getEntityType();
        if (entityTypeOptional.isPresent()) {
            var entityType = entityTypeOptional.get();
            EntityShip entityShip = (EntityShip) entityType.spawn((ServerLevel) serverPlayer.level(), shipyardBlockEntity.getBlockPos().above(), MobSpawnType.SPAWN_EGG);
            if (entityShip != null) {
                entityShip.setShipOwner(shipyardBlockEntity.getOwners().get(index));
            }
        }

    }

}
