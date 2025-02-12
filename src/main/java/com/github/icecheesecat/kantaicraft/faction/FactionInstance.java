package com.github.icecheesecat.kantaicraft.faction;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.UUID;

public class FactionInstance {

    List<UUID> members;

    public CompoundTag save(int index) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("member.size." + index, members.size());
        for (int i = 0; i < members.size(); i++) {
            String key = "member" + i + "." + index;
            nbt.putUUID(key, members.get(i));
        }

        return nbt;
    }

    public void load(CompoundTag nbt, int index) {

    }

}
