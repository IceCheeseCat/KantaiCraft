package com.github.icecheesecat.kantaicraft.event;


import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.registries.ModMenu;
import com.github.icecheesecat.kantaicraft.menu.ship.ShipScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.ClientLevel;
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
                    () -> MenuScreens.register(ModMenu.SHIP_MENU.get(), ShipScreen::new)
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
        }

//        @SubscribeEvent
//        public static void renderTickEvent(TickEvent.RenderTickEvent event) {
//            if (event.phase == TickEvent.Phase.END) {
//                if (Minecraft.getInstance().screen instanceof EquipmentScreen equipmentScreen) {
//                    equipmentScreen.equipmentWidgets.forEach(equipmentWidget -> {
//                        if (equipmentWidget.state != WidgetState.SELECTING) {
//                            equipmentWidget.getChildrenWidget().clear();
//                        }
//                    });
//                }
//            }
//        }
    }


}
