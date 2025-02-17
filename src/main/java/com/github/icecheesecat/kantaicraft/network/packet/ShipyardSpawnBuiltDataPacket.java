package com.github.icecheesecat.kantaicraft.network.packet;

import com.github.icecheesecat.kantaicraft.block.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.block.shipyardUtil.BuiltData;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.item.ShipBlueprintData;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.status.ClientboundPongResponsePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;
import java.util.function.Supplier;

public class ShipyardSpawnBuiltDataPacket {

    BlockPos blockPos;
    UUID uuid;

    public ShipyardSpawnBuiltDataPacket(BlockPos blockPos, UUID uuid) {
        this.blockPos = blockPos;
        this.uuid = uuid;
    }

    public static void encode(ShipyardSpawnBuiltDataPacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.blockPos);
        buf.writeUUID(packet.uuid);
    }

    public static ShipyardSpawnBuiltDataPacket decode(FriendlyByteBuf buf) {

        BlockPos pos = buf.readBlockPos();
        UUID uuid = buf.readUUID();

        return new ShipyardSpawnBuiltDataPacket(pos, uuid);
    }

    public static void handle(ShipyardSpawnBuiltDataPacket packet, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            ServerPlayer serverPlayer = ctx.get().getSender();
            if (serverPlayer.level().getBlockEntity(packet.blockPos) instanceof ShipyardBlockEntity shipyardBlockEntity) {

                int index = shipyardBlockEntity.hasBuildDataAt(packet.uuid);
                if (index == -1) {
                    serverPlayer.sendSystemMessage(Component.literal("Failed to spawn " + packet.uuid));
                }
                else {

                    BuiltData builtData = shipyardBlockEntity.getBuiltData().remove(index);
                    ShipBlueprintData data = builtData.getData();
                    EntityType<? extends BasicEntityShip> entityType = data.getEntityType();
                    BasicEntityShip basicEntityShip = entityType.spawn((ServerLevel) serverPlayer.level(), shipyardBlockEntity.getBlockPos().above(), MobSpawnType.SPAWNER);
                        basicEntityShip.setOwner(data.getBuilder());
                        basicEntityShip.setAmmo(100.0f);
                    serverPlayer.level().addFreshEntity(basicEntityShip);

                }

            }
            else {
                serverPlayer.sendSystemMessage(Component.literal("Failed to find blockEntity"));
            }

        });
        ctx.get().setPacketHandled(true);
    }

}
