package com.github.icecheesecat.kantaicraft.playerkantaidata;

import com.github.icecheesecat.kantaicraft.exception.KantaiCraftException;
import com.github.icecheesecat.kantaicraft.expedition.ExpeditionAssignment;
import com.github.icecheesecat.kantaicraft.util.CompoundTagHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerExpeditionData implements INBTSerializable<CompoundTag> {

    public Map<UUID, ExpeditionAssignment> m_inProgressAssignments = new HashMap<>();

    public void put(UUID p_assignmentId, ExpeditionAssignment p_assignment) {
        if (this.m_inProgressAssignments.containsKey(p_assignmentId)) {
            throw new KantaiCraftException(this.getClass(), "Shouldn't duplicated expedition exist");
        }

        this.m_inProgressAssignments.put(p_assignmentId, p_assignment);
    }

    public void tick() {
        m_inProgressAssignments.forEach((uuid, expeditionAssignment) -> {
            expeditionAssignment.tick();
        });
    }

    public PlayerExpeditionData copy() {
        PlayerExpeditionData n_playerExpeditionData = new PlayerExpeditionData();
        n_playerExpeditionData.m_inProgressAssignments = Map.copyOf(this.m_inProgressAssignments);
        return n_playerExpeditionData;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        CompoundTagHelper.serializeMap(nbt, "inProgressAssignment", this.m_inProgressAssignments, uuid -> {
            CompoundTag idNbt = new CompoundTag();
            idNbt.putUUID("assignmentId", uuid);
            return idNbt;
        }, assignment -> {
            CompoundTag assignmentNbt = new CompoundTag();
            assignmentNbt.put("assignment", assignment.serializeNBT());
            return assignmentNbt;
        });

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.m_inProgressAssignments.clear();
        this.m_inProgressAssignments = CompoundTagHelper.deserializeMap(nbt, "inProgressAssignment",
                keyNbt -> keyNbt.getUUID("assignmentId"),
                valueNbt -> {
            ExpeditionAssignment assignment = new ExpeditionAssignment();
            assignment.deserializeNBT(valueNbt.getCompound("assignment"));
            return assignment;
            });
    }
}