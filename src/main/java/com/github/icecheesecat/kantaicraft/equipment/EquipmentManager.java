package com.github.icecheesecat.kantaicraft.equipment;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.client.EquipmentRendererCache;
import com.github.icecheesecat.kantaicraft.model.equipment.renderer.EquipmentRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.DefaultedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipmentManager {

    private static final Map<Integer, EquipmentType> EQUIPMENT_TYPES = new HashMap<>();
    public static final Map<Integer, ResourceLocation> EQUIPMENT_ICON_LOCATION = new HashMap<>();
    public static final Map<Integer, ModelFactory> EQUIPMENT_MODELS = new HashMap<>();
    public static final Map<Integer, RendererFactory> EQUIPMENT_RENDERERS = new HashMap<>();
    public static void init() {
        registerEquipment(EquipmentProperties.EMPTY);
        registerEquipment(EquipmentProperties.__12cm_single_gun_mount__);
        registerEquipment(EquipmentProperties.__12cm_twin_gun_mount__);

        EquipmentRendererCache.cacheEquipmentRenderers();
    }

    private static void registerEquipment(EquipmentProperties equipmentProperties) {
        registerEquipmentType(equipmentProperties.getId(), equipmentProperties);
        registerEquipmentIconLocation(equipmentProperties.getId(), equipmentProperties);
        registerEquipmentModelAndRenderer(equipmentProperties.getId(), equipmentProperties);
    }

    private static void registerEquipmentType(int id, EquipmentProperties equipmentProperties) {
        if (EQUIPMENT_TYPES.containsKey(id)) {
            throw new IllegalStateException("EQUIPMENT_TYPES has the same id equipment already!");
        }
        EQUIPMENT_TYPES.put(id, new EquipmentType(equipmentProperties));
    }

    private static void registerEquipmentIconLocation(int id, EquipmentProperties equipmentProperties) {
        if (EQUIPMENT_ICON_LOCATION.containsKey(id)) {
            throw new IllegalStateException("EQUIPMENT_ICON_LOCATION has the same id resourceLocation already!");
        }
        EQUIPMENT_ICON_LOCATION.put(id, new ResourceLocation(KantaiCraft.MODID, equipmentProperties.getString()));
    }

    private static void registerEquipmentModelAndRenderer(int id, EquipmentProperties equipmentProperties) {
        if (EQUIPMENT_MODELS.containsKey(id)) {
            throw new IllegalStateException("EQUIPMENT MODELS has the same id resourceLocation already!");
        }
        if (EQUIPMENT_RENDERERS.containsKey(id)) {
            throw new IllegalStateException("EQUIPMENT RENDERERS has the same id resourceLocation already!");
        }

        ModelFactory modelFactory = () -> new DefaultedGeoModel<Equipment>(new ResourceLocation(KantaiCraft.MODID, equipmentProperties.getString())) {
            @Override
            protected String subtype() {
                return "equipment";
            }
        };

        RendererFactory rendererFactory = () -> new EquipmentRenderer(modelFactory.create());

        EQUIPMENT_MODELS.put(id, modelFactory);
        EQUIPMENT_RENDERERS.put(id, rendererFactory);
    }

    public static Equipment createNewEquipment(int id, int level) {
        EquipmentType equipmentType = EQUIPMENT_TYPES.get(id);

        return equipmentType.create(level);
    }

    public static Equipment createNewEquipment(int id) {
        return createNewEquipment(id, 0);
    }

    public static Equipment createEmptyEquipment() {
        return createNewEquipment(-1, 0);
    }

    @Nullable
    public static ResourceLocation getEquipmentIconResourceLocation(int id) {
        return EQUIPMENT_ICON_LOCATION.getOrDefault(id, null);
    }

    @Nullable
    public static GeoModel<Equipment> createEquipmentModel(int id) {
        if (EQUIPMENT_MODELS.containsKey(id)) {
            return EQUIPMENT_MODELS.get(id).create();
        }
        return null;
    }

    @Nullable
    public static GeoRenderer<Equipment> createEquipmentRenderer(int id) {
        if (EQUIPMENT_RENDERERS.containsKey(id)) {
            return EQUIPMENT_RENDERERS.get(id).create();
        }
        return null;
    }

    public static List<EquipmentType> getAllEquipmentTypes() {
        return EquipmentManager.EQUIPMENT_TYPES.entrySet().stream().filter(entry -> entry.getKey() != -1).map(Map.Entry::getValue).toList();
    }

    @Nullable
    public static EquipmentType getEquipmentTypeById(int id) {
        return EQUIPMENT_TYPES.get(id);
    }

    @FunctionalInterface
    public interface ModelFactory {
        DefaultedGeoModel<Equipment> create();
    }

    @FunctionalInterface
    public interface RendererFactory {
        GeoRenderer<Equipment> create();
    }

}
