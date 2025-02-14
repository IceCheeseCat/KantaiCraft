package com.github.icecheesecat.kantaicraft.item;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.util.ShipFields;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;

public record ShipBlueprintData(EntityType<? extends BasicEntityShip> entityType, Component name, ShipFields.ShipClass shipClass, ShipFields.ShipName shipName) {

    public static ShipBlueprintData create(BasicEntityShip ship) {
        return new ShipBlueprintData((EntityType<? extends BasicEntityShip>) ship.getType(), ship.getType().getDescription(), ship.getShipClass(), ship.getShipName());
    }

    public static ShipBlueprintData read(CompoundTag nbt) {
        int e = nbt.getInt("ship_name");
        int c = nbt.getInt("ship_class");

        return instance(e, c);
    }

    public static ShipBlueprintData instance(int iShipName, int iShipClass) {
        var entityType = ShipFields.ShipName.getEnum(iShipName).getEntityType();
        Component name;
        if (entityType == null) {
            name = null;
        }
        else {
            name = entityType.getDescription();
        }

        var shipClass = ShipFields.ShipClass.getEnum(iShipClass);
        var shipName = ShipFields.ShipName.getEnum(iShipName);

        return new ShipBlueprintData(entityType, name, shipClass, shipName);
    }

    public CompoundTag write() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("ship_name", this.shipName.ordinal());
        nbt.putInt("ship_class", this.shipClass.ordinal());

        return nbt;
    }

    public static ShipBlueprintData empty() {
        return new ShipBlueprintData(null, null, ShipFields.ShipClass.EMPTY, ShipFields.ShipName.EMPTY);
    }

    public int getProcessTime() {
        return this.shipClass.getTick() + this.shipName.getTick();
    }
}
