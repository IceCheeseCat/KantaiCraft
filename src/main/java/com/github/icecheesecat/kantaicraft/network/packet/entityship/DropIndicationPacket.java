package com.github.icecheesecat.kantaicraft.network.packet.entityship;

import com.github.icecheesecat.kantaicraft.capability.indicateditementities.IndicatedItemEntitiesCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DropIndicationPacket {

    int entityId;

    public DropIndicationPacket(int entityId) {
        this.entityId = entityId;
    }

    public static void encode(DropIndicationPacket packet, FriendlyByteBuf buf) {
        buf.writeVarInt(packet.entityId);
    }

    public static DropIndicationPacket decode(FriendlyByteBuf buf) {
        return new DropIndicationPacket(buf.readVarInt());
    }

    public static void handle(DropIndicationPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                Minecraft.getInstance().level.getCapability(IndicatedItemEntitiesCapability.TOKEN).ifPresent(indicatedItemEntities -> {
                    indicatedItemEntities.addNewInstance(packet.entityId);
                });
            });
        });
        ctx.get().setPacketHandled(true);
    }
    
}
