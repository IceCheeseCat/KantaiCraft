package com.github.icecheesecat.kantaicraft.playerkantaidata;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.playerkantaidata.PlayerKantaiDataPacket;
import com.github.icecheesecat.kantaicraft.util.CompoundTagHelper;
import com.github.icecheesecat.kantaicraft.util.SerializedLivingEntity;
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

    protected final List<SerializedLivingEntity> inDockShips = new ArrayList<>();
    protected final List<Equipment> equipments = new ArrayList<>();
    private PlayerExpeditionData m_playerExpeditionData = new PlayerExpeditionData();
    private Player player;

    public PlayerKantaiData(Player player) {
        this.player = player;
    }

    public PlayerExpeditionData getPlayerExpeditionData() {
        return m_playerExpeditionData;
    }

    public void setPlayerExpeditionData(PlayerExpeditionData p_playerExpeditionData) {
        m_playerExpeditionData = p_playerExpeditionData;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();

        CompoundTagHelper.serializeList(nbt, "inDockShips", this.inDockShips, (SerializedLivingEntity::serializeNBT));
        CompoundTagHelper.serializeList(nbt, "equipments", this.equipments, (Equipment::serializeNBT));
        nbt.put("playerExpeditionData", getPlayerExpeditionData().serializeNBT());

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

        inDockShips.clear();
        equipments.clear();

        inDockShips.addAll(CompoundTagHelper.deserializeList(nbt, "inDockShips", SerializedLivingEntity::createFromNbt));
        equipments.addAll(CompoundTagHelper.deserializeList(nbt, "equipments", Equipment::makeFromCompoundTag));

        setPlayerExpeditionData(new PlayerExpeditionData());
        getPlayerExpeditionData().deserializeNBT(nbt.getCompound("playerExpeditionData"));
    }

    public void setData(PlayerKantaiData data) {
        this.inDockShips.clear();
        this.equipments.clear();
        this.inDockShips.addAll(data.inDockShips);
        this.equipments.addAll(data.equipments);
        setPlayerExpeditionData(data.getPlayerExpeditionData().copy());
    }

    public void addShipInDock(EntityShip entityShip) {
        var serialized = new SerializedLivingEntity(entityShip);
        if (this.inDockShips.contains(serialized)) {
            return;
        }
        this.inDockShips.add(serialized);
        updateToSelf();
    }

    public void addShipInDock(EntityType<?> entityType, ServerLevel serverLevel) {
        var entityShip = (EntityShip) entityType.create(serverLevel);
        this.addShipInDock(entityShip);
    }

    public Optional<SerializedLivingEntity> removeShipInDock(UUID uuid) {
        var optional = this.inDockShips.stream().filter(serializedLivingEntity -> serializedLivingEntity.getUuid().equals(uuid)).findFirst();
        if (optional.isPresent()) {
            this.inDockShips.remove(optional.get());
            updateToSelf();
            return optional;
        }

        return Optional.empty();
    }

    public void addEquipment(Equipment equipment) {
        this.equipments.add(equipment);
        updateToSelf();
    }

    public List<SerializedLivingEntity> getInDockShips() {
        return inDockShips;
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public Player getPlayer() {
        return player;
    }

    public void updateToSelf() {
        ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new PlayerKantaiDataPacket(player.getId(), this));
    }

    public String debugString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("In dock ships: ").append("\n");
        for (int i = 0; i < this.inDockShips.size(); i++) {
            stringBuilder.append("  ").append(i).append(" -> ").append(this.inDockShips.get(i)).append("\n");
        }

        stringBuilder.append("Equipments: ").append("\n");
        for (int i = 0; i < this.equipments.size(); i++) {
            stringBuilder.append("  ").append(i).append(" -> ").append(this.equipments.get(i)).append("\n");
        }

        return stringBuilder.toString();

    }

}
