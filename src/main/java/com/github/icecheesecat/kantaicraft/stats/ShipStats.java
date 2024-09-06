package com.github.icecheesecat.kantaicraft.stats;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.init.ModShipAttributes;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.util.INBTSerializable;

import java.nio.ByteBuffer;
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
