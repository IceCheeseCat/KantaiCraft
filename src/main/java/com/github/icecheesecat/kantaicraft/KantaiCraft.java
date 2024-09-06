package com.github.icecheesecat.kantaicraft;

import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.init.*;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(KantaiCraft.MODID)
public class KantaiCraft
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "kantaicraft";


    public KantaiCraft()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ModEntity.ENTITIES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModShipAttributes.ATTRIBUTES.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        ModPacketHandler.registerMessages();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

}
