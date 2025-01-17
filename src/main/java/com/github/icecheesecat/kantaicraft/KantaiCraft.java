package com.github.icecheesecat.kantaicraft;

import com.github.icecheesecat.kantaicraft.config.ConfigEquipmentData;
import com.github.icecheesecat.kantaicraft.registries.*;
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

        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ModEntity.ENTITIES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModShipAttributes.ATTRIBUTES.register(modEventBus);
        ModActitvity.ACTIVITIES.register(modEventBus);
        ModMemoryModuleType.MEMORY_MODULE_TYPES.register(modEventBus);
        ModSensor.SENSOR_TYPES.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        ModPacketHandler.registerMessages();
        ModEquipment.EQUIPMENTS.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigEquipmentData.SPEC, KantaiCraft.MODID + "_equipment.toml");
    }

}
