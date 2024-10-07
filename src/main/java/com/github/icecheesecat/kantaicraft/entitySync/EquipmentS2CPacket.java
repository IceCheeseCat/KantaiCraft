package com.github.icecheesecat.kantaicraft.entitySync;

import com.github.icecheesecat.kantaicraft.entity.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentLevel;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentSlots;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import com.github.icecheesecat.kantaicraft.init.ModShipAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EquipmentS2CPacket {
    final private int entityId;
    final Equipment equipment;
    final private int index;
    final static int ALLOCATE_SIZE = 4 * 5;

    public EquipmentS2CPacket(Equipment equipment, int index, int entityId) {
        this.equipment = equipment;
        this.entityId = entityId;
        this.index = index;
    }

    public static EquipmentS2CPacket Decode(FriendlyByteBuf buf) {
        ByteBuffer buffer = ByteBuffer.wrap(buf.readByteArray());
        buffer.rewind();

        EquipmentLevel level = new EquipmentLevel(buffer.getInt(), buffer.getFloat());
        int uid = buffer.getInt();
        int index = buffer.getInt();
        int entityId = buffer.getInt();

        return new EquipmentS2CPacket(new Equipment(level, uid), index, entityId);
    }

    public static void Encode(EquipmentS2CPacket equipmentS2CPacket, FriendlyByteBuf buf) {
        ByteBuffer buffer = ByteBuffer.allocate(ALLOCATE_SIZE);
        buffer.putInt(equipmentS2CPacket.equipment.getEquipmentLevel().getLevel());
        buffer.putFloat(equipmentS2CPacket.equipment.getEquipmentLevel().getDifficulty());
        buffer.putInt(equipmentS2CPacket.equipment.getUid());
        buffer.putInt(equipmentS2CPacket.index);
        buffer.putInt(equipmentS2CPacket.entityId);
        buffer.rewind();

        buf.writeByteArray(buffer.array());
    }

    public static void handle(EquipmentS2CPacket equipmentS2CPacket, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {

                Entity entity = Minecraft.getInstance().level.getEntity(equipmentS2CPacket.entityId);
                if (entity instanceof BasicEntityShip entityShip) {
                    EquipmentSlots slots = entityShip.getEquipmentSlots();
                    if (slots == null) {
                        List<EquipmentType> types = new ArrayList<>();
                        int size = (int) entityShip.getAttributeValue(ModShipAttributes.SLOT_SIZE.get());
                        for (int i = 0; i < size; i++) {
                            types.add(entityShip.getSlotType(i));
                        }
                        entityShip.setEquipmentSlots(new EquipmentSlots(size, types));
                    }

                    slots.applyAtSlot(equipmentS2CPacket.index, equipmentS2CPacket.equipment);
                }

            });
        });
        ctx.get().setPacketHandled(true);
    }
}


