package com.github.icecheesecat.kantaicraft.entityship.entity.features;

import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AttributeGrowth {

    protected final Map<Attribute, Double> statMap = new HashMap<>();

    public double get(Attribute name) {
        return this.statMap.get(name);
    }

    public void set(Attribute name, double value) {
        this.statMap.put(name, value);
    }

    public void add(Attribute name, double value) {
        this.statMap.put(name, this.get(name) + value);
    }

    public AttributeGrowth multiply(int level) {
        AttributeGrowth nStat = AttributeGrowth.builder().build();

        for (var entry: this.statMap.entrySet()) {
            nStat.statMap.put(entry.getKey(), entry.getValue() * level);
        }
        return nStat;
    }

    public Set<Attribute> getKeys() {
        return this.statMap.keySet();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Map<Attribute, Double> statMap = new HashMap<>();

        public Builder() {
        }

        public Builder with(Attribute name, double value) {
            this.statMap.put(name, value);
            return this;
        }

        public AttributeGrowth build() {
            AttributeGrowth attributeGrowth = new AttributeGrowth();
            attributeGrowth.statMap.putAll(this.statMap);
            return attributeGrowth;
        }
    }
}
