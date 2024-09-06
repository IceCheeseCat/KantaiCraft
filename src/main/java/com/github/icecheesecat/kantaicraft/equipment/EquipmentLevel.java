package com.github.icecheesecat.kantaicraft.equipment;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;

public class EquipmentLevel implements INBTSerializable<CompoundTag> {

    private int level;
    private float difficulty;

    public static final EquipmentLevel NULL = new EquipmentLevel(-1, -1);
    public static final EquipmentLevel LEVEL_ONE = new EquipmentLevel(0, 1.0f);

    public EquipmentLevel(int level, float difficulty) {
        this.level = level;
        this.difficulty = difficulty;
    }

    public boolean doLevelUp(LivingEntity entity, int akashiLv) {
        if (this.level == 0) {
            level++;
            return true;
        }
        else {
            float chance = (float) (0.5f/(Math.log(level + 1) + akashiLv * 0.01f));
            float p = entity.getRandom().nextFloat();
            if (p < chance) {
                level++;
                return true;
            }
        }

        return false;
    }

    // TODO Configurable
    public int requiredNails() {
        int count = (int) difficulty;
        int count1 = (int) (difficulty * 2.0f);
        int count2 = (int) (difficulty * 2.5f);

        if (level <= 6) {
            return count;
        }
        else if (level <= 9) {
            return count1;
        }
        else {
            return count2;
        }

    }

    public int getLevel() {
        return level;
    }

    public float getDifficulty() {
        return difficulty;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("equipmentlevel.level", this.level);
        nbt.putFloat("equipmentlevel.difficulty", this.difficulty);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.level = nbt.getInt("equipmentlevel.level");
        this.difficulty = nbt.getFloat("equipmentlevel.difficulty");
    }

}
