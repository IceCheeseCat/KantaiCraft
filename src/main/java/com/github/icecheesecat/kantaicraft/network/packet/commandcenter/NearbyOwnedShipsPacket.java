package com.github.icecheesecat.kantaicraft.network.packet.commandcenter;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.menu.commandcenter.CommandCenterScreen;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class NearbyOwnedShipsPacket {

    final List<Integer> entitiesId;

    public NearbyOwnedShipsPacket(List<Integer> entitiesId) {
        this.entitiesId = entitiesId;
    }

    public static void encode(NearbyOwnedShipsPacket packet, FriendlyByteBuf buf) {
        buf.writeCollection(packet.entitiesId, FriendlyByteBuf::writeInt);
    }

    public static NearbyOwnedShipsPacket decode(FriendlyByteBuf buf) {
        return new NearbyOwnedShipsPacket(buf.readCollection(ArrayList::new, FriendlyByteBuf::readInt));
    }

    public static void handle(NearbyOwnedShipsPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                if (Minecraft.getInstance().screen instanceof CommandCenterScreen commandCenterScreen) {
                    commandCenterScreen.setNearbyShips(packet.entitiesId);
                    commandCenterScreen.refresh();
                }
            });

        });

        ctx.get().setPacketHandled(true);
    }

    public static class Request {
        final BlockPos blockPos;
        final int findAreaSize;

        public Request(BlockPos blockPos, int findAreaSize) {
            this.blockPos = blockPos;
            this.findAreaSize = findAreaSize;
        }

        public static void encode(Request packet, FriendlyByteBuf buf) {
            buf.writeBlockPos(packet.blockPos);
            buf.writeInt(packet.findAreaSize);
        }

        public static Request decode(FriendlyByteBuf buf) {
            return new Request(buf.readBlockPos(), buf.readInt());
        }

        public static void handle(Request packet, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {

                AABB findBox = AABB.ofSize(packet.blockPos.getCenter(), packet.findAreaSize, packet.findAreaSize, packet.findAreaSize);
                List<Integer> foundEntitiesId = ctx.get().getSender().level().getEntitiesOfClass(EntityShip.class, findBox, entityShip -> entityShip.isShipOwner(ctx.get().getSender())).stream().map(entityShip -> entityShip.getId()).toList();

                ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()), new NearbyOwnedShipsPacket(foundEntitiesId));
            });

            ctx.get().setPacketHandled(true);
        }
    }

}
