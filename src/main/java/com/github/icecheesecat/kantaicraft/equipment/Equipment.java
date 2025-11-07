package com.github.icecheesecat.kantaicraft.equipment;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.INBTSerializable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Equipment implements INBTSerializable<CompoundTag>, GeoAnimatable {
    protected Map<EquipmentStatType, Double> stats;
    private EquipmentProperties equipmentProperties;
    private int level;
    public static final int MAX_LEVEL = 10;
    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);
    private UUID uuid = UUID.randomUUID();
    private EquipmentType equipmentType;

    public Equipment(EquipmentType equipmentType) {
        this(equipmentType, 0);
    }

    public Equipment(EquipmentType equipmentType, int level) {
        this.equipmentType = equipmentType;
        this.equipmentProperties = equipmentType.getEquipmentProperties();
        this.level = level;
        this.stats = new HashMap<>(equipmentType.getDefaultStats());
    }

    public static Equipment makeFromCompoundTag(CompoundTag compoundTag) {
        Equipment equipment = EquipmentManager.createEmptyEquipment();
        equipment.deserializeNBT(compoundTag);
        return equipment;
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
        return equipmentProperties.getName();
    }

    public int getLevel() {
        return level;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void doLevelUp() {
        this.level++;
    }

    public EquipmentClass getEquipmentClass() {
        return this.equipmentProperties.getEquipmentType();
    }

    public boolean isTypeOf(EquipmentClass equipmentClass) {
        return getEquipmentClass() == equipmentClass;
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
        nbt.putUUID("uuid", this.uuid);

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
        this.equipmentProperties = new EquipmentProperties();
        this.equipmentProperties.deserializeNBT(nbt.getCompound("properties"));
        this.uuid = nbt.getUUID("uuid");
        this.equipmentType = EquipmentManager.getEquipmentTypeById(this.equipmentProperties.getId());

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animatableInstanceCache;
    }

    @Override
    public double getTick(Object o) {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Equipment equipment) {
            return equipment.getId() == this.getId() && equipment.level == this.level && this.uuid.equals(equipment.getUuid());
        }

        return false;
    }

    @Override
    public String toString() {
        return "[id=" + this.equipmentProperties.getId() + ", name=" + this.equipmentProperties.getString() + ", level=" + this.level + "]";
    }
}
