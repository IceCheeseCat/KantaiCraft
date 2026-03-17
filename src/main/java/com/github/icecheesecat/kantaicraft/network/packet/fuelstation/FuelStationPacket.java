package com.github.icecheesecat.kantaicraft.network.packet.fuelstation;

import com.github.icecheesecat.kantaicraft.block.facilities.fuelstation.FuelStationBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FuelStationPacket {

    private final FuelStationBlockEntity.State state;
    private final BlockPos blockPos;

    public FuelStationPacket(FuelStationBlockEntity blockEntity) {
        this.blockPos = blockEntity.getBlockPos();
        this.state = blockEntity.getState();
    }

    private FuelStationPacket(BlockPos blockPos, FuelStationBlockEntity.State state) {
        this.state = state;
        this.blockPos = blockPos;
    }

    public static void encode(FuelStationPacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.blockPos);
        buf.writeEnum(packet.state);
    }

    public static FuelStationPacket decode(FriendlyByteBuf buf) {
        return new FuelStationPacket(buf.readBlockPos(), buf.readEnum(FuelStationBlockEntity.State.class));
    }

    // handle summon to server level
    public static void handle(FuelStationPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                if (Minecraft.getInstance().level.getBlockEntity(packet.blockPos) instanceof FuelStationBlockEntity fuelStationBlockEntity) {
                    fuelStationBlockEntity.setState(packet.state);
                }
            });

        });

        ctx.get().setPacketHandled(true);
    }



}
