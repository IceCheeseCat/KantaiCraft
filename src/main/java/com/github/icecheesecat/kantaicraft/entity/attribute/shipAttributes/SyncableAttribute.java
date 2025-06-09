package com.github.icecheesecat.kantaicraft.entity.attribute.shipAttributes;

import net.minecraft.world.entity.ai.attributes.Attribute;

public class SyncableAttribute extends Attribute {

    public SyncableAttribute(String name, double defaultValue) {
        super(name, defaultValue);
        this.setSyncable(true);
    }

}
