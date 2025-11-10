package com.github.icecheesecat.kantaicraft.network.packet;

public enum SyncType {
    GUARD(DataType.BOOLEAN),
    MELEE(DataType.BOOLEAN);

    final DataType dataType;

    SyncType(DataType dataType) {
        this.dataType = dataType;
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public enum DataType {
        BOOLEAN,
        INTEGER
    }
}
