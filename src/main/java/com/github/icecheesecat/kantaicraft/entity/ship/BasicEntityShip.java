package com.github.icecheesecat.kantaicraft.entity.ship;

import com.github.icecheesecat.kantaicraft.capability.EquipmentHandlerCapability;
import com.github.icecheesecat.kantaicraft.capability.ShipBlueprintCapability;
import com.github.icecheesecat.kantaicraft.common.CommonEntityData;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import com.github.icecheesecat.kantaicraft.item.ShipBlueprintData;
import com.github.icecheesecat.kantaicraft.navigation.ShipPathNavigation;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.SyncShipPacket;
import com.github.icecheesecat.kantaicraft.network.packet.SyncType;
import com.github.icecheesecat.kantaicraft.registries.ModActitvity;
import com.github.icecheesecat.kantaicraft.registries.ModAttribute;
import com.github.icecheesecat.kantaicraft.registries.ModItem;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.entity.IFaction;
import com.github.icecheesecat.kantaicraft.entity.IPhysicalEntity;
import com.github.icecheesecat.kantaicraft.menu.ship.ShipMenu;
import com.github.icecheesecat.kantaicraft.stats.shipAttributes.IStatsGrowth;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BasicEntityShip extends PathfinderMob implements MenuProvider, IStatsGrowth, IFaction<BasicEntityShip>, IPhysicalEntity, ISlotCheckerEntity, IShipField, IEquipmentSelector {

    /**
     * ship attributes: hp, def, atk, ...
     */
    private static final EntityDataAccessor<Integer> DATA_AIRCRAFT = SynchedEntityData.defineId(BasicEntityShip.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_FUEL = SynchedEntityData.defineId(BasicEntityShip.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_AMMO = SynchedEntityData.defineId(BasicEntityShip.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_FACTION = SynchedEntityData.defineId(BasicEntityShip.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> DATA_IS_GUARDING = SynchedEntityData.defineId(BasicEntityShip.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> DATA_CAN_MELEE = SynchedEntityData.defineId(BasicEntityShip.class, EntityDataSerializers.BOOLEAN);
    private boolean canPickUpItem = false;
    private UUID owner;
    protected boolean debugMode = false;
    protected final List<EquipmentType> attackbleEquipmentTypes;

    public BasicEntityShip(EntityType<? extends PathfinderMob> entityType, Level level, List<EquipmentType> attackbleEquipmentTypes) {
        super(entityType, level);

        this.setAircraft((int) this.getAttributeValue(ModAttribute.AIRCRAFT.get()));
        this.setFuel((float) this.getAttributeValue(ModAttribute.FUEL.get()));
        this.setAmmo((float) this.getAttributeValue(ModAttribute.AMMO.get()));
        this.attackbleEquipmentTypes = ImmutableList.copyOf(attackbleEquipmentTypes);
        this.initEquipments();
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

    protected abstract void initEquipments();

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new ShipPathNavigation(this, pLevel);
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
        if (other.getUUID() == this.owner) return false;
        if (other.is(this)) return false;
        if (other instanceof BasicEntityShip ship) {
            return this.getFactionId() != ship.getFactionId();
        }
        if (other instanceof Monster) {
            return true;
        }

        return false;
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
        if (nbt.contains("basicentityship.owner")) {
            this.owner = nbt.getUUID("basicentityship.owner");
        }
        if (nbt.contains("basicentityship.isguarding")) {
            this.entityData.set(DATA_IS_GUARDING, nbt.getBoolean("basicentityship.isguarding"));
        }
        if (nbt.contains("basicentityship.inventory")) {
            this.inventory.deserializeNBT((CompoundTag) nbt.get("basicentityship.inventory"));
        }

    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);

        nbt.putBoolean("basicentityship.canmelee", this.entityData.get(DATA_CAN_MELEE));
        nbt.putBoolean("basicentityship.canpickupitem", this.canPickUpItem);
        nbt.putInt("basicentityship.data_aircraft", this.entityData.get(DATA_AIRCRAFT));
        nbt.putFloat("basicentityship.data_fuel", this.entityData.get(DATA_FUEL));
        nbt.putFloat("basicentityship.data_ammo", this.entityData.get(DATA_AMMO));
        nbt.putUUID("basicentityship.owner", this.owner);
        nbt.putBoolean("basicentityship.isguarding", this.entityData.get(DATA_IS_GUARDING));
        nbt.putBoolean("basicentityship.canmelee", this.entityData.get(DATA_IS_GUARDING));
        nbt.put("basicentityship.inventory", this.inventory.serializeNBT());

    }



    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) return;
        if (level().getGameTime() % 20 != 0) return;

        broadcastEquipmentHandler();
    }

    public void broadcastEquipmentHandler() {
        this.getCapability(EquipmentHandlerCapability.TOKEN).ifPresent(
                handler -> {
                    for (int i = 0; i < handler.getSlotSize(); i++) {
                        ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new SyncShipPacket(SyncType.EQUIPMENT, this.getId(), handler.getEquipment(i), (byte) i));
                    }
                }
        );
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
//        this.getBrain().getMemories().forEach((m, ev) -> {
//            System.out.println(m.toString() + ev.toString());
//        });
//        System.out.println();
//        this.getBrain().getActiveActivities().forEach(System.out::println);
//        System.out.println();
//        System.out.println();
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

    public void shipPickUpItem(ItemEntity itemEntity) {

        this.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(
                handler -> {
                    ItemStack itemStack = itemEntity.getItem();
                    for (int i = 0; i < handler.getSlots(); i++) {
                        if (handler.isItemValid(i, itemStack)) {
                            itemStack = handler.insertItem(i, itemStack, false);
                            itemEntity.setItem(itemStack);
                        }
                        if (itemStack.isEmpty()) {
                            break;
                        }
                    }
                }
        );

    }

    public boolean shipCanPickUp(ItemEntity itemEntity) {
        ItemStack stack = itemEntity.getItem().copy();
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            stack = this.inventory.insertItem(i, stack, true);
            if (stack.isEmpty()) break;
        }

        if (stack.isEmpty()) return true;
        return false;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.getCapabilities().invalidate();
    }

    ItemStackHandler inventory = new ItemStackHandler((int) this.getAttributeValue(ModAttribute.SLOT_SIZE.get()));
    ItemStackHandler simulateInventory = new ItemStackHandler((int) this.getAttributeValue(ModAttribute.SLOT_SIZE.get()));
    LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> inventory);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(capability, facing);
    }

    public IItemHandler getShipInventory() {
        if (this.level().isClientSide) return null;
        return this.inventory;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            ItemStack stack = this.inventory.extractItem(i, this.inventory.getSlotLimit(i), false);
            if (stack.isEmpty()) continue;
            ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), stack);
            level().addFreshEntity(itemEntity);
        }

        // drop a blueprint of this ship
        UUID builder = getBuilder(pSource);
        if (builder != null) {
            ItemEntity blueprintEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), this.makeBlueprint(pSource.getEntity().getUUID()));
            level().addFreshEntity(blueprintEntity);
        }
    }

    private UUID getBuilder(DamageSource source) {
        if (source.getEntity() instanceof BasicEntityShip ship) {
            if (ship.owner != null) {
                return ship.owner;
            }
        }

        if (source.getEntity() instanceof Player player) {
            return player.getUUID();
        }

        return null;
    }

    public boolean hasAttackableEquipment() {
        AtomicBoolean r = new AtomicBoolean(false);
        this.getCapability(EquipmentHandlerCapability.TOKEN).ifPresent(
                handler -> {
                    r.set(handler.getEquipments().stream().anyMatch(e -> this.attackbleEquipmentTypes.contains(e.getType())));
                }
        );

        return r.get();

    }

    public ItemStack makeBlueprint(UUID builder) {
        ItemStack blueprint = new ItemStack(ModItem.SHIP_BLUEPRINT.get());
        blueprint.getCapability(ShipBlueprintCapability.TOKEN).ifPresent(
                data -> {
                    data.set(ShipBlueprintData.create(this, builder));
                }
        );

        return blueprint;
    }
}
