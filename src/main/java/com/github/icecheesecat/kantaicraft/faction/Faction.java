package com.github.icecheesecat.kantaicraft.faction;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;

public class Faction implements INBTSerializable<CompoundTag> {

    private Map<FactionTag, FactionInstance> factions;

    public Faction() {
        factions = new HashMap<>();
    }

    public boolean factionExist(FactionTag tag) {
        return this.factions.keySet().stream().anyMatch(tTag -> tTag.isSame(tag));
    }

    public boolean sameNameFactionExits(FactionTag tag) {
        return this.factions.keySet().stream().anyMatch(tTag -> tTag.isSameName(tag));
    }

    public boolean sameIdOrNameFactionExist(FactionTag tag) {
        return this.factions.keySet().stream().anyMatch(tTag -> tTag.isSameIdOrName(tag));
    }

    public boolean createFaction(LivingEntity creator, FactionTag factionTag) {
        if (this.sameIdOrNameFactionExist(factionTag)) return false;
        this.factions.put(factionTag, FactionInstance.create(creator.getUUID()));
        return true;
    }

    public boolean joinFaction(FactionTag factionTag, LivingEntity target) {
        if (!this.factions.containsKey(factionTag)) {
            return false;
        }

        this.factions.get(factionTag).addMember(target.getUUID());
        return true;
    }

    public FactionInstance getFaction(FactionTag tag) {
        return this.factions.get(tag);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        var factions = this.factions.entrySet().stream().toList();

        nbt.putInt("faction.count", factions.size());
        for (int count = 0; count < factions.size(); count++) {
            FactionTag factionTag = factions.get(count).getKey();
            FactionInstance instance = factions.get(count).getValue();
            nbt.put( "faction_tag" + count, factionTag.serializeNBT());
            nbt.put("faction_instance" + count, instance.save());
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        int allCount = nbt.getInt("faction.count");
        for (int count = 0; count < allCount; count++) {
            FactionTag factionTag = new FactionTag(-1, "", FactionType.NEUTRAL);
                factionTag.deserializeNBT((CompoundTag) nbt.get("faction_tag" + count));

            FactionInstance nInst = new FactionInstance();
            nInst.load((CompoundTag) nbt.get("faction_instance"));
            this.factions.put(factionTag, nInst);
        }
    }
}
