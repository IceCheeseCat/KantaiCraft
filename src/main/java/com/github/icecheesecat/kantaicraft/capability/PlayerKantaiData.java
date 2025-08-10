package com.github.icecheesecat.kantaicraft.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerKantaiData implements INBTSerializable<CompoundTag> {

    protected final List<CompoundTag> ships = new ArrayList<>();
    protected final List<CompoundTag> equipments = new ArrayList<>();
    private UUID playerUUID;

    public PlayerKantaiData() {
    }

    public PlayerKantaiData(@NotNull Player player) {
        this.playerUUID = player.getUUID();
    }

    public List<CompoundTag> shipsOfPlayer(Player player) {
        if (player.getUUID().compareTo(this.playerUUID) == 0) {
            return this.ships;
        }

        return null;
    }

    public List<CompoundTag> equipmentsOfPlayer(Player player) {
        if (player.getUUID().compareTo(this.playerUUID) == 0) {
            return this.equipments;
        }

        return null;
    }

    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();

        nbt.putInt("ships", ships.size());
        for (int i = 0; i < ships.size(); i++) {
            nbt.put("ships." + i,  ships.get(i));
        }

        nbt.putInt("equipments", equipments.size());
        for (int i = 0; i < equipments.size(); i++) {
            nbt.put("equipments." + i,  equipments.get(i));
        }

        nbt.putUUID("playeruuid", this.playerUUID);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        int ships_size = nbt.getInt("ships");
        ships.clear();
        for (int i = 0; i < ships_size; i++) {
            ships.add(i, (CompoundTag) nbt.get("ships." + i));
        }

        int equipments_size = nbt.getInt("equipments");
        equipments.clear();
        for (int i = 0; i < equipments_size; i++) {
            equipments.add(i, (CompoundTag) nbt.get("equipments." + i));
        }

        this.playerUUID = nbt.getUUID("playeruuid");
    }
}
