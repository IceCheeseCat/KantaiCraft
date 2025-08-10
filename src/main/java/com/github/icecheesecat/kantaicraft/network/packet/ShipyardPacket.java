package com.github.icecheesecat.kantaicraft.network.packet;

import com.github.icecheesecat.kantaicraft.block.shipyard.ShipyardBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShipyardPacket {

    BlockPos pos;
    byte index;
    int processTime;
    int totalProcessTime;
    boolean syncTotalProcessTime;


    public ShipyardPacket(BlockPos blockPos, byte index, int processTime, int totalProcessTime, boolean syncTotalProcessTime) {
        this.pos = blockPos;
        this.index = index;
        this.processTime = processTime;
        this.totalProcessTime = totalProcessTime;
        this.syncTotalProcessTime = syncTotalProcessTime;
    }

    public ShipyardPacket(BlockPos pos, byte index, int processTime) {
        this.pos = pos;
        this.index = index;
        this.processTime = processTime;
    }

    public static void encode(ShipyardPacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.pos);
        buf.writeByte(packet.index);
        buf.writeInt(packet.processTime);
        buf.writeBoolean(packet.syncTotalProcessTime);
        if (packet.syncTotalProcessTime) buf.writeInt(packet.totalProcessTime);
    }

    public static ShipyardPacket decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        byte index = buf.readByte();
        int processTime = buf.readInt();
        boolean syncTotal = buf.readBoolean();
        int totalProcessTime = -1;
        if (syncTotal) {
             totalProcessTime = buf.readInt();
        }


        return new ShipyardPacket(pos, index, processTime, totalProcessTime, syncTotal);
    }

    public static void handle(ShipyardPacket packet, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {

                BlockEntity be = Minecraft.getInstance().level.getBlockEntity(packet.pos);
                if (be instanceof ShipyardBlockEntity shipyardBlockEntity) {
                    shipyardBlockEntity.setProcessTime(packet.index, packet.processTime);
                    if (packet.syncTotalProcessTime) {
                        shipyardBlockEntity.setTotalProcessTime(packet.index, packet.totalProcessTime);
                    }
                }

            });
        });

        ctx.get().setPacketHandled(true);

    }

}
