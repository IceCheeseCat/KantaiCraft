package com.github.icecheesecat.kantaicraft.blueprint;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.entityship.entity.ShipClass;
import com.github.icecheesecat.kantaicraft.entityship.entity.features.ShipLeveling;
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
    String entityType;
    CompoundTag savedData;
    ShipLeveling shipLeveling;
    ShipClass shipClass;
    boolean isEmpty = false;

    private Blueprint(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    protected Blueprint(EntityShip entityShip) {
        this.processTime = entityShip.getProcessTime();
        this.rarity = entityShip.getRarity();
        this.entityType = entityShip.getEncodeId();
        this.savedData = new CompoundTag();
        this.shipLeveling = entityShip.getShipLeveling();
        this.shipClass = entityShip.getShipClass();
        entityShip.addAdditionalSaveData(this.savedData);
    }

    public static Blueprint createEmpty() {
        return new Blueprint(true);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("isempty", this.isEmpty);
        if (isEmpty) return nbt;
        nbt.putInt("processtime", processTime);
        nbt.putInt("rarity", rarity.ordinal());
        nbt.putString("entitytype", this.entityType);
        nbt.put("saveddata", this.savedData);
        nbt.put("shipleveling", this.shipLeveling.serializeNBT());
        nbt.putInt("shipclass", this.shipClass.ordinal());

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.isEmpty = nbt.getBoolean("isempty");
        if (isEmpty) return;
        this.processTime = nbt.getInt("processtime");
        this.rarity = Rarity.values()[nbt.getInt("rarity")];
        this.entityType = nbt.getString("entitytype");
        this.savedData = nbt.getCompound("saveddata");
        this.shipLeveling = ShipLeveling.create(nbt.getCompound("shipleveling"));
        this.shipClass = ShipClass.values()[nbt.getInt("shipclass")];
    }

    public static Blueprint createFromTag(CompoundTag nbt) {
        Blueprint blueprint = new Blueprint(false);
        blueprint.deserializeNBT(nbt);
        return blueprint;
    }

    public static Blueprint create(EntityShip entityShip) {
        return new Blueprint(entityShip);
    }

    public static Blueprint createWithLevelZero(EntityShip entityShip, HostileStance hostileStance) {
        entityShip.setLevel(0);
        entityShip.setExp(0);
        var blueprint = create(entityShip);
        blueprint.setEntityType(EntityType.getKey(hostileStance.getPlayerSideEntityType()).toString());
        return blueprint;
    }

    public int getProcessTime() {
        return processTime;
    }
    public Rarity getRarity() {
        return rarity;
    }

    private void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public ShipLeveling getShipLevel() {
        return shipLeveling;
    }

    public ShipClass getShipClass() {
        return shipClass;
    }

    public void setShipClass(ShipClass shipClass) {
        this.shipClass = shipClass;
    }

    public CompoundTag getSavedData() {
        return this.savedData;
    }

    public boolean isEmpty() {
        return this.isEmpty;
    }

    public Optional<EntityType<?>> getEntityType() {
        return EntityType.byString(this.entityType);
    }


}
