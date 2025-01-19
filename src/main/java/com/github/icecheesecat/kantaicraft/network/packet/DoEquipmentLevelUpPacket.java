package com.github.icecheesecat.kantaicraft.network.packet;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DoEquipmentLevelUpPacket {

    int entityId;
    byte equipmentIndex;

    public DoEquipmentLevelUpPacket(int entityId, byte equipmentIndex) {
        this.entityId = entityId;
        this.equipmentIndex = equipmentIndex;
    }

    public static void encode(DoEquipmentLevelUpPacket packet, FriendlyByteBuf buf) {

        buf.writeInt(packet.entityId);
        buf.writeByte(packet.equipmentIndex);

    }

    public static DoEquipmentLevelUpPacket decode(FriendlyByteBuf buf) {
        return new DoEquipmentLevelUpPacket(buf.readInt(), buf.readByte());
    }

    public static void handle(DoEquipmentLevelUpPacket packet, Supplier<NetworkEvent.Context> ctx) {

        var context = ctx.get();
        if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER) {
            context.enqueueWork(() -> {
                Entity entity = context.getSender().level().getEntity(packet.entityId);
                if (entity instanceof BasicEntityShip ship) {
                    ship.getCapability(EquipmentProvider.EQUIPMENT_HANDLER_CAPABILITY).ifPresent(
                            equipmentHandler -> {
                                equipmentHandler.getEquipment(packet.equipmentIndex).doLevelUp();
                            }
                    );
                }
            });
            context.setPacketHandled(true);
        }

    }

}
