package com.github.icecheesecat.kantaicraft.equipment;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class Equipment implements INBTSerializable<CompoundTag> {

    protected Map<EquipmentStatType, Double> stats = new HashMap<>();
    private int id;
    private Component name;
    private int level;
    private EquipmentType type;

    public static final int MAX_LEVEL = 10;

    public Equipment(Equipment equipment) {
        this.id = equipment.id;
        this.name = equipment.name;
        this.type = equipment.type;
        this.level= equipment.level;
    }

    public Equipment(EquipmentProperties equipmentProperties) {
        this.id = equipmentProperties.getId();
        this.name = equipmentProperties.getComponentName();
        this.type = equipmentProperties.getEquipmentType();
        this.level = 0;
    }

    public double getStat(EquipmentStatType type) {
        return stats.get(type);
    }

    public void setStats(Map<EquipmentStatType, Double> s) {
        this.stats = s;
    }

    public Map<EquipmentStatType, Double> getStats() {
        return this.stats;
    }

    public int getId() {
        return id;
    }

    public Component getName() {
        return this.name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void doLevelUp() {
        this.level++;
    }

    public EquipmentType getType() {
        return this.type;
    }

    public Equipment asCopy() {
        return new Equipment(this);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        var entry_set = stats.entrySet().stream().toList();
        nbt.putInt("stats_size", stats.size());
        for (int i = 0; i < stats.size(); i++) {
            nbt.putInt("equipment_stat_type_" + i, entry_set.get(i).getKey().ordinal());
            nbt.putDouble("equipment_stat_value_" + i, entry_set.get(i).getValue());
        }

        nbt.putInt("id", this.id);
        nbt.putInt("level", this.level);
        nbt.putInt("equipment_type", this.type.ordinal());
        nbt.putString("name", Component.Serializer.toJson(name));

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.stats.clear();
        int stats_size = nbt.getInt("stats_size");
        for (int i = 0; i < stats_size; i++) {
            this.stats.put(
                    EquipmentStatType.get(nbt.getInt("equipment_stat_type_"+i)),
                    nbt.getDouble("equipment_stat_value_"+i)
            );
        }

        this.id = nbt.getInt("id");
        this.level = nbt.getInt("level");
        this.type = EquipmentType.get(nbt.getInt("equipment_type"));
        this.name = Component.Serializer.fromJson(nbt.getString("name"));

    }
}
