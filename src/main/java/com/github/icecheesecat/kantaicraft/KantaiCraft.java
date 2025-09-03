package com.github.icecheesecat.kantaicraft;

import com.github.icecheesecat.kantaicraft.config.ConfigEquipmentStats;
import com.github.icecheesecat.kantaicraft.config.ConfigEquipmentTree;
import com.github.icecheesecat.kantaicraft.registries.*;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
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
        ModEntity.ENTITIES.register(modEventBus);
        ModItem.ITEMS.register(modEventBus);
        ModMenu.MENUS.register(modEventBus);
        ModAttribute.ATTRIBUTES.register(modEventBus);
        ModActitvity.ACTIVITIES.register(modEventBus);
        ModMemoryModuleType.MEMORY_MODULE_TYPES.register(modEventBus);
        ModSensor.SENSOR_TYPES.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        ModPacketHandler.registerMessages();
        ModBlock.BLOCKS.register(modEventBus);
        ModBlock.BLOCK_ENTITIES.register(modEventBus);
        ModEntityDataSerializer.ENTITY_DATA_SERIALIZERS.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigEquipmentTree.SPEC, KantaiCraft.MODID + "_equipment_tree.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigEquipmentStats.SPEC, KantaiCraft.MODID + "_equipment_stats.toml");
    }

}
