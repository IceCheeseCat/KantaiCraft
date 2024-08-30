package com.github.icecheesecat.shincolle;

import com.github.icecheesecat.shincolle.init.ModCreativeTabs;
import com.github.icecheesecat.shincolle.init.ModEntity;
import com.github.icecheesecat.shincolle.init.ModItems;
import com.github.icecheesecat.shincolle.init.ModMenus;
import com.github.icecheesecat.shincolle.network.ModPacketHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Shincolle.MODID)
public class Shincolle
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "shincolle";


    public Shincolle()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ModEntity.ENTITIES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        ModPacketHandler.registerMessages();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

}
