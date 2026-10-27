package com.github.icecheesecat.kantaicraft.expedition;

import com.github.icecheesecat.kantaicraft.exception.KantaiCraftException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public class ExpeditionAssignment implements INBTSerializable<CompoundTag> {

    // CompoundTags
    private static final String DEFINITION_ID_TAG = "definitionId";
    private static final String ASSIGNMENT_ID_TAG = "assignmentId";
    private static final String PROGRESS_TAG = "progress";

    private UUID m_assignmentId;
    private ResourceLocation m_definitionId;
    private long m_progress;

    public ExpeditionAssignment() {
    }

    // Creating a new instance of Expedition Assignment owned by player (theoretically)
    public ExpeditionAssignment(ResourceLocation p_definitionId) {
        m_assignmentId = UUID.randomUUID();
        m_definitionId = p_definitionId;
    }

    public UUID getAssignmentId() {
        return m_assignmentId;
    }

    public void setAssignmentId(UUID p_assignmentId) {
        m_assignmentId = p_assignmentId;
    }

    public ResourceLocation getDefinitionId() {
        return m_definitionId;
    }

    public void setDefinitionId(ResourceLocation p_definitionId) {
        m_definitionId = p_definitionId;
    }

    public long getProgress() {
        return m_progress;
    }

    public void setProgress(long p_progress) {
        m_progress = p_progress;
    }

    private ExpeditionDefinition getDefinition() {
        if (m_definitionId == null) throw new KantaiCraftException(this.getClass(), "Cannot get ExpeditionDefinition since m_definitionId is null");
        return ExpeditionDefinitionManager.getInstance().getExpeditionDef(m_definitionId);
    }

    public boolean isDone() {
        return m_progress > this.getDefinition().getDurationTicks();
    }

    public void tick() {
        this.m_progress++;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString(DEFINITION_ID_TAG, this.m_definitionId.toString());
        nbt.putUUID(ASSIGNMENT_ID_TAG, this.m_assignmentId);
        nbt.putLong(PROGRESS_TAG, this.m_progress);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag p_nbt) {
        this.m_definitionId = new ResourceLocation(p_nbt.getString(DEFINITION_ID_TAG));
        this.m_assignmentId = p_nbt.getUUID(ASSIGNMENT_ID_TAG);
        this.m_progress = p_nbt.getLong(PROGRESS_TAG);
    }
}
