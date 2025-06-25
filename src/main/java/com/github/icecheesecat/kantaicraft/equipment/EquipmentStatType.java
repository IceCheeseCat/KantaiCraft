package com.github.icecheesecat.kantaicraft.equipment;

public enum EquipmentStatType {
    FIREPOWER(DataType.POSITIVE_DOUBLE, 0.0d, Double.MAX_VALUE),
    TORPEDO(DataType.POSITIVE_DOUBLE, 0.0d, Double.MAX_VALUE),
    ANTIAIR(DataType.POSITIVE_DOUBLE, 0.0d, Double.MAX_VALUE),
    CANNON_SIZE(DataType.POSITIVE_INTEGER, 0.0d, 4.0d),
    CANNON_RANGE(DataType.POSITIVE_DOUBLE, 0.0d, Double.MAX_VALUE),
    CANNON_COOLDOWN(DataType.POSITIVE_INTEGER, 0.0d, Long.MAX_VALUE),
    CANNON_MISSILE_VELOCITY(DataType.POSITIVE_DOUBLE, 0.0d, Double.MAX_VALUE),
    ACCURACY(DataType.POSITIVE_INTEGER, 0.0d, Double.MAX_VALUE),
    ARMOR(DataType.POSITIVE_INTEGER, 0.0d, Double.MAX_VALUE),
    EVASION(DataType.POSITIVE_INTEGER, 0.0d, Double.MAX_VALUE);

    final DataType dataType;
    double min, max;
    EquipmentStatType(DataType dataType, double min, double max) {
        this.dataType = dataType;
        this.min = min;
        this.max = max;
    }
    public static EquipmentStatType get(int i) {
        return values()[i];
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public boolean isInt() {
        return this.dataType == DataType.POSITIVE_INTEGER;
    }

    public boolean isDouble() {
        return this.dataType == DataType.POSITIVE_DOUBLE;
    }

    public boolean isLong() {
        return this.dataType == DataType.POSITIVE_LONG;
    }

    public enum DataType {
        POSITIVE_INTEGER,
        POSITIVE_LONG,
        POSITIVE_DOUBLE;
    }
}
