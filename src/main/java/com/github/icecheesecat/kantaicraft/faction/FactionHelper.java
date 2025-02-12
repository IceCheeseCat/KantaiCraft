package com.github.icecheesecat.kantaicraft.faction;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class FactionHelper {

    public static void createFactionInstance(Level level, LivingEntity creator, FactionTag tag) {
        level.getCapability(LevelFactionCapability.FACTION).ifPresent(
                faction -> {
                    faction.createFaction(creator, tag);
                }
        );
    }

    public static void joinFaction(Level level, @Nullable Player user, LivingEntity target, FactionTag factionTag) {
        level.getCapability(LevelFactionCapability.FACTION).ifPresent(
                faction -> {
                    if (!faction.joinFaction(factionTag, target)) {
                        if (user != null) user.sendSystemMessage(Component.literal(target.getName().getString() + " failed to join " + faction));
                    }
                }
        );
    }

}
