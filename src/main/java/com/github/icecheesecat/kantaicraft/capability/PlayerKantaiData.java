package com.github.icecheesecat.kantaicraft.capability;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.PlayerKantaiDataPacket;
import com.github.icecheesecat.kantaicraft.network.packet.PlayerKantaiDataUpdatedPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PlayerKantaiData implements INBTSerializable<CompoundTag> {

    private Player player;
    protected final List<SerializedEntityShip> ships = new ArrayList<>();
    protected final List<CompoundTag> equipments = new ArrayList<>();

    public PlayerKantaiData(Player player) {
        this.player = player;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();

        nbt.putInt("ships", ships.size());
        for (int i = 0; i < ships.size(); i++) {
            nbt.put("ships." + i,  ships.get(i).serializeNBT());
        }

        nbt.putInt("equipments", equipments.size());
        for (int i = 0; i < equipments.size(); i++) {
            nbt.put("equipments." + i,  equipments.get(i));
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

        int ships_size = nbt.getInt("ships");
        ships.clear();
        for (int i = 0; i < ships_size; i++) {
            ships.add(i, SerializedEntityShip.createFromNbt(nbt.getCompound("ships." + i)));
        }

        int equipments_size = nbt.getInt("equipments");
        equipments.clear();
        for (int i = 0; i < equipments_size; i++) {
            equipments.add(i, nbt.getCompound("equipments." + i));
        }

    }

    public void setDataOnClient(PlayerKantaiData data) {
        this.ships.clear();
        this.equipments.clear();
        this.ships.addAll(data.ships);
        this.equipments.addAll(data.equipments);
    }

    public void addShip(EntityShip entityShip) {
        this.ships.add(new SerializedEntityShip(entityShip));
        updateToClient();
    }

    public void addShip(EntityType<?> entityType, ServerLevel serverLevel) {
        var entityShip = (EntityShip) entityType.create(serverLevel);
        this.addShip(entityShip);
        updateToClient();
    }

    public void removeShip(UUID uuid) {
        var optional = this.ships.stream().filter(serializedEntityShip -> serializedEntityShip.getUuid().equals(uuid)).findFirst();
        if (optional.isPresent()) {
            this.ships.remove(optional.get());
            updateToClient();
        }
    }

    public void addEquipment(CompoundTag nbt) {
        this.equipments.add(nbt);
        updateToClient();
    }

    public List<SerializedEntityShip> getShips() {
        return ships;
    }

    public Optional<SerializedEntityShip> getSerializedEntityShipByUUID(UUID uuid) {
        return this.ships.stream().filter(serializedEntityShip -> serializedEntityShip.getUuid().equals(uuid)).findFirst();
    }

    public List<CompoundTag> getEquipments() {
        return equipments;
    }

    protected void updateToClient() {
        ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new PlayerKantaiDataUpdatedPacket());
    }

}
