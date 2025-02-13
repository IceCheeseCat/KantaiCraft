package com.github.icecheesecat.kantaicraft.util;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModEntity;
import net.minecraft.world.entity.EntityType;

public class ShipFields {
    public enum ShipClass {
        DESTROYER,
        CARRIER;

        public static ShipClass getEnum(int i) {
            return values()[i];
        }
    }

    public enum ShipName {
        DestroyerRoClass,
        DestroyerIClass;

        public static ShipName getEnum(int i) {
            return values()[i];
        }

        public EntityType<? extends BasicEntityShip> getEntityType() {
            return switch (this) {
                case DestroyerRoClass -> ModEntity.DestroyerRoClass.get();
                case DestroyerIClass -> ModEntity.DestroyerIClass.get();
            };
        }
    }
}
