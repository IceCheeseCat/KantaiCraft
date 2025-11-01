package com.github.icecheesecat.kantaicraft.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerKantaiData implements INBTSerializable<CompoundTag> {

    protected final List<UUID> ships = new ArrayList<>();
    protected final List<CompoundTag> equipments = new ArrayList<>();

    public PlayerKantaiData() {
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();

        nbt.putInt("ships", ships.size());
        for (int i = 0; i < ships.size(); i++) {
            nbt.putUUID("ships." + i,  ships.get(i));
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
            ships.add(i, nbt.getUUID("ships." + i));
        }

        int equipments_size = nbt.getInt("equipments");
        equipments.clear();
        for (int i = 0; i < equipments_size; i++) {
            equipments.add(i, (CompoundTag) nbt.get("equipments." + i));
        }

    }

    public void iterateOverShips(Consumer<UUID> consumer) {
        this.ships.forEach(consumer);
    }

    public void setDataOnClient(PlayerKantaiData data) {
        this.ships.clear();
        this.equipments.clear();
        this.ships.addAll(data.ships);
        this.equipments.addAll(data.equipments);
    }
}
