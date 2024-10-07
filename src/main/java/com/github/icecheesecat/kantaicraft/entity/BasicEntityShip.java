package com.github.icecheesecat.kantaicraft.entity;

import com.github.icecheesecat.kantaicraft.entitySync.EquipmentS2CPacket;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentSlots;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import com.github.icecheesecat.kantaicraft.init.ModShipAttributes;
import com.github.icecheesecat.kantaicraft.menu.ShipMenu;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.stats.IStatsGrowth;
import com.github.icecheesecat.kantaicraft.stats.ShipAttribute;
import com.github.icecheesecat.kantaicraft.util.*;
import com.github.icecheesecat.kantaicraft.util.tickable.EquipmentActionHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

public abstract class BasicEntityShip extends PathfinderMob implements MenuProvider, IStatsGrowth {

    /**
     * ship attributes: hp, def, atk, ...
     */
    private static final EntityDataAccessor<Integer> DATA_AIRCRAFT = SynchedEntityData.defineId(BasicEntityShip.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_FUEL = SynchedEntityData.defineId(BasicEntityShip.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_AMMO = SynchedEntityData.defineId(BasicEntityShip.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<String> DATA_EQUIPMENT_TYPES = SynchedEntityData.defineId(BasicEntityShip.class, EntityDataSerializers.STRING);


    private ShipFields.ShipClass shipClass;
    private ShipFields.ShipName shipName;
    private final Radar radar;

    private String customShipName;
    private boolean canMelee = false;
    private boolean canPickUpItem = false;
    private String ownerName = "";

    protected EquipmentSlots equipmentSlot;
    private final LazyOptional<EquipmentSlots> lazyEquipmentSlot;
//    private ItemSlots itemSlots;
    public static final Capability<EquipmentSlots> WEAPON_SLOTS = CapabilityManager.get(new CapabilityToken<>(){});

    protected final EquipmentActionHandler actionHandler;

    protected BasicEntityShip(EntityType<? extends PathfinderMob> entityType, Level level, EquipmentSlots equipmentSlot) {
        super(entityType, level);
        this.equipmentSlot = equipmentSlot;
        this.lazyEquipmentSlot = LazyOptional.of(() -> this.equipmentSlot);
        this.actionHandler = new EquipmentActionHandler(this, equipmentSlot);

        this.setAircraft((int) this.getAttributeValue(ModShipAttributes.AIRCRAFT.get()));
        this.setFuel((float) this.getAttributeValue(ModShipAttributes.FUEL.get()));
        this.setAmmo((float) this.getAttributeValue(ModShipAttributes.AMMO.get()));

        if (!this.level().isClientSide) {
            radar = new Radar(this, 1 * 20);
        }
        else {
            radar = null;
        }

    }

//    abstract protected void registerAttributes();

    @Override
    public void checkDespawn() {
        // IMPORTANT
        if (!this.isPersistenceRequired() && !this.requiresCustomPersistence()) {
            Entity entity = this.level().getNearestPlayer(this, -1.0D);
            Event.Result result = ForgeEventFactory.canEntityDespawn(this, (ServerLevel) this.level());
            if (result == Event.Result.DENY) {
                noActionTime = 0;
                entity = null;
            } else if (result == Event.Result.ALLOW) {
                this.discard();
                entity = null;
            }
            if (entity != null) {
                double d0 = entity.distanceToSqr(this);
                int i = this.getType().getCategory().getDespawnDistance();
                int j = i * i;
                if (d0 > (double)j && this.removeWhenFarAway(d0)) {
                    this.discard();
                }

                int k = this.getType().getCategory().getNoDespawnDistance();
                int l = k * k;
                if (this.noActionTime > 600 && this.random.nextInt(800) == 0 && d0 > (double)l && this.removeWhenFarAway(d0)) {
                    this.discard();
                } else if (d0 < (double)l) {
                    this.noActionTime = 0;
                }
            }

        } else {
            this.noActionTime = 0;
        }

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_AIRCRAFT, 0);
        this.entityData.define(DATA_FUEL, 0.0f);
        this.entityData.define(DATA_AMMO, 0.0f);
    }

    public void addShipAttributes(AttributeSupplier sup) {
        AttributeMap attributeMap = new AttributeMap(sup);

        this.getAttributes().assignValues(attributeMap);
    }

    public void setAircraft(int value) {
        this.entityData.set(DATA_AIRCRAFT, value);
    }

    public void setFuel(float value) {
        this.entityData.set(DATA_FUEL, value);
    }

    public void setAmmo(float value) {
        this.entityData.set(DATA_AMMO, value);
    }

    public int getAircraft() {
        return this.entityData.get(DATA_AIRCRAFT);
    }

    public float getFuel() {
        return this.entityData.get(DATA_FUEL);
    }

    public float getAmmo() {
        return this.entityData.get(DATA_AMMO);
    }

    public EquipmentType getSlotType(int i) {
        String str = this.entityData.get(DATA_EQUIPMENT_TYPES);
        int num = str.charAt(i) - '0';
        return EquipmentType.get(num);
    }

    public boolean hasAircraft() {
        return this.entityData.get(DATA_AIRCRAFT) > 0;
    }

    public boolean hasFuel() {
        return this.entityData.get(DATA_FUEL) > 0.0f;
    }

    public boolean hasAmmo() {
        return this.entityData.get(DATA_AMMO) > 0.0f;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
//        if (nbt.contains("basicentityship.shipattrs")) {
//            CompoundTag nbt1 = nbt.getCompound("basicentityship.shipattrs");
//                this.shipStats.deserializeNBT(nbt1);
//        }

        if (nbt.contains("equipmentslots.data")) {
            CompoundTag nbt2 = (CompoundTag) nbt.get("equipmentslots.data");
                equipmentSlot.deserializeNBT(nbt2);
                this.actionHandler.resetAllActions();
        }

        if (nbt.contains("basicentityship.canmelee")) {
            this.canMelee = nbt.getBoolean("basicentityship.canmelee");
        }

        if (nbt.contains("basicentityship.canpickupitem")) {
            this.canPickUpItem = nbt.getBoolean("basicentityship.canpickupitem");
        }

        if (nbt.contains("basicentityship.data_aircraft")) {
            this.entityData.set(DATA_AIRCRAFT, nbt.getInt("basicentityship.data_aircraft"));
        }
        if (nbt.contains("basicentityship.data_fuel")) {
            this.entityData.set(DATA_FUEL, nbt.getFloat("basicentityship.data_fuel"));
        }
        if (nbt.contains("basicentityship.data_ammo")) {
            this.entityData.set(DATA_AMMO, nbt.getFloat("basicentityship.data_ammo"));
        }

    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
//        nbt.put("basicentityship.shipattrs", this.shipStats.serializeNBT());
        nbt.put("equipmentslots.data", equipmentSlot.serializeNBT());
        nbt.putBoolean("basicentityship.canmelee", this.canMelee);
        nbt.putBoolean("basicentityship.canpickupitem", this.canPickUpItem);
        nbt.putInt("basicentityship.data_aircraft", this.entityData.get(DATA_AIRCRAFT));
        nbt.putFloat("basicentityship.data_fuel", this.entityData.get(DATA_FUEL));
        nbt.putFloat("basicentityship.data_ammo", this.entityData.get(DATA_AMMO));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        return capability == WEAPON_SLOTS ? this.lazyEquipmentSlot.cast() : super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.lazyEquipmentSlot.invalidate();
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) return;
        this.radar.tick();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ShipMenu(containerId, inventory, this);
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (player.level().isClientSide) {
            return InteractionResult.PASS;
        }

        if (player.isCrouching() && hand == InteractionHand.MAIN_HAND && player instanceof ServerPlayer serverPlayer) {

            NetworkHooks.openScreen(serverPlayer, this, (friendlyByteBuf -> {
                friendlyByteBuf.writeInt(this.getId());
                for (int i = 0; i < this.getAttributeValue(ModShipAttributes.SLOT_SIZE.get()); i++) {
                    ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer),  new EquipmentS2CPacket(this.equipmentSlot.getEquipments().get(i), i, this.getId()));
                }
            }));
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
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

    public void setEquipmentSlots(EquipmentSlots slots) {
        this.equipmentSlot = slots;
    }

    public EquipmentSlots getEquipmentSlots() {
        return equipmentSlot;
    }

    public EquipmentActionHandler getActionHandler() {
        return actionHandler;
    }

    public double getAttributeValue(Attribute attribute) {
        if (this.getAttributes().hasAttribute(attribute)) {
            return this.getAttributes().getValue(attribute);
        }
        else {
//            throw new IllegalStateException(String.format("No attribute %s at %s.", attribute.getDescriptionId(), this.getClass().getCanonicalName()));
            return Double.NEGATIVE_INFINITY;
        }
    }
}
