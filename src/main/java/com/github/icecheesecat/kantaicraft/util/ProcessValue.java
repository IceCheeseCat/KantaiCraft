package com.github.icecheesecat.kantaicraft.util;

public record ProcessValue(Type type, int value) {

    public int getTick() {
        return type.getTick() * value;
    }

    public enum Type {
        HOUR, MINUTE, SECOND;

        public int getTick() {
            return switch (this) {
                case HOUR -> 3600 * 20;
                case MINUTE -> 60 * 20;
                case SECOND -> 20;
            };
        }
    }
}
