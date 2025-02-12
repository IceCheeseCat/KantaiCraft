package com.github.icecheesecat.kantaicraft.menu.screen;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import net.minecraft.resources.ResourceLocation;

public class FactionResourcesHelper {

    public static ResourceLocation create(int id) {
        return new ResourceLocation(KantaiCraft.MODID, "textures/faction_icon/faction_" + id + ".png");
    }

}
