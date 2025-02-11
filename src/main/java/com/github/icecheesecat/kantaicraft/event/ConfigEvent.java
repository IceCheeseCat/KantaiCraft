package com.github.icecheesecat.kantaicraft.event;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.config.ConfigEquipmentStats;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentProperties;
import com.github.icecheesecat.kantaicraft.equipment.Equipments;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigEvent {

    @SubscribeEvent
    public static void onConfigLoading(ModConfigEvent.Loading event) {
        var config = event.getConfig();

        if (config.getFileName().equals(KantaiCraft.MODID + "_equipment_stats.toml")) {
            Equipments.ALL_EQUIPMENTS.forEach((id, equipment) -> {
                equipment.setStats(ConfigEquipmentStats.createMap(id));
            });
        }
    }

}
