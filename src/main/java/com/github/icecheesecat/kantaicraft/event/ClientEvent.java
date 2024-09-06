package com.github.icecheesecat.kantaicraft.event;


import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entity.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.init.ModMenus;
import com.github.icecheesecat.kantaicraft.init.ModShipAttributes;
import com.github.icecheesecat.kantaicraft.menu.screen.ShipScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEvent {

    @Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class MOD {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(
                    () -> MenuScreens.register(ModMenus.SHIP_MENU.get(), ShipScreen::new)
            );
        }
    }

    @Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class FORGE {
        @SubscribeEvent
        public static void clientTickEvent(TickEvent.ClientTickEvent event) {
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return;
            if (Minecraft.getInstance().isPaused()) return;
//            if (event.phase == TickEvent.Phase.END) {
//                for (int i = 0; i < level.getEntityCount(); i++) {
//                    BasicEntityShip entity = (BasicEntityShip) level.getEntity(1);
//                    System.out.print((entity));
//                    System.out.print("  " + entity.getAttributeValue(ModShipAttributes.FIREPOWER.get()));
//                    System.out.print(" " + entity.getAttributeValue(ModShipAttributes.LUCK.get()));
//                    System.out.print(" " + entity.getAttributeValue(ModShipAttributes.TORPEDO.get()));
//                    System.out.println(" " + entity.getAttributeValue(ModShipAttributes.LOS.get()));
//                }
//            }
        }
    }


}
