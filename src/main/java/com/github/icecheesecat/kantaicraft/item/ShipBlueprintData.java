package com.github.icecheesecat.kantaicraft.item;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.util.ShipFields;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;

public record ShipBlueprintData(EntityType<? extends BasicEntityShip> entityType, Component name, ShipFields.ShipClass shipClass, ShipFields.ShipName shipName) {

    public static ShipBlueprintData read(CompoundTag nbt) {
        int e = nbt.getInt("ship_name");
        int c = nbt.getInt("ship_class");
        var entityType = ShipFields.ShipName.getEnum(e).getEntityType();
        var name = entityType.getDescription();
        var shipClass = ShipFields.ShipClass.getEnum(c);
        var shipName = ShipFields.ShipName.getEnum(e);

        return new ShipBlueprintData(entityType, name, shipClass, shipName);
    }

    public CompoundTag write() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("ship_name", this.shipName.ordinal());
        nbt.putInt("ship_class", this.shipClass.ordinal());

        return nbt;
    }

}
