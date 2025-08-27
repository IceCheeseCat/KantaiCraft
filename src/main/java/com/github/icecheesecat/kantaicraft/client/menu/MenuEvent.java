package com.github.icecheesecat.kantaicraft.client.menu;


import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.block.renderer.ShipyardRenderer;
import com.github.icecheesecat.kantaicraft.client.model.renderer.RendererDestroyerRo;
import com.github.icecheesecat.kantaicraft.menu.shipyard.ShipyardScreen;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import com.github.icecheesecat.kantaicraft.registries.ModEntity;
import com.github.icecheesecat.kantaicraft.registries.ModMenu;
import com.github.icecheesecat.kantaicraft.menu.ship.ShipScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.*;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MenuEvent {

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
