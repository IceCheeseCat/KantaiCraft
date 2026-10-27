package com.github.icecheesecat.kantaicraft.expedition;

import com.github.icecheesecat.kantaicraft.exception.KantaiCraftException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

public class ExpeditionDefinitionManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogUtils.getLogger();
    private ImmutableMap<ResourceLocation, ExpeditionDefinition> m_expeditions;
    private static final ExpeditionDefinitionManager INSTANCE = new ExpeditionDefinitionManager();

    private ExpeditionDefinitionManager() {
        super(GSON, "expeditions");
    }

    public static ExpeditionDefinitionManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        Map<ResourceLocation, ExpeditionDefinition> edfMap = new HashMap<>();
        for (var entry: pObject.entrySet()) {
            var resourceLocation = entry.getKey();
            var jsonElement = entry.getValue();
            if (!jsonElement.isJsonObject()) {
                LOGGER.debug("Skipping loading expedition {} as it's not a Json Object", resourceLocation);
                continue;
            }

            fromJson(resourceLocation, jsonElement.getAsJsonObject()).ifPresent(e -> edfMap.put(e.getId(), e));
        }

        this.m_expeditions = ImmutableMap.copyOf(edfMap);

        LOGGER.info("Loaded {} expeditions", this.m_expeditions.size());
    }

    public static Optional<ExpeditionDefinition> fromJson(ResourceLocation id, JsonObject jsonObject) {
        try {
            var name = jsonObject.get("name").getAsString();
            var description = jsonObject.get("description").getAsString();
            var duration = jsonObject.get("durationTicks").getAsLong();
            var rewards = jsonObject.getAsJsonObject("rewards");
            if (rewards == null || rewards.size() == 0) {
                LOGGER.error("Expedition json parsing error at {} no rewards", id);
                return Optional.empty();
            }
            Map<String, Integer> reward_map = new HashMap<>();
            for (var type : ExpeditionDefinition.REWARD_KEYS) {
                if (rewards.has(type)) {
                    reward_map.put(type, rewards.get(type).getAsInt());
                }
            }

            var n_expeditionDef = new ExpeditionDefinition(id, name, description);
            n_expeditionDef.setDurationTicks(duration);
            n_expeditionDef.setRewards(reward_map);
            return Optional.of(n_expeditionDef);
        } catch (Exception e) {
            LOGGER.error("Parsing error while loading expedition {}", id, e);
            return Optional.empty();
        }
    }

    public ImmutableList<ExpeditionDefinition> getAllExpeditions() {
        return this.m_expeditions.values().asList();
    }

    /// Main getter for ExpeditionDefinition. Get default if not found in map object.
    /// @param id ResourceLocation of the Expedition, defined by the actual resource located in data
    /// @return ExpeditionDefinition
    public ExpeditionDefinition getExpeditionDef(ResourceLocation id) {
        return this.m_expeditions.getOrDefault(id, ExpeditionDefinition.DEFAULT);
    }
}
