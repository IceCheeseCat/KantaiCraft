package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.entityship.entity.destroyer.*;
import com.github.icecheesecat.kantaicraft.entityship.entity.destroyer.DestroyerIClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class ModEntity {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,
    KantaiCraft.MODID);

    public static final MobCategory HOSTILE_SHIP = MobCategory.create("hostile_ship", "kantaicraft:hostile_ship", 20, false, false, 128);
    public static final MobCategory PLAYER_SHIP = MobCategory.create("player_ship", "kantaicraft:player_ship", -1, true, true, 128);
    public static final MobCategory EQUIPMENT_ENTITY = MobCategory.create("equipment_entity", "kantaicraft:equipment_entity", -1, true, true, 128);

    public static class PlayerShip {
        public static final RegistryObject<EntityType<DestroyerRoClass>> DESTROYER_RO_CLASS =
                registerPlayerShip(DestroyerRoClass.PlayerSide::new, "destroyer_ro_class", 1.0f, 0.8f);
        public static final RegistryObject<EntityType<DestroyerIClass>> DESTROYER_I_CLASS =
                registerPlayerShip(DestroyerIClass.PlayerSide::new, "destroyer_i_class", 1.0f, 0.8f);
        public static final RegistryObject<EntityType<Inazuma>> INAZUMA =
                registerPlayerShip(Inazuma.PlayerSide::new, "inazuma", 0.8f, 1.6f);
        public static final RegistryObject<EntityType<Ikazuchi>> IKAZUCHI =
                registerPlayerShip(Ikazuchi.PlayerSide::new, "ikazuchi", 0.8f, 1.6f);
        public static final RegistryObject<EntityType<Hibiki>> HIBIKI =
                registerPlayerShip(Hibiki.PlayerSide::new, "hibiki", 0.8f, 1.6f);

    }

    public static class HostileShip {

        public static final RegistryObject<EntityType<DestroyerRoClass>> DESTROYER_RO_CLASS =
                registerHostileShip(DestroyerRoClass.HostileSide::new, "hostile_destroyer_ro_class", 1.0f, 0.8f);
        public static final RegistryObject<EntityType<DestroyerIClass>> DESTROYER_I_CLASS =
                registerHostileShip(DestroyerIClass.HostileSide::new, "hostile_destroyer_i_class", 1.0f, 0.8f);
        public static final RegistryObject<EntityType<Inazuma>> INAZUMA =
                registerHostileShip(Inazuma.HostileSide::new, "hostile_inazama", 0.8f, 1.4f);
        public static final RegistryObject<EntityType<Ikazuchi>> IKAZUCHI =
                registerHostileShip(Ikazuchi.HostileSide::new, "hostile_ikazuchi", 0.8f, 1.4f);
        public static final RegistryObject<EntityType<Hibiki>> HIBIKI =
                registerHostileShip(Hibiki.HostileSide::new, "hostile_hibiki", 0.8f, 1.4f);
    }

//    public static final RegistryObject<EntityType<EntityA6MZeroFighter>> A6M_Zero_Fighter = ENTITIES.register("a6m_zero_fighter", () ->
//            EntityType.Builder.of(EntityA6MZeroFighter::new, MobCategory.MISC).sized(1.0f, 1.0f).build(new ResourceLocation(KantaiCraft.MODID, "a6m_zero_fighter").toString()));
    private static <T extends Entity> RegistryObject<EntityType<T>> registerPlayerShip(EntityType.EntityFactory<T> factory, String name, float width, float height) {
        return ENTITY_TYPES.register(name, () ->
            EntityType.Builder.of(factory, PLAYER_SHIP).sized(width, height).build(new ResourceLocation(KantaiCraft.MODID, name).toString()));
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> registerHostileShip(EntityType.EntityFactory<T> factory, String name, float width, float height) {
        return ENTITY_TYPES.register(name, () ->
            EntityType.Builder.of(factory, HOSTILE_SHIP).sized(width, height).build(new ResourceLocation(KantaiCraft.MODID, name).toString()));
    }

    public static List<EntityType<?>> getAllPlayerShips() {
        return ENTITY_TYPES.getEntries().stream().filter(registryObject-> registryObject.get().getCategory() == PLAYER_SHIP).map(RegistryObject::get).collect(Collectors.toList());
    }

    public static List<EntityType<?>> getAllHostileShips() {
        return ENTITY_TYPES.getEntries().stream().filter(registryObject-> registryObject.get().getCategory() == PLAYER_SHIP).map(RegistryObject::get).collect(Collectors.toList());
    }

    @Nullable
    public static EntityType<?> getByName(String entityTypeName) {
        var list = ENTITY_TYPES.getEntries().stream().filter(registry -> registry.get().toString().equals(entityTypeName)).toList();
        if (list.isEmpty()) return null;
        return list.get(0).get();
    }

}