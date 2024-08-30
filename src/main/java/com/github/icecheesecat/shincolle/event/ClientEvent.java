package com.github.icecheesecat.shincolle.event;


import com.github.icecheesecat.shincolle.Shincolle;
import com.github.icecheesecat.shincolle.init.ModMenus;
import com.github.icecheesecat.shincolle.menu.screen.ShipScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEvent {

    @Mod.EventBusSubscriber(modid = Shincolle.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class MOD {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(
                    () -> MenuScreens.register(ModMenus.SHIP_MENU.get(), ShipScreen::new)
            );
        }
    }


}
