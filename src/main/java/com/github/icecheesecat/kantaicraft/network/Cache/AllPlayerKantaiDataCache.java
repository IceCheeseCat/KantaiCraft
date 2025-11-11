package com.github.icecheesecat.kantaicraft.network.Cache;

import com.github.icecheesecat.kantaicraft.playerkantaidata.PlayerKantaiData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AllPlayerKantaiDataCache {
    /**
     * Only cache when player requests it, such as opening to visit
     */
    private boolean allPlayerKantaiDataCacheIsDirty = false;
    private Map<UUID, PlayerKantaiData> allPlayerKantaiDataCache = new HashMap<>();

    public void setPlayerKantaiDataCache(PlayerKantaiData data, UUID playerUUID) {
        this.allPlayerKantaiDataCache.put(playerUUID, data);
        this.allPlayerKantaiDataCacheIsDirty = true;
    }

    public boolean hasUpdated() {
        return allPlayerKantaiDataCacheIsDirty;
    }

    public void clean() {
        this.allPlayerKantaiDataCacheIsDirty = false;
    }
}
