package com.github.icecheesecat.kantaicraft.event;


import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.menu.shipyard.ShipyardScreen;
import com.github.icecheesecat.kantaicraft.registries.ModMenu;
import com.github.icecheesecat.kantaicraft.menu.ship.ShipScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetupEvent {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(
            () -> {
                MenuScreens.register(ModMenu.SHIP_MENU.get(), ShipScreen::new);
                MenuScreens.register(ModMenu.SHIPYARD_MENU.get(), ShipyardScreen::new);
            }
        );
    }


}
