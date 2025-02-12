package com.github.icecheesecat.kantaicraft.faction;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.event.IModBusEvent;

public class FactionTag implements INBTSerializable<CompoundTag> {
    private int id;
    private String name;
    FactionType factionType;

    public static final FactionTag EMPTY = new FactionTag(-1, "Empty", FactionType.NEUTRAL);

    public FactionTag(int id, String name, FactionType factionType) {
        this.id = id;
        this.name = name;
        this.factionType = factionType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FactionType getFactionType() {
        return factionType;
    }

    public void copy(FactionTag tag) {
        this.id = tag.id;
        this.name = tag.name;
        this.factionType = tag.factionType;
    }

    public boolean isSame(FactionTag factionTag) {
        return this.id == factionTag.id && this.name == factionTag.name && this.factionType == factionTag.factionType;
    }

    public boolean isSameIdOrName(FactionTag factionTag) {
        return this.id == factionTag.id || this.name == factionTag.name;
    }

    public boolean isSameName(FactionTag factionTag) {
        return this.name.equals(factionTag.name);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("faction_id", this.id);
        nbt.putString("faction_name", this.name);
        nbt.putInt("faction_type", this.factionType.ordinal());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.id = nbt.getInt("faction_id");
        this.name = nbt.getString("faction_name");
        this.factionType =  FactionType.get(nbt.getInt("faction_type"));
    }

    @Override
    public String toString() {
        return "<Faction id: " + this.id + ", Faction name: " + this.name;
    }
}
