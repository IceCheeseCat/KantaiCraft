package com.github.icecheesecat.kantaicraft.faction;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

public class FactionInstance {

    UUID leader;
    List<UUID> members = new ArrayList<>();
    static Function<Integer, String> uuidKeyGetter = mi ->
            "member" + mi;

    public UUID getLeader() {
        return leader;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public boolean getMember(UUID pUUID) {
        return this.members.stream().anyMatch(uuid -> pUUID.compareTo(uuid) == 0);
    }

    public void addMember(UUID uuid) {
        this.members.add(uuid);
    }

    public CompoundTag save() {
        CompoundTag nbt = new CompoundTag();

        nbt.putUUID("leader.", leader);
        nbt.putInt("member.size.", members.size());
        for (int i = 0; i < members.size(); i++) {
            nbt.putUUID(uuidKeyGetter.apply(i), members.get(i));
        }

        return nbt;
    }

    public void load(CompoundTag nbt) {
        this.leader = nbt.getUUID("leader");
        int size = nbt.getInt("member.size");
        for (int i = 0; i < size; i++) {
            members.add(i, nbt.getUUID(uuidKeyGetter.apply(i)));
        }
    }

    public static FactionInstance create(UUID leader) {
        FactionInstance nInst = new FactionInstance();
        nInst.leader = leader;

        return nInst;
    }

}
