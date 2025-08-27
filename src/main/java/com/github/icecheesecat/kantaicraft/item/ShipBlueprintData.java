//package com.github.icecheesecat.kantaicraft.item;
//
//import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
//import com.github.icecheesecat.kantaicraft.registries.ModItem;
//import com.github.icecheesecat.kantaicraft.util.ShipFields;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.item.ItemStack;
//
//import java.util.UUID;
//
//public class ShipBlueprintData {
//
//    EntityType<? extends BasicEntityShip> entityType;
//    Component name;
//    ShipFields.ShipClass shipClass;
//    ShipFields.ShipName shipName;
//    UUID builder;
//
//    public ShipBlueprintData(EntityType<? extends BasicEntityShip> entityType, Component name, ShipFields.ShipClass shipClass, ShipFields.ShipName shipName, UUID builder) {
//        this.entityType = entityType;
//        this.name = name;
//        this.shipClass = shipClass;
//        this.shipName = shipName;
//        this.builder = builder;
//    }
//
//    public void set(ShipBlueprintData data) {
//        this.entityType = data.entityType;
//        this.name = data.name;
//        this.shipClass = data.shipClass;
//        this.shipName = data.shipName;
//        this.builder = data.builder;
//    }
//
//    public EntityType<? extends BasicEntityShip> getEntityType() {
//        return entityType;
//    }
//
//    public Component getName() {
//        return name;
//    }
//
//    public ShipFields.ShipClass getShipClass() {
//        return shipClass;
//    }
//
//    public ShipFields.ShipName getShipName() {
//        return shipName;
//    }
//
//    public UUID getBuilder() {
//        return builder;
//    }
//
//    public static ShipBlueprintData create(BasicEntityShip ship, UUID builder) {
//        return new ShipBlueprintData((EntityType<? extends BasicEntityShip>) ship.getType(), ship.getType().getDescription(), ship.getShipClass(), ship.getShipName(), builder);
//    }
//
//    public static ShipBlueprintData read(CompoundTag nbt) {
//        int e = nbt.getInt("ship_name");
//        int c = nbt.getInt("ship_class");
//        UUID builder = nbt.getUUID("builder");
//
//        return instance(e, c, builder);
//    }
//
//    public static ShipBlueprintData instance(int iShipName, int iShipClass, UUID builder) {
//        var entityType = ShipFields.ShipName.getEnum(iShipName).getEntityType();
//        Component name;
//        if (entityType == null) {
//            name = null;
//        }
//        else {
//            name = entityType.getDescription();
//        }
//
//        var shipClass = ShipFields.ShipClass.getEnum(iShipClass);
//        var shipName = ShipFields.ShipName.getEnum(iShipName);
//
//        return new ShipBlueprintData(entityType, name, shipClass, shipName, builder);
//    }
//
//    public CompoundTag write() {
//        CompoundTag nbt = new CompoundTag();
//        nbt.putInt("ship_name", this.shipName.ordinal());
//        nbt.putInt("ship_class", this.shipClass.ordinal());
//        nbt.putUUID("builder", this.builder);
//
//        return nbt;
//    }
//
//    public static ShipBlueprintData empty() {
//        return new ShipBlueprintData(null, null, ShipFields.ShipClass.EMPTY, ShipFields.ShipName.EMPTY, UUID.randomUUID());
//    }
//
//    public int getProcessTime() {
//        return this.shipClass.getTick() + this.shipName.getTick();
//    }
//
//    public ItemStack createItemStack() {
//        ItemStack blueprint = new ItemStack(ModItem.SHIP_BLUEPRINT.get());
////        blueprint.getCapability(ShipBlueprintCapability.TOKEN).ifPresent(
////                data -> {
////                    data.set(this);
////                }
////        );
//
//
//        return blueprint;
//    }
//
//}
