package com.github.icecheesecat.kantaicraft.network.packet;

import com.github.icecheesecat.kantaicraft.block.shipyard.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.blueprint.Blueprint;
import com.github.icecheesecat.kantaicraft.util.Constant;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class ShipyardPacket {

    BlockPos pos;
    int[] processTime;
    int[] maxProcessTime;
    NonNullList<UUID> owners;

    public ShipyardPacket(ShipyardBlockEntity shipyardBlockEntity) {
        this.pos = shipyardBlockEntity.getBlockPos();
        this.processTime = new int[shipyardBlockEntity.processShipSize];
        this.maxProcessTime = new int[shipyardBlockEntity.processShipSize];
        for (int i = 0; i < shipyardBlockEntity.processShipSize; i++) {
            this.processTime[i] = shipyardBlockEntity.getProcessAt(i);
            this.maxProcessTime[i] = shipyardBlockEntity.getMaxProcessAt(i);
        }
        this.owners = shipyardBlockEntity.getOwners();
    }

    public ShipyardPacket(BlockPos pos, int[] processTime, int[] maxProcessTime, NonNullList<UUID> owners) {
        this.pos = pos;
        this.processTime = processTime;
        this.maxProcessTime = maxProcessTime;
        this.owners = owners;
    }

    public static void encode(ShipyardPacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.pos);
        buf.writeByteArray(FriendlyByteBufHelper.encodeInt(packet.processTime));
        buf.writeByteArray(FriendlyByteBufHelper.encodeInt(packet.maxProcessTime));
        for (int i = 0; i < packet.processTime.length; i++) {
            buf.writeUUID(packet.owners.get(i));
        }
    }

    public static ShipyardPacket decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        int[] processTime = FriendlyByteBufHelper.decodeInt(buf.readByteArray());
        int[] maxProcessTime = FriendlyByteBufHelper.decodeInt(buf.readByteArray());
        NonNullList<UUID> owners = NonNullList.withSize(processTime.length, Constant.uuidEmpty);
        for (int i = 0; i < processTime.length; i++) {
            owners.set(i, buf.readUUID());
        }

        return new ShipyardPacket(pos, processTime, maxProcessTime, owners);
    }

    public static void handle(ShipyardPacket packet, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {

                BlockEntity be = Minecraft.getInstance().level.getBlockEntity(packet.pos);
                if (be instanceof ShipyardBlockEntity shipyardBlockEntity) {
                    for (int i = 0; i < packet.processTime.length; i++) {
                        shipyardBlockEntity.setProcessTime(i, packet.processTime[i]);
                        shipyardBlockEntity.setMaxProcessTime(i, packet.maxProcessTime[i]);
                        shipyardBlockEntity.setOwners(packet.owners);
                    }
                }

            });
        });

        ctx.get().setPacketHandled(true);

    }

}
