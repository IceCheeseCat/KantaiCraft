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

public class Equipment implements INBTSerializable<CompoundTag>, GeoAnimatable {

    public static final Equipment EMPTY = new Equipment(EquipmentProperties.EMPTY);
    protected Map<EquipmentStatType, Double> stats = new HashMap<>();
    private EquipmentProperties equipmentProperties;
    private int level;
    public static final int MAX_LEVEL = 10;
    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    public Equipment() {
    }

    public Equipment(Equipment equipment) {
        this.equipmentProperties = equipment.equipmentProperties;
        this.level= equipment.level;
        this.stats = new HashMap<>(equipment.stats);
    }

    public Equipment(EquipmentProperties equipmentProperties) {
        this.equipmentProperties = equipmentProperties;
        this.level = 0;
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
        this.equipmentProperties = new EquipmentProperties();
        this.equipmentProperties.deserializeNBT(nbt.getCompound("properties"));

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
}
