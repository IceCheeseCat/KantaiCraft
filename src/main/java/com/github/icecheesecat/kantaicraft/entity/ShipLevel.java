package com.github.icecheesecat.kantaicraft.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class ShipLevel implements INBTSerializable<CompoundTag> {
    private int MAX_LEVEL;
    private int level = 0;
    private int exp;
    public static final int BASE_REQUIRED = 10;

    public ShipLevel(int MAX_LEVEL, int shipLevel, int exp) {
        this.MAX_LEVEL = MAX_LEVEL;
        this.level = shipLevel;
        this.exp = exp;
    }

    public static ShipLevel levelZero() {
         return new ShipLevel(9999, 0, 0);
    }

    // TODO could improve the levelup check algorithm
    public void addExp(int gainedExp) {
        this.exp += gainedExp;
        while (this.exp >= levelUpRequiredExp() && this.level <= MAX_LEVEL) {
            this.exp %= levelUpRequiredExp();
            this.level++;
        }
    }

    public void addLevel(int shipLevel) {
        this.level += shipLevel;
    }

    private int levelUpRequiredExp() {
        return (int) (Math.pow(this.level, 1.2) + BASE_REQUIRED);
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("level", this.level);
        nbt.putInt("exp", this.exp);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.level = nbt.getInt("level");
        this.exp = nbt.getInt("exp");
    }
}
