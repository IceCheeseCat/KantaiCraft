package com.github.icecheesecat.kantaicraft.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigEntityData {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC = BUILDER.build();

    private static final ForgeConfigSpec.IntValue NUMBER;

    static {
        NUMBER = BUILDER
                .comment("Number config test")
                .defineInRange("numbertest", 69, 0, 100);

    }

}
