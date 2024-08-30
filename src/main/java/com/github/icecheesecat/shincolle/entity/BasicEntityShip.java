package com.github.icecheesecat.shincolle.entity;

import com.github.icecheesecat.shincolle.Shincolle;
import com.github.icecheesecat.shincolle.menu.ShipMenu;
import com.github.icecheesecat.shincolle.util.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Pig;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BasicEntityShip extends PathfinderMob implements MenuProvider {

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

    protected final EquipmentSlots equipmentSlot;
    private final LazyOptional<EquipmentSlots> lazyEquipmentSlot;
//    private ItemSlots itemSlots;
    public static final Capability<EquipmentSlots> WEAPON_SLOTS = CapabilityManager.get(new CapabilityToken<>(){});

    protected final EquipmentActionHandler actionHandler;

    protected BasicEntityShip(EntityType<? extends PathfinderMob> entityType, Level level, EquipmentSlots equipmentSlot) {
        super(entityType, level);
        this.equipmentSlot = equipmentSlot;
        this.lazyEquipmentSlot = LazyOptional.of(() -> this.equipmentSlot);
        this.actionHandler = new EquipmentActionHandler(this, equipmentSlot);

        radar = new Radar(this, 1 * 20);
    }

    public static AttributeSupplier.Builder createAtrributes() {
        return Pig.createAttributes();
    }

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
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("basicentityship.shipattrs")) {
            CompoundTag nbt1 = nbt.getCompound("basicentityship.shipattrs");
                this.shipAttrs.deserializeNBT(nbt1);
        }

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

    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.put("basicentityship.shipattrs", this.shipAttrs.serializeNBT());
        nbt.put("equipmentslots.data", equipmentSlot.serializeNBT());
        nbt.putBoolean("basicentityship.canmelee", this.canMelee);
        nbt.putBoolean("basicentityship.canpickupitem", this.canPickUpItem);
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
    public Component getDisplayName() {
        return Component.translatable("menu.shincolle.shipmenu");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ShipMenu(containerId, inventory, this, this.equipmentSlot);
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (player.level().isClientSide) {
            return InteractionResult.PASS;
        }

        if (player.isCrouching() && hand == InteractionHand.MAIN_HAND && player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, this, this.equipmentSlot::writeBuffer);
            return InteractionResult.SUCCESS;
        }


        return InteractionResult.PASS;
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

    public EquipmentSlots getWeaponSlot() {
        return equipmentSlot;
    }

    public EquipmentActionHandler getActionHandler() {
        return actionHandler;
    }
}
