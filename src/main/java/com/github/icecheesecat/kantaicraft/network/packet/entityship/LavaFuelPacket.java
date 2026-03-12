package com.github.icecheesecat.kantaicraft.network.packet.entityship;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LavaFuelPacket {

    int entityId;
    CompoundTag nbt;

    public LavaFuelPacket(int entityId, CompoundTag nbt) {
        this.entityId = entityId;
        this.nbt = nbt;
    }

    public static void encode(LavaFuelPacket packet, FriendlyByteBuf buf) {
        buf.writeVarInt(packet.entityId);
        buf.writeNbt(packet.nbt);
    }

    public static LavaFuelPacket decode(FriendlyByteBuf buf) {
        return new LavaFuelPacket(buf.readVarInt(), buf.readNbt());
    }

    public static void handle(LavaFuelPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {

                ClientLevel level = Minecraft.getInstance().level;
                if (level == null) return;
                if (level.getEntity(packet.entityId) == null) return;
                if (level.getEntity(packet.entityId) instanceof EntityShip entityShip) {
                    entityShip.getLavaFuelCapability().deserializeNBT(packet.nbt);
                }

            });
        });
        ctx.get().setPacketHandled(true);
    }
    
}
