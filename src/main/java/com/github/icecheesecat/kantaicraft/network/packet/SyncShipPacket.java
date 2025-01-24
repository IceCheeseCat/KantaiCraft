package com.github.icecheesecat.kantaicraft.network.packet;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicCannonShip;
import com.github.icecheesecat.kantaicraft.entity.ship.CannonFireMode;
import com.github.icecheesecat.kantaicraft.capability.EquipmentProvider;
import com.github.icecheesecat.kantaicraft.equipment.Equipments;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class SyncShipPacket {

    SyncType syncType;
    int entityId;
    byte index;
    Object value;

    public SyncShipPacket(SyncType type, int entityId, Object value) {
        this.syncType = type;
        this.entityId = entityId;
        this.value = value;
    }

    public SyncShipPacket(SyncType type, int entityId, Object value, byte index) {
        this.syncType = type;
        this.entityId = entityId;
        this.value = value;
        this.index = index;
    }

    public static void encode(SyncShipPacket packet, FriendlyByteBuf buf) {
        buf.writeEnum(packet.syncType);
        buf.writeInt(packet.entityId);
        switch (packet.syncType) {
            case GUARD, MELEE -> buf.writeBoolean((Boolean) packet.value);
            case CANNON_FIRE_MODE -> buf.writeEnum((Enum<?>) packet.value);
            case EQUIPMENT -> {
                Equipment equipment = (Equipment) packet.value;
                buf.writeInt(equipment.getId());
                buf.writeInt(equipment.getLevel());
                buf.writeByte(packet.index);
            }
            case LEVEL_UP_EQUIPMENT -> {
                buf.writeInt((Integer) packet.value);
                buf.writeByte(packet.index);
            }
        }

    }

    public static SyncShipPacket decode(FriendlyByteBuf buf) {
        SyncType syncType = buf.readEnum(SyncType.class);
        int entityId = buf.readInt();

        switch (syncType) {
            case GUARD, MELEE -> {
                return new SyncShipPacket(syncType, entityId, buf.readBoolean());
            }
            case CANNON_FIRE_MODE -> {
                return new SyncShipPacket(syncType, entityId, buf.readEnum(CannonFireMode.class));
            }
            case EQUIPMENT -> {
                int id = buf.readInt();
                int level = buf.readInt();
                byte index = buf.readByte();
                Equipment n_equipment = Equipments.getEquipmentInstanceById(id, level);

                return new SyncShipPacket(syncType, entityId, n_equipment, index);
            }
            case LEVEL_UP_EQUIPMENT -> {
                return new SyncShipPacket(syncType, entityId, buf.readInt(), buf.readByte());
            }
            default -> {
                return null;
            }
        }

    }

    public static void handle(SyncShipPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
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
            }
            else if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
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
                                        if (equipmentHandler.canApplyAtSlot(packet.index, (Equipment) packet.value, ship)) {
                                            equipmentHandler.setEquipment(packet.index, (Equipment) packet.value, ship);
                                            ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()), new SyncShipPacket(SyncType.EQUIPMENT, ship.getId(), packet.value, packet.index));
                                        }
                                        else {
                                            System.err.println("apply equipment at ship = " + ship + " failed.");
                                        }
                                    }
                            );
                        }
                        case LEVEL_UP_EQUIPMENT -> {
                            ship.getCapability(EquipmentProvider.EQUIPMENT_HANDLER_CAPABILITY).ifPresent(
                                    equipmentHandler -> {
                                        equipmentHandler.getEquipment(packet.index).doLevelUp();
                                        ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()), new SyncShipPacket(SyncType.EQUIPMENT, ship.getId(), equipmentHandler.getEquipment(packet.index), packet.index));
                                    }
                            );
                        }
                    }
                }
            }

        });
        ctx.get().setPacketHandled(true);
    }

}
