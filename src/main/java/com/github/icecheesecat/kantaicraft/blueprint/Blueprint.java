package com.github.icecheesecat.kantaicraft.blueprint;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.entityship.entity.ShipClass;
import com.github.icecheesecat.kantaicraft.entityship.entity.ShipLeveling;
import com.github.icecheesecat.kantaicraft.entityship.stance.HostileStance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Optional;

public class Blueprint implements INBTSerializable<CompoundTag> {

    public static final Blueprint EMPTY = createEmpty();
    int processTime;
    Rarity rarity;
    ShipLeveling shipLeveling;
    ShipClass shipClass;
    String entityType;

    protected Blueprint(int processTime, Rarity rarity, ShipLeveling shipLeveling, ShipClass shipClass, String entityType) {
        this.processTime = processTime;
        this.rarity = rarity;
        this.shipLeveling = shipLeveling;
        this.shipClass = shipClass;
        this.entityType = entityType;
    }

    public static Blueprint createEmpty() {
        return new Blueprint(-1, Rarity.COMMON, ShipLeveling.levelZero(), ShipClass.NONE, "");
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("processtime", processTime);
        nbt.putInt("rarity", rarity.ordinal());
        nbt.put("shiplevel", shipLeveling.serializeNBT());
        nbt.putInt("shipclass", shipClass.ordinal());
        nbt.putString("entitytype", this.entityType);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.processTime = nbt.getInt("processtime");
        this.rarity = Rarity.values()[nbt.getInt("rarity")];
        this.shipLeveling = ShipLeveling.create(nbt.getCompound("shiplevel"));
        this.shipClass = ShipClass.get(nbt.getInt("shipclass"));
        this.entityType = nbt.getString("entitytype");
    }

    public static Blueprint createFromTag(CompoundTag nbt) {
        Blueprint blueprint = createEmpty();
        blueprint.deserializeNBT(nbt);
        return blueprint;
    }

    public static Blueprint create(EntityShip entityShip) {
        return new Blueprint(entityShip.getProcessTime(), entityShip.getRarity(), entityShip.getShipLeveling(), entityShip.getShipClass(), entityShip.getEncodeId());
    }

    public static Blueprint createWithLevelZero(EntityShip entityShip) {
        var blueprint = create(entityShip);
        blueprint.setShipLeveling(ShipLeveling.levelZero());
        return blueprint;
    }

    public static Blueprint createWithLevelZero(EntityShip entityShip, HostileStance hostileStance) {
        var blueprint = create(entityShip);
        blueprint.setEntityType(EntityType.getKey(hostileStance.getPlayerSideEntityType()).toString());
        blueprint.setShipLeveling(ShipLeveling.levelZero());
        return blueprint;
    }

    public int getProcessTime() {
        return processTime;
    }
    public Rarity getRarity() {
        return rarity;
    }

    public void setProcessTime(int processTime) {
        this.processTime = processTime;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public ShipLeveling getShipLevel() {
        return shipLeveling;
    }

    public void setShipLeveling(ShipLeveling shipLeveling) {
        this.shipLeveling = shipLeveling;
    }

    public ShipLeveling getShipLeveling() {
        return shipLeveling;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public ShipClass getShipClass() {
        return shipClass;
    }

    public void setShipClass(ShipClass shipClass) {
        this.shipClass = shipClass;
    }

    public boolean isEmpty() {
        return this.processTime == -1;
    }

    public Optional<EntityType<?>> getEntityType() {
        return EntityType.byString(this.entityType);
    }
}
