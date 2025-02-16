package com.github.icecheesecat.kantaicraft.util;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModEntity;
import net.minecraft.world.entity.EntityType;

public class ShipFields {
    public enum ShipClass implements IProcessValue {
        DESTROYER,
        CARRIER,
        EMPTY;

        public static ShipClass getEnum(int i) {
            return values()[i];
        }

        @Override
        public ProcessValue getSecond() {
            return switch (this) {
                case DESTROYER -> new ProcessValue(ProcessValue.Type.SECOND, 10);
                case CARRIER -> new ProcessValue(ProcessValue.Type.SECOND, 0);
                case EMPTY -> new ProcessValue(ProcessValue.Type.SECOND, 0);
            };
        }

        @Override
        public ProcessValue getMinute() {
            return switch (this) {
                case DESTROYER -> new ProcessValue(ProcessValue.Type.MINUTE, 0);
                case CARRIER -> new ProcessValue(ProcessValue.Type.MINUTE, 30);
                case EMPTY -> new ProcessValue(ProcessValue.Type.MINUTE, 0);
            };
        }

        @Override
        public ProcessValue getHour() {
            return switch (this) {
                case DESTROYER -> new ProcessValue(ProcessValue.Type.HOUR, 0);
                case CARRIER -> new ProcessValue(ProcessValue.Type.HOUR, 4);
                case EMPTY -> new ProcessValue(ProcessValue.Type.HOUR, 0);
            };
        }
    }

    public enum ShipName implements IProcessValue {
        DestroyerRoClass,
        DestroyerIClass,
        EMPTY;

        public static ShipName getEnum(int i) {
            return values()[i];
        }

        public EntityType<? extends BasicEntityShip> getEntityType() {
            return switch (this) {
                case DestroyerRoClass -> ModEntity.DestroyerRoClass.get();
                case DestroyerIClass -> ModEntity.DestroyerIClass.get();
                case EMPTY -> null;
            };
        }

        @Override
        public ProcessValue getSecond() {
            return new ProcessValue(ProcessValue.Type.SECOND, 0);
        }

        @Override
        public ProcessValue getMinute() {
            return new ProcessValue(ProcessValue.Type.MINUTE, 0);
        }

        @Override
        public ProcessValue getHour() {
            return new ProcessValue(ProcessValue.Type.HOUR, 0);
        }
    }
}
