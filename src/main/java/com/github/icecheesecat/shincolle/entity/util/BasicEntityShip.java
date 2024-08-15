package com.github.icecheesecat.shincolle.entity.util;

import com.github.icecheesecat.shincolle.Shincolle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;

public abstract class BasicEntityShip extends PathfinderMob {

    /**
     * ship attributes: hp, def, atk, ...
     */
    protected ShipAttrs shipAttrs;
    private ShipFields.ShipClass shipClass;
    private ShipFields.ShipName shipName;
    private final Radar radar;

    private String customShipName;
    private boolean canMelee = false;
    private boolean canPickUpItem = false;
    private String ownerName = "";

    protected BasicEntityShip(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        radar = new Radar(this, 1 * 20);
    }

    public static AttributeSupplier.Builder createAtrributes() {
        return Pig.createAttributes();
    }

    @Override
    public void checkDespawn() {
        // Do nothing
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains(Shincolle.MODID + ".baseentityship.shipattrs")) {
            CompoundTag tag0 = nbt.getCompound(Shincolle.MODID + ".baseentityship.shipattrs");
            this.shipAttrs.deserializeNBT(tag0);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.put(Shincolle.MODID + ".baseentityship.shipattrs", this.shipAttrs.serializeNBT());
    }

    public ShipAttrs getShipAttrs() {
        return shipAttrs;
    }

    public void setShipAttrs(ShipAttrs shipAttrs) {
        this.shipAttrs = shipAttrs;
    }

    public ShipFields.ShipClass getShipClass() {
        return shipClass;
    }

    public void setShipClass(ShipFields.ShipClass shipClass) {
        this.shipClass = shipClass;
    }

    public ShipFields.ShipName getShipName() {
        return shipName;
    }

    public void setShipName(ShipFields.ShipName shipName) {
        this.shipName = shipName;
    }

    public String getCustomShipName() {
        return customShipName;
    }

    public void setCustomShipName(String customShipName) {
        this.customShipName = customShipName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public boolean isCanPickUpItem() {
        return canPickUpItem;
    }

    public void setCanPickUpItem(boolean canPickUpItem) {
        this.canPickUpItem = canPickUpItem;
    }

    public boolean isCanMelee() {
        return canMelee;
    }

    public void setCanMelee(boolean canMelee) {
        this.canMelee = canMelee;
    }

    public void changeRadarCooldown(int cd_in_sec) {
        this.radar.setMaxCooldown(cd_in_sec);
    }

    public Radar getRadar() {
        return radar;
    }
}
