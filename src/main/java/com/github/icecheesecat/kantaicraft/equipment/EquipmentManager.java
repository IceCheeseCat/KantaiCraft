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
//        createEquipment(EquipmentProperties.__12cm_single_gun_mount__);
//        createEquipment(EquipmentProperties.__12cm_twin_gun_mount__);
//        createEquipment(EquipmentProperties.__12cm_twin_gun_mount_model_b_kai_2__);
//        createEquipment(EquipmentProperties.__14cm_single_gun_mount__);
//        createEquipment(EquipmentProperties.__155mm_triple_gun_mount__);
//        createEquipment(EquipmentProperties.__203mm_twin_gun_mount__);
//        createEquipment(EquipmentProperties.__203mm_no3_single_gun_mount__);
//        createEquipment(EquipmentProperties.__prototype_203mm_no4_single_gun_mount__);
//        createEquipment(EquipmentProperties.__356mm_twin_gun_mount__);
//        createEquipment(EquipmentProperties.__prototype_356mm_triple_gun_mount__);
//        createEquipment(EquipmentProperties.__356mm_twin_gun_mount_kai_2__);
//        createEquipment(EquipmentProperties.__41cm_twin_gun_mount__);
//        createEquipment(EquipmentProperties.__prototype_41cm_triple_gun_mount__);
//        createEquipment(EquipmentProperties.__41cm_triple_gun_mount_kai_2__);
//        createEquipment(EquipmentProperties.__61cm_triple_torpedo_mount__);
//        createEquipment(EquipmentProperties.__61cm_triple_oxygen_torpedo_mount__);
//        createEquipment(EquipmentProperties.__61cm_quadruple_torpedo_mount__);
//        createEquipment(EquipmentProperties.__61cm_quadruple_oxygen_torpedo_mount__);
//        createEquipment(EquipmentProperties.__61cm_quintuple_oxygen_torpedo_mount__);
//        createEquipment(EquipmentProperties.__type_13_air_radar__);
//        createEquipment(EquipmentProperties.__type_13_air_radar_kai__);
//        createEquipment(EquipmentProperties.__type_22_surface_radar__);
//        createEquipment(EquipmentProperties.__type_22_surface_radar_kai_4__);
//        createEquipment(EquipmentProperties.__type_33_surface_radar__);
//        createEquipment(EquipmentProperties.__type_21_air_radar__);
//        createEquipment(EquipmentProperties.__type_21_air_radar_kai__);
//        createEquipment(EquipmentProperties.__type_32_surface_radar__);
//        createEquipment(EquipmentProperties.__type_42_air_radar__);
//        createEquipment(EquipmentProperties.__type_0_reconnaissance_seaplane__);
//        createEquipment(EquipmentProperties.__type_0_reconnaissance_seaplane_model_11b__);
//        createEquipment(EquipmentProperties.__type_0_reconnaissance_seaplane_model_11b_skilled__);
//        createEquipment(EquipmentProperties.__type_0_observation_seaplane__);
//        createEquipment(EquipmentProperties.__type_98_reconnaissance_seaplane_night_recon__);
//        createEquipment(EquipmentProperties.__type_0_reconnaissance_seaplane_model_11b_kai_night_recon__);
//        createEquipment(EquipmentProperties.__type_2_seaplane_fighter_kai__);
//        createEquipment(EquipmentProperties.__kyoufuu_kai__);
//        createEquipment(EquipmentProperties.__type_99_dive_bomber__);
//        createEquipment(EquipmentProperties.__type_99_dive_bomber_egusa_squadron__);
//        createEquipment(EquipmentProperties.__suisei__);
//        createEquipment(EquipmentProperties.__suisei_model_12a__);
//        createEquipment(EquipmentProperties.__suisei_model_22_634_air_group__);
//        createEquipment(EquipmentProperties.__suisei_601_air_group__);
//        createEquipment(EquipmentProperties.__suisei_egusa_squadron__);
//        createEquipment(EquipmentProperties.__type_97_torpedo_bomber__);
//        createEquipment(EquipmentProperties.__type_97_torpedo_bomber_tomonaga_squadron__);
//        createEquipment(EquipmentProperties.__type_97_torpedo_bomber_murata_squadron__);
//        createEquipment(EquipmentProperties.__tenzan__);
//        createEquipment(EquipmentProperties.__tenzan_601_air_group__);
//        createEquipment(EquipmentProperties.__tenzan_model_12_tomonaga_squadron__);
//        createEquipment(EquipmentProperties.__tenzan_model_12_murata_squadron__);
//        createEquipment(EquipmentProperties.__ryuusei__);
//        createEquipment(EquipmentProperties.__ryuusei_kai__);
//        createEquipment(EquipmentProperties.__ryuusei_kai_cardiv_1__);
//        createEquipment(EquipmentProperties.__type_96_fighter__);
//        createEquipment(EquipmentProperties.__type_0_fighter_model_21__);
//        createEquipment(EquipmentProperties.__type_0_fighter_model_21_skilled__);
//        createEquipment(EquipmentProperties.__type_0_fighter_model_32__);
//        createEquipment(EquipmentProperties.__type_0_fighter_model_52_skilled__);
//        createEquipment(EquipmentProperties.__zero_fighter_model_52c_601_air_group__);
//        createEquipment(EquipmentProperties.__shiden_kai_2__);
//        createEquipment(EquipmentProperties.__prototype_reppuu_late_model__);
//        createEquipment(EquipmentProperties.__reppuu_model_11__);
//        createEquipment(EquipmentProperties.__reppuu_kai_prototype_carrier_based_model__);
//        createEquipment(EquipmentProperties.__reppuu_kai_2__);
//        createEquipment(EquipmentProperties.__shinden_kai__);
//        createEquipment(EquipmentProperties.__type_93_passive_sonar__);
//        createEquipment(EquipmentProperties.__type_3_active_sonar__);
//        createEquipment(EquipmentProperties.__type_94_depth_charge_projector__);
//        createEquipment(EquipmentProperties.__type_3_depth_charge_projector__);
//        createEquipment(EquipmentProperties.__type_95_depth_charge__);
//        createEquipment(EquipmentProperties.__type_2_depth_charge__);
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
