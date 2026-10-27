package com.github.icecheesecat.kantaicraft.expedition;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.google.common.collect.ImmutableSet;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class ExpeditionDefinition {

    private ResourceLocation m_id;
    private String m_name;
    private String m_description;
    private long m_durationTicks;
    private Map<String, Integer> m_rewards = Map.of();
    public static final ImmutableSet<String> REWARD_KEYS = ImmutableSet.of("AMMO", "FUEL");

    public static final ExpeditionDefinition DEFAULT = new ExpeditionDefinition(new ResourceLocation(KantaiCraft.MODID, "expeditions.default"), "expedition.kantaicraft.default", "Default Expedition (Fallback)");

    public ExpeditionDefinition(ResourceLocation p_id, String p_nameKey, String p_descriptionKey) {
        m_id = p_id;
        m_name = p_nameKey;
        m_description = p_descriptionKey;
    }

    public ResourceLocation getId() {
        return m_id;
    }

    public void setId(ResourceLocation p_id) {
        m_id = p_id;
    }

    public String getNameKey() {
        return m_name;
    }

    public void setNameKey(String p_nameKey) {
        m_name = p_nameKey;
    }

    public String getDescriptionKey() {
        return m_description;
    }

    public void setDescriptionKey(String p_descriptionKey) {
        m_description = p_descriptionKey;
    }

    public long getDurationTicks() {
        return m_durationTicks;
    }

    public void setDurationTicks(long p_durationTicks) {
        m_durationTicks = p_durationTicks;
    }

    public Map<String, Integer> getRewards() {
        return m_rewards;
    }

    public void setRewards(Map<String, Integer> p_rewards) {
        m_rewards = Map.copyOf(p_rewards);
    }
}
