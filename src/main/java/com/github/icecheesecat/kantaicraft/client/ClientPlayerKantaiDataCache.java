package com.github.icecheesecat.kantaicraft.client;

import com.github.icecheesecat.kantaicraft.playerkantaidata.PlayerKantaiData;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.menu.commandcenter.CommandCenterScreen;
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
            boolean none = playerKantaiData.getShips().stream().noneMatch(serializeEntityShip -> serializeEntityShip.getUuid().equals(entry.getKey()));
            if (none) {
                entry.getValue().discard();
                return true;
            }
            return false;
        });

        playerKantaiData.getShips().forEach(serializeEntityShip -> {
            if (!entitiesCache.containsKey(serializeEntityShip.getUuid())) {
                if (Minecraft.getInstance().level != null) {
                    entitiesCache.put(serializeEntityShip.getUuid(), (EntityShip) serializeEntityShip.getEntityType().create(Minecraft.getInstance().level));
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
