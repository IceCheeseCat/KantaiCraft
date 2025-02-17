package com.github.icecheesecat.kantaicraft.network.packet;

import com.github.icecheesecat.kantaicraft.block.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.block.shipyardUtil.BuiltData;
import com.github.icecheesecat.kantaicraft.item.ShipBlueprintData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ShipyardBuiltDataPacket {

    BlockPos pos;
    List<BuiltData> builtData;

    public ShipyardBuiltDataPacket(BlockPos pos, List<BuiltData> builtData) {
        this.pos = pos;
        this.builtData = builtData;
    }

    public static void encode(ShipyardBuiltDataPacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.pos);
        buf.writeByte(packet.builtData.size());
        for (int i = 0; i < packet.builtData.size(); i++) {
            buf.writeNbt(packet.builtData.get(i).serializeNBT());
        }
    }

    public static ShipyardBuiltDataPacket decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        byte size = buf.readByte();
        List<BuiltData> builtData = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            builtData.add(i, BuiltData.read(buf.readNbt()));
        }

        return new ShipyardBuiltDataPacket(pos, builtData);
    }

    public static void handle(ShipyardBuiltDataPacket packet, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                var be = Minecraft.getInstance().level.getBlockEntity(packet.pos);
                if (be instanceof ShipyardBlockEntity shipyardBlockEntity) {
                    shipyardBlockEntity.clientSetBuiltData(packet.builtData);
                }
            });
        });

        ctx.get().setPacketHandled(true);
    }

}
