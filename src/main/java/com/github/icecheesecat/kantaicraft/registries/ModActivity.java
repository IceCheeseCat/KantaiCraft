package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModActivity {

    public static final DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(ForgeRegistries.ACTIVITIES, KantaiCraft.MODID);
    public static final RegistryObject<Activity> BURN_OUT_FUELS = register("burn_out_fuels");
    public static final RegistryObject<Activity> CIRCLE = register("circle");
    public static final RegistryObject<Activity> HOSTILE_ATTACK = register("hostile_attack");
    public static final RegistryObject<Activity> MISSION = register("mission");
    public static final RegistryObject<Activity> RETURN = register("return");
    public static final RegistryObject<Activity> SITTING = register("sitting");
    public static final RegistryObject<Activity> STRIKE = register("strike");

    private static RegistryObject<Activity> register(String name) {
        return ACTIVITIES.register(name, () -> new Activity(name));
    }

}
