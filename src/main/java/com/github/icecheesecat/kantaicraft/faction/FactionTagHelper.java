package com.github.icecheesecat.kantaicraft.faction;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FactionTagHelper {

    public static void setFaction(FactionTag tag, LivingEntity le) {
        le.getCapability(FactionTagCapability.FACTION_TAG).ifPresent(
                factionTag -> {
                    factionTag.copy(tag);
                }
        );
    }

}
