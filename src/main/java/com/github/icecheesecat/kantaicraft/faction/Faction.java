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
        this.factions.put(factionTag, new FactionInstance(creator.getUUID(), new ArrayList<>()));
        return true;
    }

    public boolean moveEntityToFaction(FactionTag factionTag, LivingEntity livingEntity) {
        // faction does not exist
        if (!factionExist(factionTag)) {
            return false;
        }

        this.factions.get(factionTag).members().add(livingEntity.getUUID());
        return true;
    }

    public FactionInstance leaderGetFaction(LivingEntity leader, FactionTag tag) {
        var inst = this.getFaction(tag);
        if (inst.leader().compareTo(leader.getUUID()) == 0) {
            return inst;
        }

        return null;
    }

    public FactionInstance getFaction(FactionTag tag) {
        return this.factions.get(tag);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        int count = 0;
        for (var entry: this.factions.entrySet()) {
            FactionTag factionTag = entry.getKey();
            FactionInstance instance = entry.getValue();
            nbt.put( "faction_tag" + count, factionTag.serializeNBT());
            nbt.putUUID("leader" + count, instance.leader());
            nbt.putInt("member_count" + count, instance.members().size());
            var members = instance.members();
            for (int i = 0; i < members.size(); i++) {
                String memStr = "member" + i + ".";
                nbt.putUUID( memStr + count, members.get(i));
            }

            count++;
        }

        nbt.putInt("faction.count", count);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        int allCount = nbt.getInt("faction.count");
        for (int count = 0; count < allCount; count++) {
            FactionTag factionTag = new FactionTag(-1, "", FactionType.NEUTRAL);
                factionTag.deserializeNBT((CompoundTag) nbt.get("faction_tag" + count));

            UUID leader = nbt.getUUID("leader" + count);
            int member_count = nbt.getInt("member_count" + count);
            List<UUID> members = new ArrayList<>();
            for (int i = 0; i < member_count; i++) {
                String memStr = "member" + i + ".";
                members.add(i, (nbt.getUUID( memStr + count)));
            }

            FactionInstance nInst = new FactionInstance(leader, members);
            this.factions.put(factionTag, nInst);
        }
    }
}
