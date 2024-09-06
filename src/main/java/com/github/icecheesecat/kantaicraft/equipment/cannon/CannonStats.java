package com.github.icecheesecat.kantaicraft.equipment.cannon;

import com.github.icecheesecat.kantaicraft.stats.ShipStats;

public class CannonStats extends ShipStats<CannonStats> {
    private final int size; // 0 small, 1 mid, 2 big
    public CannonStats(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
