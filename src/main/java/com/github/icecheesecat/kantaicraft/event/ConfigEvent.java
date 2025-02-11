package com.github.icecheesecat.kantaicraft.event;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.config.ConfigEquipmentStats;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentProperties;
import com.github.icecheesecat.kantaicraft.equipment.Equipments;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigEvent {

    @SubscribeEvent
    public static void onConfig(ModConfigEvent.Loading event) {
        var config = event.getConfig();

        if (config.getFileName().equals(KantaiCraft.MODID + "_equipment_stats.toml")) {
            var data = config.getConfigData();


            Equipments.ALL_EQUIPMENTS.forEach((id, equipment) -> {
                data.get("equipment." + EquipmentProperties.ALL_PROPERTIES.get(id).getName());
                equipment.setStats(ConfigEquipmentStats.createMap(id));
            });
        }
    }

}
