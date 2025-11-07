package com.github.icecheesecat.kantaicraft.client;

import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentManager;
import com.github.icecheesecat.kantaicraft.model.equipment.renderer.EquipmentRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;

import java.util.HashMap;
import java.util.Map;

public class EquipmentRendererCache {

    private static final Map<Integer, GeoRenderer<Equipment>> equipmentRendererCache = new HashMap<>();

    public static void cacheEquipmentRenderers() {
        EquipmentManager.getAllEquipmentTypes().forEach((equipmentType)-> {
            equipmentRendererCache.put(equipmentType.getId(), EquipmentManager.createEquipmentRenderer(equipmentType.getId()));
        });
    }

    public static GeoRenderer<Equipment> getEquipmentRenderer(int id) {
        return equipmentRendererCache.get(id);
    }

}
