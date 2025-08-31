package com.github.icecheesecat.kantaicraft.tags;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.Map;

public class ModEntityTypeTags {

    public static final TagKey<EntityType<?>> DESTROYER_TAG = createTag("destroyer");
    public static final TagKey<EntityType<?>> LIGHT_CRUISER_TAG = createTag("light_cruiser");
    public static final TagKey<EntityType<?>> HEAVY_CRUISER_TAG = createTag("heavy_cruiser");
    public static final TagKey<EntityType<?>> BATTLE_SHIP_TAG = createTag("battleship");
    public static final TagKey<EntityType<?>> CARRIER_TAG = createTag("carrier");
    public static final TagKey<EntityType<?>> LIGHT_CARRIER_TAG = createTag("light_carrier");

    private static TagKey<EntityType<?>> createTag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(KantaiCraft.MODID, name));
    }

    public static final Map<String, TagKey<EntityType<?>>> ALL_ENTITY_TAGS = Map.of(
            DESTROYER_TAG.toString(), DESTROYER_TAG,
            LIGHT_CRUISER_TAG.toString(), LIGHT_CRUISER_TAG,
            HEAVY_CRUISER_TAG.toString(), HEAVY_CRUISER_TAG,
            BATTLE_SHIP_TAG.toString(), BATTLE_SHIP_TAG,
            CARRIER_TAG.toString(), CARRIER_TAG,
            LIGHT_CARRIER_TAG.toString(), LIGHT_CARRIER_TAG
    );

    public static TagKey<EntityType<?>> getTag(String name) {
        return ALL_ENTITY_TAGS.get(name);
    }
}
