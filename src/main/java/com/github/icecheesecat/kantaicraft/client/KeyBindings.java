package com.github.icecheesecat.kantaicraft.client;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public final class KeyBindings {
    public static final KeyBindings INSTANCE = new KeyBindings();
    private KeyBindings() {}
    private static final String CATEGORY = "key.categories." + KantaiCraft.MODID;

    private static String createKeyName(String name) {
        return "key." + KantaiCraft.MODID + "." + name;
    }

    public final KeyMapping debugEntity = new KeyMapping(
            createKeyName("debug_entity"),
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_B, -1),
            CATEGORY
    );





}
