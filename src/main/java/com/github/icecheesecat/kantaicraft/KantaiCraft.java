package com.github.icecheesecat.kantaicraft;

import com.github.icecheesecat.kantaicraft.command.ModCommands;
import com.github.icecheesecat.kantaicraft.config.ConfigEquipmentStats;
import com.github.icecheesecat.kantaicraft.config.ConfigEquipmentTree;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentManager;
import com.github.icecheesecat.kantaicraft.menu.commandcenter.CommandCenterScreen;
import com.github.icecheesecat.kantaicraft.menu.ship.ShipScreen;
import com.github.icecheesecat.kantaicraft.menu.shipyard.ShipyardScreen;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.registries.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib.GeckoLib;

import java.util.logging.LogManager;
import java.util.logging.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(KantaiCraft.MODID)
public class KantaiCraft
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "kantaicraft";
    public static final Logger LOGGER = LogManager.getLogManager().getLogger(MODID);

    public KantaiCraft()
    {
        GeckoLib.initialize();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ModEntity.ENTITY_TYPES.register(modEventBus);
        ModItem.ITEMS.register(modEventBus);
        ModMenu.MENUS.register(modEventBus);
        ModAttribute.ATTRIBUTES.register(modEventBus);
        ModActivity.ACTIVITIES.register(modEventBus);
        ModMemoryModuleType.MEMORY_MODULE_TYPES.register(modEventBus);
        ModSensor.SENSOR_TYPES.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        ModPacketHandler.registerMessages();
        ModBlock.BLOCKS.register(modEventBus);
        ModBlock.BLOCK_ENTITIES.register(modEventBus);
        ModEntityDataSerializer.ENTITY_DATA_SERIALIZERS.register(modEventBus);
        EquipmentManager.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigEquipmentTree.SPEC, KantaiCraft.MODID + "_equipment_tree.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigEquipmentStats.SPEC, KantaiCraft.MODID + "_equipment_stats.toml");
    }

    @Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class MenuEvent {

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(
                    () -> {
                        MenuScreens.register(ModMenu.SHIP_MENU.get(), ShipScreen::new);
                        MenuScreens.register(ModMenu.SHIPYARD_MENU.get(), ShipyardScreen::new);
                        MenuScreens.register(ModMenu.COMMAND_CENTER_MENU.get(), CommandCenterScreen::new);
                    }
            );
        }

    }

    @Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class CommandEvent {

        @SubscribeEvent
        public static void registerCommand(RegisterCommandsEvent event) {
            ModCommands.register(event.getDispatcher(), event.getBuildContext());
        }

    }



}
