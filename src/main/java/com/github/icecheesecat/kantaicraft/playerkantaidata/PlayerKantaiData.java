package com.github.icecheesecat.kantaicraft.playerkantaidata;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.playerkantaidata.PlayerKantaiDataPacket;
import com.github.icecheesecat.kantaicraft.util.CompoundTagHelper;
import com.github.icecheesecat.kantaicraft.util.SerializedLivingEntity;
import net.minecraft.core.BlockPos;
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
    protected final List<SerializedLivingEntity> onDutyShips = new ArrayList<>();
    protected final List<Equipment> equipments = new ArrayList<>();
    private Player player;

    public PlayerKantaiData(Player player) {
        this.player = player;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();

        CompoundTagHelper.serializeList(nbt, "onDutyShips", this.onDutyShips, (SerializedLivingEntity::serializeNBT));
        CompoundTagHelper.serializeList(nbt, "inDockShips", this.inDockShips, (SerializedLivingEntity::serializeNBT));
        CompoundTagHelper.serializeList(nbt, "equipments", this.equipments, (Equipment::serializeNBT));

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

        onDutyShips.clear();
        inDockShips.clear();
        equipments.clear();

        onDutyShips.addAll(CompoundTagHelper.deserializeList(nbt, "onDutyShips", SerializedLivingEntity::createFromNbt));
        inDockShips.addAll(CompoundTagHelper.deserializeList(nbt, "inDockShips", SerializedLivingEntity::createFromNbt));
        equipments.addAll(CompoundTagHelper.deserializeList(nbt, "equipments", Equipment::makeFromCompoundTag));

    }

    public void setData(PlayerKantaiData data) {
        this.inDockShips.clear();
        this.onDutyShips.clear();
        this.equipments.clear();
        this.inDockShips.addAll(data.inDockShips);
        this.onDutyShips.addAll(data.onDutyShips);
        this.equipments.addAll(data.equipments);
    }

    public void addShipInDock(EntityShip entityShip) {
        this.inDockShips.add(new SerializedLivingEntity(entityShip));
        updateToClient();
    }

    public void addShipInDock(EntityType<?> entityType, ServerLevel serverLevel) {
        var entityShip = (EntityShip) entityType.create(serverLevel);
        this.addShipInDock(entityShip);
        updateToClient();
    }

    public void addShipOnDuty(EntityShip entityShip) {
        this.onDutyShips.add(new SerializedLivingEntity(entityShip));
        updateToClient();
    }

    private void removeShipInDock(UUID uuid) {
        var optional = this.inDockShips.stream().filter(serializedLivingEntity -> serializedLivingEntity.getUuid().equals(uuid)).findFirst();
        if (optional.isPresent()) {
            this.inDockShips.remove(optional.get());
            updateToClient();
        }
    }

    public void removeShipOnDuty(UUID uuid) {
        var optional = this.onDutyShips.stream().filter(serializedLivingEntity -> serializedLivingEntity.getUuid().equals(uuid)).findFirst();
        if (optional.isPresent()) {
            this.onDutyShips.remove(optional.get());
            updateToClient();
        }
    }

    public void addEquipment(Equipment equipment) {
        this.equipments.add(equipment);
        updateToClient();
    }

    public List<SerializedLivingEntity> getInDockShips() {
        return inDockShips;
    }

    public List<SerializedLivingEntity> getOnDutyShips() {
        return onDutyShips;
    }

    public Optional<SerializedLivingEntity> getSerializedEntityShipByUUID(UUID uuid) {
        return this.inDockShips.stream().filter(serializedLivingEntity -> serializedLivingEntity.getUuid().equals(uuid)).findFirst();
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public Player getPlayer() {
        return player;
    }

    protected void updateToClient() {
        ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new PlayerKantaiDataPacket(player.getId(), this));
    }

    public void summonToLevel(ServerPlayer player, SerializedLivingEntity ses, BlockPos summonLocation) {
        EntityShip entityShip = (EntityShip) ses.getEntityType().create(player.level());
        if (entityShip == null) return;
        entityShip.setPos(summonLocation.getX() + 0.5f, summonLocation.getY(), summonLocation.getZ() + 0.5f);
        entityShip.setShipOwner(player.getUUID());
        player.level().addFreshEntity(entityShip);
        this.removeShipInDock(ses.getUuid());
    }

    public String debugString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("On duty ships: ").append("\n");
        for (int i = 0; i < this.onDutyShips.size(); i++) {
            stringBuilder.append("  ").append(i).append(" -> ").append(this.onDutyShips.get(i)).append("\n");
        }

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
