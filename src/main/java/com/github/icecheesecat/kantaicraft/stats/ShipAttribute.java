package com.github.icecheesecat.kantaicraft.stats;

import net.minecraft.world.entity.ai.attributes.Attribute;

public class ShipAttribute extends Attribute {

    public ShipAttribute(String name, double defaultValue) {
        super(name, defaultValue);
        this.setSyncable(true);
    }

}
