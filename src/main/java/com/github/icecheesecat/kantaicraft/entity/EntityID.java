package com.github.icecheesecat.kantaicraft.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class EntityID implements INBTSerializable<CompoundTag> {
    int id;

    public EntityID(int id) {
        this.id = id;
    }

    public static final EntityID DestroyerRoClass = new EntityID(0);
    public static final EntityID DestroyerIClass = new EntityID(1);
    public static final EntityID Inazuma = new EntityID(2);

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EntityID id) {
            this.id = id.id;
            return true;
        }
        return super.equals(obj);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("id", this.id);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.id = nbt.getInt("id");
    }

    public int getId() {
        return id;
    }
}
