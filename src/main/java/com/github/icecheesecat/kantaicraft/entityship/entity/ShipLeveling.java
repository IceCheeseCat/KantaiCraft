package com.github.icecheesecat.kantaicraft.entityship.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraftforge.common.util.INBTSerializable;

public class ShipLeveling implements INBTSerializable<CompoundTag> {
    private int MAX_LEVEL = 999;
    private int level = 0;
    private int exp;
    public static final int BASE_REQUIRED = 10;

    public ShipLeveling(int shipLevel, int exp) {
        this.level = shipLevel;
        this.exp = exp;
    }

    public static ShipLeveling levelZero() {
         return new ShipLeveling(0, 0);
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

    public static ShipLeveling create(CompoundTag nbt) {
        var shiplevel = ShipLeveling.levelZero();
        shiplevel.deserializeNBT(nbt);
        return shiplevel;
    }

    // from level 1 to level 100
    public static ShipLeveling createRandom(RandomSource randomSource) {
        int r = (int) (50 * randomSource.nextFloat());
        return new ShipLeveling(r, 0);
    }

    @Override
    public String toString() {
        return "Lv: " + this.level;
    }
}
