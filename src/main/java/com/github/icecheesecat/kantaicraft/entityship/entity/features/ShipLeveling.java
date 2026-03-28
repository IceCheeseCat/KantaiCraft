package com.github.icecheesecat.kantaicraft.entityship.entity.features;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraftforge.common.util.INBTSerializable;

public class ShipLeveling implements INBTSerializable<CompoundTag> {
    private int MAX_LEVEL = 999;
    private int level = 0;
    private int exp;
    public static final int BASE_REQUIRED = 10;
    private boolean dirty = true;

    private ShipLeveling(int shipLevel, int exp) {
        this.level = shipLevel;
        this.exp = exp;
    }

    public static ShipLeveling levelZero() {
         return new ShipLeveling(0, 0);
    }

    /**
     *
     * @param gainedExp
     * @return incremented levels from exp
     */
    public int addExp(int gainedExp) {
        if (gainedExp <= 0) return 0;
        int levelIncremented = 0;
        this.exp += gainedExp;
        while (this.exp >= levelUpRequiredExp() && this.level <= MAX_LEVEL) {
            this.exp %= levelUpRequiredExp();
            levelIncremented++;
        }

        this.level += levelIncremented;
        makeDirty();
        return levelIncremented;
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
        makeDirty();
    }

    public void setExp(int exp) {
        this.exp = exp;
        makeDirty();
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    private void makeDirty() {
        this.dirty = true;
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
