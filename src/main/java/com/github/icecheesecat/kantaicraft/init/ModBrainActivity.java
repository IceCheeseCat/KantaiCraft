package com.github.icecheesecat.kantaicraft.init;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.checkerframework.checker.units.qual.A;

public class ModBrainActivity {

    public static final DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(ForgeRegistries.ACTIVITIES, KantaiCraft.MODID);

    public static final RegistryObject<Activity> CIRCLE = ACTIVITIES.register("activity.circle", () -> new Activity("circle"));
    public static final RegistryObject<Activity> SWOOP = ACTIVITIES.register("activity.circle", () -> new Activity("circle"));

}
