package com.github.icecheesecat.kantaicraft.blueprint;

import com.github.icecheesecat.kantaicraft.entity.EntityID;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.util.INBTSerializable;

public class Blueprint implements INBTSerializable<CompoundTag> {

    public static final Blueprint EMPTY = new Blueprint();
    int processTime = -1;
    EntityID entityID = new EntityID(-1);
    Rarity rarity = Rarity.COMMON;

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("processtime", processTime);
        nbt.put("entityid", entityID.serializeNBT());
        nbt.putInt("rarity", rarity.ordinal());

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.processTime = nbt.getInt("processtime");
        this.entityID = new EntityID(-1);
        this.entityID.deserializeNBT(nbt.getCompound("entityid"));
        this.rarity = Rarity.values()[nbt.getInt("rarity")];
    }

    public static Blueprint createFromTag(CompoundTag nbt) {
        Blueprint blueprint = new Blueprint();
        blueprint.deserializeNBT(nbt);
        return blueprint;
    }

    public static Blueprint create(BasicEntityShip ship) {
        var blueprint = new Blueprint();
            blueprint.setEntityID(ship.getEntityId());
            blueprint.setRarity(ship.getRarity());
            blueprint.setProcessTime(ship.getProcessTime());

        return blueprint;
    }

    public int getProcessTime() {
        return processTime;
    }

    public EntityID getEntityID() {
        return entityID;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setProcessTime(int processTime) {
        this.processTime = processTime;
    }

    public void setEntityID(EntityID entityID) {
        this.entityID = entityID;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public boolean isEmpty() {
        return this.entityID.getId() == -1 || this.processTime == -1;
    }
}
