package com.github.icecheesecat.kantaicraft.network.packet;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicCannonShip;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.entity.ship.CannonFireMode;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentProvider;
import com.github.icecheesecat.kantaicraft.equipment.Equipments;
import com.github.icecheesecat.kantaicraft.registries.ModEquipment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncShipC2SPacket {

    SyncType syncType;
    int entityId;
    int index;
    Object value;

    public SyncShipC2SPacket(SyncType type, int entityId, Object value) {
        this.syncType = type;
        this.entityId = entityId;
        this.value = value;
    }

    public SyncShipC2SPacket(SyncType type, int entityId, Object value, int index) {
        this.syncType = type;
        this.entityId = entityId;
        this.value = value;
        this.index = index;
    }

    public static void encode(SyncShipC2SPacket packet, FriendlyByteBuf buf) {
        buf.writeEnum(packet.syncType);
        buf.writeInt(packet.entityId);
        switch (packet.syncType) {
            case GUARD, MELEE -> buf.writeBoolean((Boolean) packet.value);
            case CANNON_FIRE_MODE -> buf.writeEnum((Enum<?>) packet.value);
            case EQUIPMENT -> {
                Equipment equipment = (Equipment) packet.value;
                buf.writeInt(packet.index);
                buf.writeInt(equipment.getId());
                buf.writeInt(equipment.getLevel());
            }
        }

    }

    public static SyncShipC2SPacket decode(FriendlyByteBuf buf) {
        SyncType syncType = buf.readEnum(SyncType.class);
        int entityId = buf.readInt();

        switch (syncType) {
            case GUARD, MELEE -> {
                return new SyncShipC2SPacket(syncType, entityId, buf.readBoolean());
            }
            case CANNON_FIRE_MODE -> {
                return new SyncShipC2SPacket(syncType, entityId, buf.readEnum(CannonFireMode.class));
            }
            case EQUIPMENT -> {
                int index = buf.readInt();
                int id = buf.readInt();
                int level = buf.readInt();
                Equipment n_equipment = Equipments.getEquipmentInstanceById(id);
                n_equipment.setLevel(level);

                return new SyncShipC2SPacket(syncType, entityId, n_equipment, index);
            }
            default -> {
                return null;
            }
        }

    }

    public static void serverHandle(SyncShipC2SPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            SyncType syncType = packet.syncType;
            int entityId = packet.entityId;
            Entity entity = ctx.get().getSender().level().getEntity(entityId);
            if (entity instanceof BasicEntityShip ship) {
                switch (syncType) {
                    case GUARD -> ship.setGuarding((Boolean) packet.value);
                    case MELEE -> ship.setCanMelee((Boolean) packet.value);
                    case CANNON_FIRE_MODE -> {
                        if (ship instanceof BasicCannonShip cannonShip) {
                            cannonShip.setCannonFireMode((CannonFireMode) packet.value);
                        }
                    }
                    case EQUIPMENT -> {
                        ship.getCapability(EquipmentProvider.EQUIPMENT_HANDLER_CAPABILITY).ifPresent(
                                equipmentHandler -> {
                                    equipmentHandler.applyAndRefund(packet.index, (Equipment) packet.value, ship);
                                }
                        );
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
