package com.github.icecheesecat.kantaicraft.stats.shipAttributes;

import com.github.icecheesecat.kantaicraft.customObjects.ModShipAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.HashMap;
import java.util.Map;

public class ShipStats <T extends ShipStats<?>> {

    final Map<Attribute, Double> statsMap = new HashMap<>();

    public ShipStats() {
    }

    public T addStats(Attribute a, double v) {
        if (ModShipAttributes.ATTRIBUTES.getEntries().contains(a)) {
            this.statsMap.put(a, v);
        }

        return (T) this;
    }

    public Map<Attribute, Double> getStats() {
        return this.statsMap;
    }

}
