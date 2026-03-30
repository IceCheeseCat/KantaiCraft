package com.github.icecheesecat.kantaicraft.entityship.entity.features;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

import java.util.function.Supplier;

public class LevelingModifier {

    private final Supplier<AttributeMap> attributes;
    private final Supplier<ShipLeveling> shipLeveling;
    private final AttributeGrowth growthMap;
    private final SynchedEntityData entityData;

    public LevelingModifier(Supplier<AttributeMap> attributes, Supplier<ShipLeveling> shipLeveling, AttributeGrowth growthMap, SynchedEntityData entityData) {
        this.attributes = attributes;
        this.shipLeveling = shipLeveling;
        this.growthMap = growthMap;
        this.entityData = entityData;
    }

    public void addExp(int exp) {
        int levelIncremented = this.shipLeveling.get().addExp(exp);
        addStatisticByLevelGrowth(levelIncremented);
        this.entityData.set(EntityShip.DATA_SHIP_LEVELING, this.shipLeveling.get(), true);
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
        int levelChanges = this.shipLeveling.get().setLevel(level);
        this.addStatisticByLevelGrowth(levelChanges);
        this.entityData.set(EntityShip.DATA_SHIP_LEVELING, this.shipLeveling.get(), true);
    }

}
