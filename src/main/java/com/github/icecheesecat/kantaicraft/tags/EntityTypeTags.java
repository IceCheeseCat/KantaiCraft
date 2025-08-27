package com.github.icecheesecat.kantaicraft.tags;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicDestroyerShip;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class EntityTypeTags {

    public static final TagKey<EntityType<?>> DESTROYER_TAG = createTag("destroyer");
    public static final TagKey<EntityType<?>> LIGHT_CRUISER_TAG = createTag("light_cruiser");
    public static final TagKey<EntityType<?>> HEAVY_CRUISER_TAG = createTag("heavy_cruiser");
    public static final TagKey<EntityType<?>> BATTLE_SHIP_TAG = createTag("battleship");
    public static final TagKey<EntityType<?>> CARRIER_TAG = createTag("carrier");
    public static final TagKey<EntityType<?>> LIGHT_CARRIER_TAG = createTag("light_carrier");

    private static TagKey<EntityType<?>> createTag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(KantaiCraft.MODID, name));
    }
}
