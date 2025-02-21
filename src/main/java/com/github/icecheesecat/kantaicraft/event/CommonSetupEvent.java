package com.github.icecheesecat.kantaicraft.event;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetupEvent {

    @SubscribeEvent
    public static void onServerSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }

}
