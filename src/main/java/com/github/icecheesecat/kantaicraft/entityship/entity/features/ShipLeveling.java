package com.github.icecheesecat.kantaicraft.entityship.entity.features;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraftforge.common.util.INBTSerializable;

public class ShipLeveling implements INBTSerializable<CompoundTag> {
    private int MAX_LEVEL = 999;
    private int level = 0;
    private int exp;
    public static final int BASE_REQUIRED = 10;

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

        this.exp += gainedExp;
        int levelIncremented = expToLevelTranslator();
        this.level += levelIncremented;
        return levelIncremented;
    }

    private int expToLevelTranslator() {
        int levelIncremented = 0;
        while (this.exp >= levelUpRequiredExp() && this.level <= MAX_LEVEL) {
            this.exp %= levelUpRequiredExp();
            levelIncremented++;
        }

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

    public int setLevel(int level) {
        int origin = this.level;
        this.level = Math.min(this.MAX_LEVEL, level);
        return this.level - origin;
    }

    public int setExp(int exp) {
        this.exp = exp;
        int levelIncremented = expToLevelTranslator();
        this.level += levelIncremented;
        return levelIncremented;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ShipLeveling shipLeveling) {
            return this.level == shipLeveling.getLevel() && this.exp == shipLeveling.getExp();
        }
        return super.equals(obj);
    }
}
