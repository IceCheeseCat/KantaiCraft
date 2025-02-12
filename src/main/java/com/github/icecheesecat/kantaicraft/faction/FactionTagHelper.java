package com.github.icecheesecat.kantaicraft.faction;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FactionTagHelper {

    public static void joinFaction(Level level, LivingEntity creator, LivingEntity livingEntity, FactionTag tag) {
        livingEntity.getCapability(FactionTagCapability.FACTION_TAG).ifPresent(
                factionTag -> {
                    factionTag.set(tag.getId(), tag.getName(), tag.getFactionType());
                }
        );

        level.getCapability(LevelFactionCapability.FACTION).ifPresent(faction -> {
            if (!faction.factionExist(tag)) {
                faction.createFaction(creator, tag);
            }
        });
    }



}
