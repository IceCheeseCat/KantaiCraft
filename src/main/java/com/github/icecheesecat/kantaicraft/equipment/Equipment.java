package com.github.icecheesecat.kantaicraft.equipment;

import com.github.icecheesecat.kantaicraft.util.ShipFields;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Equipment {

    protected Map<EquipmentStatType, Double> stats = new HashMap<>();
    private int id;
    private String name;
    private int level;
    public static final int MAX_LEVEL = 10;

    public static final Equipment EMPTY = new Equipment(-1, "NONE");

    protected Equipment(Equipment equipment) {
        this(equipment.id, equipment.name);
        this.stats.putAll(equipment.stats);
    }

    public Equipment(int id, String name) {
        this.id = id;
        this.name = name;
        this.level = 0;
    }

    public Equipment addStat(EquipmentStatType type, Double v) {
        this.stats.put(type, v);
        return this;
    }

    public double getStat(EquipmentStatType type) {
        return stats.get(type);
    }

    private void setStats(Map<EquipmentStatType, Double> s) {
        this.stats = s;
    }

    public Map<EquipmentStatType, Double> getStats() {
        return this.stats;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
        return EquipmentType.calculateType(this.id);
    }

    public Equipment asCopy() {
        return new Equipment(this) {};
    }

    public CompoundTag save() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("equipment.id", this.id);

//        List<Integer> statTypes = new ArrayList<>();
//        this.stats.forEach((tpye, value) -> statTypes.add(tpye.ordinal()));
//        nbt.putIntArray("equipment.stattypes", statTypes.stream().mapToInt(Integer::intValue).toArray());
//        this.stats.forEach((type, value) -> nbt.putDouble("equipment." + type.name().toLowerCase(), value));

        nbt.putInt("equipment.level", this.level);

        return nbt;
    }

    public Equipment load(CompoundTag nbt) {
//        int[] statTypes = nbt.getIntArray("equipment.stattypes");
//        for (int i: statTypes) {
//            EquipmentStatType type = EquipmentStatType.get(i);
//            this.stats.put(type, nbt.getDouble("equipment." + type.name().toLowerCase()));
//        }
        this.id = nbt.getInt("equipment.id");
        this.level = nbt.getInt("equipment.level");
        return Equipments.getEquipmentInstanceById(this.id, this.level);
    }
}
