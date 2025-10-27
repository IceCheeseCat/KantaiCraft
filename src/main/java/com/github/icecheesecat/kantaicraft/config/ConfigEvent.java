package com.github.icecheesecat.kantaicraft.config;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigEvent {

    @SubscribeEvent
    public static void onConfigLoading(ModConfigEvent.Loading event) {
        var config = event.getConfig();

        if (config.getFileName().equals(KantaiCraft.MODID + "_equipment_stats.toml")) {
            EquipmentManager.ALL_EQUIPMENTS.forEach((id, equipment) -> {
                equipment.setStats(ConfigEquipmentStats.createMap(id));
            });
        }
    }

}
