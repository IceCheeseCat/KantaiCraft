package com.github.icecheesecat.kantaicraft.equipment;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class Equipment implements INBTSerializable<CompoundTag> {

    public static final Equipment EMPTY = new Equipment(EquipmentProperties.EMPTY, new DefaultValue());
    protected Map<EquipmentStatType, Double> stats = new HashMap<>();
//    private int id;
//    private Component name;
//    private EquipmentType type;
    private EquipmentProperties equipmentProperties;
    private int level;
    private final DefaultValue defaultValue;

    public static final int MAX_LEVEL = 10;

    public Equipment(Equipment equipment) {
        this.equipmentProperties = equipment.equipmentProperties;
        this.level= equipment.level;
        this.stats = new HashMap<>(equipment.stats);
        this.defaultValue = equipment.defaultValue;
    }

    public Equipment(EquipmentProperties equipmentProperties, DefaultValue defaultValue) {
        this.equipmentProperties = equipmentProperties;
        this.level = 0;
        this.defaultValue = defaultValue;
    }

    public double getStat(EquipmentStatType type) {
        return stats.get(type);
    }

    public Equipment setStats(Map<EquipmentStatType, Double> s) {
        this.stats = s;
        return this;
    }

    public Map<EquipmentStatType, Double> getStats() {
        return this.stats;
    }

    public int getId() {
        return equipmentProperties.getId();
    }

    public Component getName() {
        return equipmentProperties.getComponentName();
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
        return this.equipmentProperties.getEquipmentType();
    }

    public Equipment asCopy() {
        return new Equipment(this);
    }

    public DefaultValue defaultValue() {
        return this.defaultValue;
    }

    public boolean isTypeOf(EquipmentType type) {
        return getType() == type;
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

        nbt.putInt("level", this.level);
        nbt.put("properties", this.equipmentProperties.serializeNBT());

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

        this.level = nbt.getInt("level");
        this.equipmentProperties.deserializeNBT(nbt.getCompound("properties"));

    }
}
