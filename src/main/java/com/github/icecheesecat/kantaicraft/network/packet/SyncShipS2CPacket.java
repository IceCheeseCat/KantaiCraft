package com.github.icecheesecat.kantaicraft.network.packet;

import com.github.icecheesecat.kantaicraft.equipment.EquipmentProvider;
import com.github.icecheesecat.kantaicraft.equipment.Equipments;
import com.github.icecheesecat.kantaicraft.registries.ModEquipment;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncShipS2CPacket {

    SyncType syncType;
    int entityId;
    int index;
    Object value;

    public SyncShipS2CPacket(SyncType type, int entityId, Object value) {
        this.syncType = type;
        this.entityId = entityId;
        this.value = value;
    }

    public SyncShipS2CPacket(SyncType type, int entityId, Object value, int index) {
        this.syncType = type;
        this.entityId = entityId;
        this.value = value;
        this.index = index;
    }

    public static void encode(SyncShipS2CPacket packet, FriendlyByteBuf buf) {
        buf.writeEnum(packet.syncType);
        buf.writeInt(packet.entityId);
        switch (packet.syncType) {
            case GUARD -> buf.writeBoolean((Boolean) packet.value);
            case EQUIPMENT -> {
                Equipment equipment = (Equipment) packet.value;
                buf.writeInt(packet.index);
                buf.writeInt(equipment.getId());
                buf.writeInt(equipment.getLevel());
            }
        }

    }

    public static SyncShipS2CPacket decode(FriendlyByteBuf buf) {
        SyncType syncType = buf.readEnum(SyncType.class);
        int entityId = buf.readInt();

        switch (syncType) {
            case GUARD -> {
                return new SyncShipS2CPacket(syncType, entityId, buf.readBoolean());
            }
            case EQUIPMENT -> {
                int index = buf.readInt();
                int id = buf.readInt();
                int level = buf.readInt();
                Equipment n_equipment = Equipments.getEquipmentInstanceById(id);
                n_equipment.setLevel(level);

                return new SyncShipS2CPacket(syncType, entityId, n_equipment, index);
            }
            default -> {
                return null;
            }
        }

    }

    public static void clientHandle(SyncShipS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId);
                if (entity instanceof BasicEntityShip ship) {
                    switch (packet.syncType) {
                        case EQUIPMENT -> {
                            ship.getCapability(EquipmentProvider.EQUIPMENT_HANDLER_CAPABILITY).ifPresent(equipmentHandler -> {
                                equipmentHandler.setOnClient(packet.index, (Equipment) packet.value);;
                            });
                        }
                    }
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }

}
