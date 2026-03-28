package com.github.icecheesecat.kantaicraft.entityship.entity.features;

import net.minecraft.world.entity.ai.attributes.AttributeMap;

import java.util.function.Supplier;

public class LevelingModifier {

    private final Supplier<AttributeMap> attributes;
    private final Supplier<ShipLeveling> shipLeveling;
    private final AttributeGrowth growthMap;

    public LevelingModifier(Supplier<AttributeMap> attributes, Supplier<ShipLeveling> shipLeveling, AttributeGrowth growthMap) {
        this.attributes = attributes;
        this.shipLeveling = shipLeveling;
        this.growthMap = growthMap;
    }

    public void addExp(int exp) {
        int incrementedLevel = shipLeveling.get().addExp(exp);
        addStatisticByLevelGrowth(incrementedLevel);
    }

    private void addStatisticByLevelGrowth(int level) {
        var multipliedGrowth = growthMap.multiply(level);
        for (var attr: growthMap.getKeys()) {
            double growthValue = multipliedGrowth.get(attr);
            double originValue = attributes.get().getValue(attr);
            attributes.get().getInstance(attr).setBaseValue(originValue + growthValue);
        }
    }

    public void setLevel(int level) {
        int levelChanges = level - this.shipLeveling.get().getLevel();
        this.addStatisticByLevelGrowth(levelChanges);
        this.shipLeveling.get().setLevel(level);
    }

}
