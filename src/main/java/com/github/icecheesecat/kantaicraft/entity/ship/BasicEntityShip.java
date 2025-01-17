package com.github.icecheesecat.kantaicraft.entity.ship;

import com.github.icecheesecat.kantaicraft.common.CommonEntityData;
import com.github.icecheesecat.kantaicraft.registries.ModActitvity;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.entity.IFaction;
import com.github.icecheesecat.kantaicraft.entity.IPhysicalEntity;
import com.github.icecheesecat.kantaicraft.registries.ModShipAttributes;
import com.github.icecheesecat.kantaicraft.menu.ShipMenu;
import com.github.icecheesecat.kantaicraft.stats.shipAttributes.IStatsGrowth;
import com.github.icecheesecat.kantaicraft.util.*;
import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class BasicEntityShip extends PathfinderMob implements MenuProvider, IStatsGrowth, IFaction<BasicEntityShip>, IPhysicalEntity, ISlotCheckerEntity, IShipClass {

    /**
     * ship attributes: hp, def, atk, ...
     */
    private static final EntityDataAccessor<Integer> DATA_AIRCRAFT = SynchedEntityData.defineId(BasicEntityShip.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_FUEL = SynchedEntityData.defineId(BasicEntityShip.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_AMMO = SynchedEntityData.defineId(BasicEntityShip.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_FACTION = SynchedEntityData.defineId(BasicEntityShip.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_GUARDING = SynchedEntityData.defineId(BasicEntityShip.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_CAN_MELEE = SynchedEntityData.defineId(BasicEntityShip.class, EntityDataSerializers.BOOLEAN);

    private ShipFields.ShipName shipName;

    private boolean canPickUpItem = false;
    private UUID owner;
    protected boolean debugMode = false;

    protected BasicEntityShip(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);

        this.setAircraft((int) this.getAttributeValue(ModShipAttributes.AIRCRAFT.get()));
        this.setFuel((float) this.getAttributeValue(ModShipAttributes.FUEL.get()));
        this.setAmmo((float) this.getAttributeValue(ModShipAttributes.AMMO.get()));
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
        this.entityData.define(DATA_AIRCRAFT, 0);
        this.entityData.define(DATA_FUEL, 100.0f);
        this.entityData.define(DATA_AMMO, 0.0f);
        this.entityData.define(DATA_FACTION, CommonEntityData.noFaction);
        this.entityData.define(DATA_IS_GUARDING, false);
        this.entityData.define(DATA_CAN_MELEE, false);
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

    public void useAmmo() {
        this.setAmmo(this.getAmmo() - this.getAmmoCost());
    }

    public boolean hasEnoughAmmo() {
        return this.getAmmo() > this.getAmmoCost();
    }

    public abstract float getAmmoCost();


    public int getAircraft() {
        return this.entityData.get(DATA_AIRCRAFT);
    }

    public float getFuel() {
        return this.entityData.get(DATA_FUEL);
    }

    public float getAmmo() {
        return this.entityData.get(DATA_AMMO);
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
    public int getFactionId() {
        return this.entityData.get(DATA_FACTION);
    }

    @Override
    public void setFactionId(int factionId) {
        this.entityData.set(DATA_FACTION, factionId);
    }

    @Override
    public boolean isEnemy(LivingEntity other) {
        return other instanceof BasicEntityShip ship && this.getFactionId() != ship.getFactionId();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);

        if (nbt.contains("basicentityship.canmelee")) {
            this.entityData.set(DATA_CAN_MELEE, nbt.getBoolean("basicentityship.canmelee"));
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
        if (nbt.contains("basicentityship.ship_name")) {
            this.shipName = ShipFields.ShipName.getEnum(nbt.getInt("basicentityship.ship_name"));
        }
        if (nbt.contains("basicentityship.owner")) {
            this.owner = nbt.getUUID("basicentityship.owner");
        }
        if (nbt.contains("basicentityship.isguarding")) {
            this.entityData.set(DATA_IS_GUARDING, nbt.getBoolean("basicentityship.isguarding"));
        }

    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        super.addAdditionalSaveData(nbt);

        nbt.putBoolean("basicentityship.canmelee", this.entityData.get(DATA_CAN_MELEE));
        nbt.putBoolean("basicentityship.canpickupitem", this.canPickUpItem);
        nbt.putInt("basicentityship.data_aircraft", this.entityData.get(DATA_AIRCRAFT));
        nbt.putFloat("basicentityship.data_fuel", this.entityData.get(DATA_FUEL));
        nbt.putFloat("basicentityship.data_ammo", this.entityData.get(DATA_AMMO));
        nbt.putInt("basicentityship.ship_name", this.shipName.ordinal());
        nbt.putUUID("basicentityship.owner", this.owner);
        nbt.putBoolean("basicentityship.isguarding", this.entityData.get(DATA_IS_GUARDING));
        nbt.putBoolean("basicentityship.canmelee", this.entityData.get(DATA_IS_GUARDING));
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) return;
        if (level().getGameTime() % 20 == 0)
            System.out.println(this.isGuarding());
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
            }));
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    public ShipFields.ShipName getShipName() {
        return shipName;
    }

    public void setShipName(ShipFields.ShipName shipName) {
        this.shipName = shipName;
    }

    public boolean isCanPickUpItem() {
        return canPickUpItem;
    }

    public void setCanPickUpItem(boolean canPickUpItem) {
        this.canPickUpItem = canPickUpItem;
    }

    public boolean canMelee() {
        return this.entityData.get(DATA_CAN_MELEE);
    }

    public void setCanMelee(boolean canMelee) {
        this.entityData.set(DATA_CAN_MELEE, canMelee);
    }

    public boolean isGuarding() {
        return this.entityData.get(DATA_IS_GUARDING);
    }

    public void setGuarding(boolean guarding) {
        if (guarding) {
            this.getBrain().setMemory(ModMemoryModuleType.IS_GUARDING.get(), Unit.INSTANCE);
        }
        else {
            this.getBrain().eraseMemory(ModMemoryModuleType.IS_GUARDING.get());
        }
        this.entityData.set(DATA_IS_GUARDING, guarding);
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

    @Override
    public boolean canStandOnFluid(FluidState fluidState) {
        return fluidState.is(FluidTags.WATER);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.updateActivity();

        if (this.level().getGameTime() % 100 != 0) return;
        this.getBrain().getMemories().forEach((m, ev) -> {
            System.out.println(m.toString() + ev.toString());
        });
        System.out.println();
        this.getBrain().getActiveActivities().forEach(System.out::println);
        System.out.println();
        System.out.println();
    }

    @Override
    public Brain<BasicEntityShip> getBrain() {
        return (Brain<BasicEntityShip>) super.getBrain();
    }

    protected void updateActivity() {
        this.getBrain().setActiveActivityToFirstValid(ImmutableList.of(ModActitvity.BURN_OUT_FUELS.get(), ModActitvity.GUARD.get(), Activity.IDLE));
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
}
