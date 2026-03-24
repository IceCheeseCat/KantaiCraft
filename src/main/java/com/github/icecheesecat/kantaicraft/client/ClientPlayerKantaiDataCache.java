package com.github.icecheesecat.kantaicraft.client;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.playerkantaidata.PlayerKantaiData;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClientPlayerKantaiDataCache {

    private final Map<UUID, EntityShip> entitiesCache = new HashMap<>();

    public void prepareCache(PlayerKantaiData playerKantaiData) {
        this.cacheEntityShips(playerKantaiData);
    }

    private void cacheEntityShips(PlayerKantaiData playerKantaiData) {
        entitiesCache.entrySet().removeIf((entry) -> {
            boolean none = playerKantaiData.getInDockShips().stream().noneMatch(serializeEntityShip -> serializeEntityShip.getUuid().equals(entry.getKey()));
            if (none) {
                entry.getValue().discard();
                return true;
            }
            return false;
        });

        playerKantaiData.getInDockShips().forEach(serializeEntityShip -> {
            if (!entitiesCache.containsKey(serializeEntityShip.getUuid())) {
                if (Minecraft.getInstance().level != null) {
                    if (serializeEntityShip.getEntityType().create(Minecraft.getInstance().level) instanceof EntityShip entityShip) {
                        entityShip.setNoAnimation();
                        entitiesCache.put(serializeEntityShip.getUuid(), entityShip);
                    }
                }
            }
        });
    }

    public Entity getCacheEntity(UUID uuid) {
        return entitiesCache.get(uuid);
    }

    public List<Map.Entry<UUID, EntityShip>> getListOfShips() {
        return entitiesCache.entrySet().stream().toList();
    }

}
