package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModActitvity {

    public static final DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(ForgeRegistries.ACTIVITIES, KantaiCraft.MODID);

    public static final RegistryObject<Activity> CIRCLE = ACTIVITIES.register("activity.circle", () -> new Activity("circle"));
    public static final RegistryObject<Activity> STRIKE = ACTIVITIES.register("activity.strike", () -> new Activity("strike"));
    public static final RegistryObject<Activity> RETURN = ACTIVITIES.register("activity.return", () -> new Activity("return"));
    public static final RegistryObject<Activity> MISSION = ACTIVITIES.register("activity.mission", () -> new Activity("mission"));
    public static final RegistryObject<Activity> BURN_OUT_FUELS = ACTIVITIES.register("activity.burn_out_fuels", () -> new Activity("burn_out_fuels"));
    public static final RegistryObject<Activity> GUARD = ACTIVITIES.register("activity.guard", () -> new Activity("guard"));

}
