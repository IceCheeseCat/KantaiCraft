package com.github.icecheesecat.kantaicraft.entityship.entity;

import com.github.icecheesecat.kantaicraft.blueprint.Blueprint;
import com.github.icecheesecat.kantaicraft.equipment.handler.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.capability.EquipmentHandlerCapability;
import com.github.icecheesecat.kantaicraft.entityship.animation.BlinkAnimationControl;
import com.github.icecheesecat.kantaicraft.entityship.stance.Stance;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentClass;
import com.github.icecheesecat.kantaicraft.menu.ship.ShipMenu;
import com.github.icecheesecat.kantaicraft.navigation.ShipPathNavigation;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.equipment.EquipmentHandlerPacket;
import com.github.icecheesecat.kantaicraft.registries.*;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Unit;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class EntityShip extends PathfinderMob implements ISlotCheckerEntity, MenuProvider, GeoEntity, Stance {

    public static final EntityDataAccessor<Integer> DATA_AIRCRAFT = SynchedEntityData.defineId(EntityShip.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> DATA_FUEL = SynchedEntityData.defineId(EntityShip.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> DATA_AMMO = SynchedEntityData.defineId(EntityShip.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Boolean> DATA_FORCE_MELEE = SynchedEntityData.defineId(EntityShip.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<ShipAnimationState> DATA_ANIMATION_STATE = SynchedEntityData.defineId(EntityShip.class, ModEntityDataSerializer.ANIMATION_STATE_SERIALIZER.get());
//    public static final EntityDataAccessor<EmotionState> DATA_EMOTION_STATE = SynchedEntityData.defineId(EntityShip.class, ModEntityDataSerializer.EMOTION_STATE_SERIALIZER.get());
    public static final EntityDataAccessor<ShipLeveling> DATA_SHIP_LEVEL = SynchedEntityData.defineId(EntityShip.class, ModEntityDataSerializer.SHIP_LEVEL_SERIALIZER.get());
    public static final EntityDataAccessor<Float> DATA_SPEED_MODIFIER = SynchedEntityData.defineId(EntityShip.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Boolean> DATA_IS_GUARDING = SynchedEntityData.defineId(EntityShip.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Optional<UUID>> DATA_SHIP_OWNER = SynchedEntityData.defineId(EntityShip.class, EntityDataSerializers.OPTIONAL_UUID);
    public static final EntityDataAccessor<Boolean> DATA_SIT_DOWN = SynchedEntityData.defineId(EntityShip.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> DATA_WONDER_AROUND = SynchedEntityData.defineId(EntityShip.class, EntityDataSerializers.BOOLEAN);

    public static final EntityDataAccessor<Integer> DATA_FOLLOW_DISTANCE = SynchedEntityData.defineId(EntityShip.class, EntityDataSerializers.INT);

    private ShipAnimationState prevAnimationShipAnimationState;
    protected final List<EquipmentClass> equippableTypes;
    private final BlinkAnimationControl blinkAnimationControl = new BlinkAnimationControl(60, 80, this.random);
    private long lastEmotionChangedTick = -1;
    private final ShipClass shipClass;
    private final EquippableSlots equippableSlots;

    public EntityShip(EntityType<? extends PathfinderMob> entityType, ShipClass shipClass, Level level, List<EquipmentClass> equippableTypes) {
        super(entityType, level);
        this.equippableTypes = ImmutableList.copyOf(equippableTypes);
        this.getCapability(EquipmentHandlerCapability.TOKEN).ifPresent(this::defaultEquipments);
        this.prevAnimationShipAnimationState = ShipAnimationState.IDLE;
        this.shipClass = shipClass;
        this.equippableSlots = this.defineEquippableSlots();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

        this.entityData.define(DATA_IS_GUARDING, false);
        this.entityData.define(DATA_FORCE_MELEE, false);
        this.entityData.define(DATA_ANIMATION_STATE, ShipAnimationState.IDLE);
//        this.entityData.define(DATA_EMOTION_STATE, EmotionState.NORMAL);
        this.entityData.define(DATA_SHIP_OWNER, Optional.empty());
        this.entityData.define(DATA_SPEED_MODIFIER, 0.4f);
        this.entityData.define(DATA_FOLLOW_DISTANCE, 10);
        this.entityData.define(DATA_SIT_DOWN, false);
        this.entityData.define(DATA_WONDER_AROUND, false);
        this.setupSyncedDataFromStance(entityData, this.random);
    }

    protected abstract EquippableSlots defineEquippableSlots();

    protected abstract void defaultEquipments(EquipmentHandler equipmentHandler);

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

    public boolean hasNoFuel() {
        return !this.hasFuel();
    }

    public boolean notEnoughAmmo() {
        return !this.hasEnoughAmmo();
    }

    public boolean hasNoAirCraft() {
        return !this.hasAircraft();
    }

    public boolean hasAmmo() {
        return this.entityData.get(DATA_AMMO) > 0.0f;
    }

    public int getFollowOwnerDistance() {
        return this.entityData.get(DATA_FOLLOW_DISTANCE);
    }

    public int getFollowTooCloseDistance() {
        return 3;
    }

    public void setFollowOwnerDistance(int num) {
        this.entityData.set(DATA_FOLLOW_DISTANCE, num);
    }

    public void toggleWonderAround() {
        this.entityData.set(DATA_WONDER_AROUND, !this.canWonderAround());
    }

    public boolean canWonderAround() {
        return this.entityData.get(DATA_WONDER_AROUND);
    }

    public boolean isSitDown() {
        return this.entityData.get(DATA_SIT_DOWN);
    }

    public void toggleSitDown() {
        this.entityData.set(DATA_SIT_DOWN, !this.isSitDown());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.put("playerKantaiData", saveAsPlayerKantaiDataTag());
    }

    public CompoundTag saveAsPlayerKantaiDataTag() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("forcemelee", this.entityData.get(DATA_FORCE_MELEE));
        nbt.putInt("data_aircraft", this.entityData.get(DATA_AIRCRAFT));
        nbt.putFloat("data_fuel", this.entityData.get(DATA_FUEL));
        nbt.putFloat("data_ammo", this.entityData.get(DATA_AMMO));
        nbt.putInt("animation_state", this.entityData.get(DATA_ANIMATION_STATE).ordinal());
        nbt.putInt("previous_animation_state", this.prevAnimationShipAnimationState.ordinal());
//        nbt.putInt("emotion_state", this.entityData.get(DATA_EMOTION_STATE).ordinal());
        nbt.putLong("last_emotion_changed_tick", this.lastEmotionChangedTick);
        nbt.put("shiplevel", this.entityData.get(DATA_SHIP_LEVEL).serializeNBT());
        nbt.putFloat("speedmodifier", this.getNormalSpeedModifier());
        if (this.getShipInventory() != null)
            nbt.put("inventory", this.saveInventory());
        nbt.putBoolean("isguarding", this.entityData.get(DATA_IS_GUARDING));
        this.entityData.get(DATA_SHIP_OWNER).ifPresent(uuid ->
                nbt.putUUID("shipowner", uuid));
        nbt.putInt("follow_distance", this.getFollowOwnerDistance());
        nbt.putBoolean("sit_down", this.isSitDown());
        nbt.put("equipmentHandler", this.equipmentHandler.serializeNBT());
        nbt.putString("entityType", this.getType().toString());
        nbt.putBoolean("wonder_around", this.canWonderAround());

        return nbt;
    }

    protected CompoundTag saveInventory() {
        CompoundTag nbt = new CompoundTag();
        ListTag listtag = new ListTag();

        for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putByte("Slot", (byte)i);
                itemstack.save(compoundtag);
                listtag.add(compoundtag);
            }
        }

        nbt.put("Items", listtag);

        return nbt;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);

        this.loadPlayerKantaiDataTag(nbt.getCompound("playerKantaiData"));

    }

    public void loadPlayerKantaiDataTag(CompoundTag nbt) {
        if (nbt.contains("forcemelee")) {
            this.entityData.set(DATA_FORCE_MELEE, nbt.getBoolean("forcemelee"));
        }
        if (nbt.contains("data_aircraft")) {
            this.entityData.set(DATA_AIRCRAFT, nbt.getInt("data_aircraft"));
        }
        if (nbt.contains("data_fuel")) {
            this.entityData.set(DATA_FUEL, nbt.getFloat("data_fuel"));
        }
        if (nbt.contains("data_ammo")) {
            this.entityData.set(DATA_AMMO, nbt.getFloat("data_ammo"));
        }
        if (nbt.contains("animation_state")) {
            this.entityData.set(DATA_ANIMATION_STATE, ShipAnimationState.create(nbt.getInt("animation_state")));
        }
        if (nbt.contains("previous_animation_state")) {
            this.prevAnimationShipAnimationState = ShipAnimationState.create(nbt.getInt("previous_animation_state"));
        }
//        if (nbt.contains("emotion_state")) {
//            this.entityData.set(DATA_EMOTION_STATE, EmotionState.create(nbt.getInt("emotion_state")));
//        }
        if (nbt.contains("last_emotion_changed_tick")) {
            this.lastEmotionChangedTick = nbt.getLong("last_emotion_changed_tick");
        }
        if (nbt.contains("shiplevel")) {
            this.entityData.get(DATA_SHIP_LEVEL).deserializeNBT((CompoundTag) nbt.get("shiplevel"));
        }
        if (nbt.contains("speedmodifier")) {
            this.entityData.set(DATA_SPEED_MODIFIER, nbt.getFloat("speedmodifier"));
        }
        if (nbt.contains("inventory")) {
            this.loadInventory(nbt.getCompound("inventory"));
        }
        if (nbt.contains("basicentityship.isguarding")) {
            this.entityData.set(DATA_IS_GUARDING, nbt.getBoolean("basicentityship.isguarding"));
        }
        if (nbt.contains("shipowner")) {
            this.entityData.set(DATA_SHIP_OWNER, Optional.of(nbt.getUUID("shipowner")));
        }
        if (nbt.contains("follow_distance")) {
            this.setFollowOwnerDistance(nbt.getInt("follow_distance"));
        }
        if (nbt.contains("sit_down")) {
            this.entityData.set(DATA_SIT_DOWN, nbt.getBoolean("sit_down"));
        }
        if (nbt.contains("equipmentHandler")) {
            this.equipmentHandler.deserializeNBT(nbt.getCompound("equipmentHandler"));
        }
        if (nbt.contains("wonder_around")) {
            this.entityData.set(DATA_WONDER_AROUND, nbt.getBoolean("wonder_around"));
        }
    }

    protected void loadInventory(CompoundTag nbt) {
        ListTag listtag = nbt.getList("Items", 10);

        for(int i = 0; i < listtag.size(); ++i) {
            CompoundTag compoundtag = listtag.getCompound(i);
            int j = compoundtag.getByte("Slot") & 255;
            if (j >= 2 && j < this.inventory.getContainerSize()) {
                this.inventory.setItem(j, ItemStack.of(compoundtag));
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide) {
            //server side
            if (!continueAnimationState()) {
                this.setAnimationState(prevAnimationShipAnimationState);
            }

            if (this.walkAnimation.isMoving()) {
                if (this.walkAnimation.speed() > 0.4f) {
                    this.setAnimationState(ShipAnimationState.RUN);
                }
                else {
                    this.setAnimationState(ShipAnimationState.WALK);
                }
            }

            syncEquipmentHandler();
            printServerDebug();
        }
        else {
            // client side
            printClientDebug();
        }
    }

    private void printServerDebug() {
//            this.getBrain().getActiveNonCoreActivity().ifPresent(System.out::println);
//            System.out.println("walk target: ");
//            this.getBrain().getMemory(MemoryModuleType.WALK_TARGET).ifPresent(System.out::println);
//            System.out.println("attack target: ");
//            this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent(System.out::println);
//            System.out.println("path: ");
//            this.getBrain().getMemory(MemoryModuleType.PATH).ifPresent(System.out::println);
//            System.out.println("look target: ");
//            this.getBrain().getMemory(MemoryModuleType.LOOK_TARGET).ifPresent(System.out::println);
    }

    private void printClientDebug() {
//        System.out.println("id: " + this.getId());
//        this.getCapability(EquipmentHandlerCapability.TOKEN).ifPresent(System.out::println);
    }

    protected boolean continueAnimationState() {
         return switch (this.getAnimationState()) {
            case IDLE -> true;
            case WALK -> this.navigation.isInProgress();
            case RUN -> this.navigation.isInProgress() && this.walkAnimation.speed() > 0.4f;
            default -> {
                System.out.println("Not implemented state, return to IDLE state.");
                yield false;
            }
        };
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {

        super.onSyncedDataUpdated(pKey);
    }

    public void syncEquipmentHandler() {
        this.getCapability(EquipmentHandlerCapability.TOKEN).ifPresent(
                equipmentHandler1 -> {
                    if (this.equipmentHandler.hasAnyDirty()) {
                        ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), EquipmentHandlerPacket.dirtyHandlerPacket(this.getId(), equipmentHandler1));
                    }
                }
        );
    }

    public boolean forceMelee() {
        return this.entityData.get(DATA_FORCE_MELEE);
    }

    public void setForceMelee(boolean canMelee) {
        this.entityData.set(DATA_FORCE_MELEE, canMelee);
    }

    public boolean canRangeAttack() {
        return this.equipmentHandler.hasRangeAttackWeapon() && hasEnoughAmmo();
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
    }

    @Override
    public Brain<EntityShip> getBrain() {
        return (Brain<EntityShip>) super.getBrain();
    }

    protected void updateActivity() {
        this.getBrain().setActiveActivityToFirstValid(ImmutableList.of(ModActitvity.BURN_OUT_FUELS.get(), Activity.FIGHT, Activity.IDLE));
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
//        this.getShipInventory().addItem(itemEntity.getItem());
    }

    public boolean hasAttackableEquipment() {
        return this.equipmentHandler.getEquipments().stream().anyMatch(equipment -> this.equippableTypes.contains(equipment.getEquipmentClass()));
    }

    public ShipAnimationState getAnimationState() {
        return this.entityData.get(DATA_ANIMATION_STATE);
    }

    public void setAnimationState(ShipAnimationState shipAnimationState) {
        ShipAnimationState previousShipAnimationState = this.getAnimationState();
        if (previousShipAnimationState.isMainState()) {
            this.prevAnimationShipAnimationState = previousShipAnimationState;
        }

        this.entityData.set(DATA_ANIMATION_STATE, shipAnimationState);
    }


//    public void setEmotionState(EmotionState emotionState, long lastEmotionChangedTick) {
//        if (!emotionState.isConsistent()) {
//            this.lastEmotionChangedTick = lastEmotionChangedTick;
//        }
//        this.entityData.set(DATA_EMOTION_STATE, emotionState);
//    }
//
//    public EmotionState getEmotionState() {
//        return this.entityData.get(DATA_EMOTION_STATE);
//    }

//    private void tickEmotionState(long currentTick) {
//        if (this.getEmotionState().isConsistent()) return;
//        if (currentTick >= this.lastEmotionChangedTick + this.getEmotionState().getDuration()) {
//            this.setEmotionState(EmotionState.NORMAL, -1);
//        }
//    }
//
//    protected void changeEmotion() {
//        this.getBrain().getActiveNonCoreActivity().ifPresent(activity -> {
//                if (activity.equals(Activity.FIGHT)) {
//                    this.setEmotionState(EmotionState.SERIOUS, this.tickCount);
//                }
//            }
//        );
//    }

    public int getShipLevel() {
        return this.entityData.get(DATA_SHIP_LEVEL).getLevel();
    }

    public ShipLeveling getShipLeveling() {
        return this.entityData.get(DATA_SHIP_LEVEL);
    }

    public int getShipExp() {
        return this.entityData.get(DATA_SHIP_LEVEL).getExp();
    }

    public abstract int getProcessTime();

    // save blueprint to itemstack nbt
    public Blueprint makeBlueprint() {
        return isHostileSide() ? Blueprint.createWithLevelZero(this) :
                Blueprint.create(this);
    }

    public abstract Rarity getRarity();

    public float getNormalSpeedModifier() {
        return this.entityData.get(DATA_SPEED_MODIFIER);
    }

    public float getRunSpeedModifier() {
        return this.entityData.get(DATA_SPEED_MODIFIER) * 2.5f;
    }

    public ShipClass getShipClass() {
        return this.shipClass;
    }

    SimpleContainer inventory = this.createShipInventory();
    LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> new InvWrapper(inventory));

    protected SimpleContainer createShipInventory() {
        return this.isHostileSide() ? null : new SimpleContainer(36);
    }

    public boolean hasInventory() {
        return this.inventory != null;
    }

    /**
     * {@link EquipmentHandler}
     */
    EquipmentHandler equipmentHandler = new EquipmentHandler(4);
    LazyOptional<EquipmentHandler> equipmentHandlerLazyOptional = LazyOptional.of(() -> equipmentHandler);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        if (capability == EquipmentHandlerCapability.TOKEN) {
            return  equipmentHandlerLazyOptional.cast();
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        equipmentHandlerLazyOptional.invalidate();
    }

    public void equipmentHandlerConsumer(NonNullConsumer<EquipmentHandler> consumer) {
        this.getCapability(EquipmentHandlerCapability.TOKEN).ifPresent(consumer);
    }

    public SimpleContainer getShipInventory() {
        return this.inventory;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
        // drop a blueprint of this ship
        ItemStack itemStack = new ItemStack(ModItem.SHIP_BLUEPRINT.get());
        itemStack.setTag(this.makeBlueprint().serializeNBT());
        ItemEntity blueprintEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), itemStack);
        level().addFreshEntity(blueprintEntity);

        // drop all item in ship's inventory
        if (hasInventory()) {
            for (int i = 0; i < this.inventory.getMaxStackSize(); i++) {
                ItemStack stack = this.inventory.getItem(i);
                if (stack.isEmpty()) continue;
                ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), stack);
                level().addFreshEntity(itemEntity);
            }
        }

    }

    public boolean isPlayerShip() {
        return this.isPlayerSide();
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

    public Optional<UUID> getShipOwner() {
        return this.entityData.get(DATA_SHIP_OWNER);
    }

    @Nullable
    public LivingEntity getOwnerEntity() {
        if (this.getShipOwner().isEmpty()) return null;
        if (this.level() instanceof ClientLevel clientLevel) {
            return null; // TODO request from client to get Entity id
        }
        else if (this.level() instanceof ServerLevel serverLevel) {
            var entity = serverLevel.getEntity(this.getShipOwner().get());
            return entity instanceof LivingEntity ? (LivingEntity) entity : null;
        }
        return null;
    }

    public void setShipOwner(UUID uuid) {
        if (this.isHostileSide()) return;
        this.entityData.set(DATA_SHIP_OWNER, Optional.of(uuid));
    }

    public boolean isShipOwner(Player player) {
        if (this.isHostileSide()) return false;
        return this.getShipOwner().isPresent() && this.getShipOwner().get().compareTo(player.getUUID()) == 0;
    }

    public boolean hasSameShipOwner(EntityShip entityShip) {
        if (this.isHostileSide()) return false;
        return this.getShipOwner().isPresent() && entityShip.getShipOwner().isPresent() && this.getShipOwner().get().compareTo(entityShip.getShipOwner().get()) == 0;
    }

    public boolean hasSameOwner(TamableAnimal tamableAnimal) {
        if (this.getShipOwner().isEmpty()) return false;
        UUID ownerUUID = this.getShipOwner().get();
        if (tamableAnimal.getOwnerUUID() == null) return false;

        return tamableAnimal.getOwnerUUID().compareTo(ownerUUID) == 0;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!super.hurt(pSource, pAmount)) {
            return false;
        }

        if (pSource.getEntity() != null && pSource.getEntity().is(this)) {
            return false;
        }

        if (pSource.getEntity() instanceof LivingEntity livingEntity) {
            // damage from shipowner
            if (livingEntity instanceof Player player && this.isShipOwner(player)) {
                return true;
            }

            this.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, livingEntity);
        }

        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource pSource) {
        return pSource.is(DamageTypes.FALL) || super.isInvulnerableTo(pSource);
    }

    /**
     * Only {@link EntityShip} has the full damage output on entity
     */
    @Override
    protected void actuallyHurt(DamageSource pDamageSource, float pDamageAmount) {
        if (pDamageSource.is(DamageTypes.GENERIC_KILL)) {
            super.actuallyHurt(pDamageSource, pDamageAmount);
        }
        else if (pDamageSource.getEntity() instanceof EntityShip entityShip) {
            super.actuallyHurt(pDamageSource, pDamageAmount);
        }
        else if (pDamageSource.is(DamageTypes.PLAYER_ATTACK) && isShipOwner((Player) pDamageSource.getEntity())) {
            super.actuallyHurt(pDamageSource, pDamageAmount);
        }
        else {
            super.actuallyHurt(pDamageSource, 1.0f);
        }
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity pTarget) {
        if (pTarget.is(this)) {
            return false;
        }
        if (pTarget instanceof Player player) {
            return !this.isShipOwner(player);
        }
        if (pTarget instanceof EntityShip entityShip) {
            return !this.hasSameShipOwner(entityShip);
        }
        if (pTarget instanceof TamableAnimal tamableAnimal) {
            return !tamableAnimal.isTame();
        }

        return true;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ShipMenu(pContainerId, pPlayerInventory, this);
    }

    @Override
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (pPlayer.level().isClientSide) {
            return InteractionResult.PASS;
        }
        if (this.isHostileSide()) {
            return InteractionResult.PASS;
        }

        if (pHand == InteractionHand.MAIN_HAND && pPlayer.isShiftKeyDown()) {
            if (!this.isSitDown())  {
                orderedToSit();
            }
            this.toggleSitDown();
            return InteractionResult.SUCCESS;
        }

        if (pHand == InteractionHand.MAIN_HAND && pPlayer instanceof ServerPlayer serverPlayer) {
            ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), EquipmentHandlerPacket.wholeHandlerPacket(this.getId(), this.equipmentHandler));
            NetworkHooks.openScreen(serverPlayer, this, (friendlyByteBuf -> {
                friendlyByteBuf.writeInt(this.getId());
            }));
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    protected void orderedToSit() {
        this.navigation.stop();
        this.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        this.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isPersistenceRequired() {
        return this.getType().getCategory().isPersistent();
    }

    /**
     * {@link GeckoLib}
     */

    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);
    protected static final RawAnimation WALKING_ANIMATION = RawAnimation.begin().thenLoop("walk");
//    protected static final RawAnimation RUNNING_ANIMATION = RawAnimation.begin().thenLoop("run");
    protected static final RawAnimation BLINK_ANIMATION = RawAnimation.begin().thenPlay("blink");
//    protected static final RawAnimation NORMAL_EXPRESSION = RawAnimation.begin().thenPlayAndHold("facial.normal");
//    protected static final RawAnimation SHOCK_EXPRESSION = RawAnimation.begin().thenPlayAndHold("facial.shock");
//    protected static final RawAnimation SAD_EXPRESSION = RawAnimation.begin().thenPlayAndHold("facial.sad");
//    protected static final RawAnimation SERIOUS_EXPRESSION = RawAnimation.begin().thenPlayAndHold("facial.serious");
//    protected static final RawAnimation HAPPY_EXPRESSION = RawAnimation.begin().thenPlayAndHold("facial.happy");
//    protected static final RawAnimation ANGRY_EXPRESSION = RawAnimation.begin().thenPlayAndHold("facial.angry");
    protected static final RawAnimation BREATH_ANIMATION = RawAnimation.begin().thenLoop("breath");
    protected static final RawAnimation DUCK_POSE_ANIMATION = RawAnimation.begin().thenPlayAndHold("duck_pose");

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animatableInstanceCache;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, this::moveAnimationController));
//        controllers.add(new AnimationController<>(this, "expression", 5, this::expressionController));
        controllers.add(new AnimationController<>(this, "blink", 5, this::blinkAnimationController));
        controllers.add(new AnimationController<>(this, "idle", 5, this::idleAnimationController));
        controllers.add(new AnimationController<>(this, "sit", 5, this::sitAnimationController));
    }

    protected <E extends EntityShip> PlayState moveAnimationController(final AnimationState<E> event) {
        if (event.isMoving() || (this.walkAnimation.isMoving() && this.walkAnimation.speed() > 0.05f)) {
//            if (this.walkAnimation.speed() < 0.3f)
                return event.setAndContinue(WALKING_ANIMATION);
//            else
//                return event.setAndContinue(RUNNING_ANIMATION);
        }

        return PlayState.STOP;
    }

//    protected <E extends EntityShip> PlayState expressionController(final AnimationState<E> event) {
//        return switch (this.getEmotionState()) {
//            case NORMAL -> event.setAndContinue(NORMAL_EXPRESSION);
//            case HAPPY -> event.setAndContinue(HAPPY_EXPRESSION);
//            case SAD -> event.setAndContinue(SAD_EXPRESSION);
//            case ANGRY -> event.setAndContinue(ANGRY_EXPRESSION);
//            case SERIOUS -> event.setAndContinue(SERIOUS_EXPRESSION);
//            case SHOCK -> event.setAndContinue(SHOCK_EXPRESSION);
//        };
//    }


    protected <E extends EntityShip> PlayState blinkAnimationController(final AnimationState<E> event) {
        if (this.blinkAnimationControl.canAnimate(this.tickCount)) {
            event.getController().forceAnimationReset();
            event.getController().setAnimation(BLINK_ANIMATION);
        }
        return PlayState.CONTINUE;
    }

    protected <E extends EntityShip> PlayState idleAnimationController(final AnimationState<E> event) {
        if (this.getBrain().getActiveNonCoreActivity().isPresent() && this.getBrain().getActiveNonCoreActivity().get() == Activity.IDLE && !isWalkingOrRunning()) { // BUG : walking animation cannot play with breath animation (because they use same part of the body?)
            return event.setAndContinue(BREATH_ANIMATION);
        }

        return PlayState.STOP;
    }

    protected <E extends EntityShip> PlayState sitAnimationController(final AnimationState<E> event) {
        if (this.isSitDown()) {
            return event.setAndContinue(DUCK_POSE_ANIMATION);
        }
        event.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    private boolean isWalkingOrRunning() {
        return this.walkAnimation.isMoving() && this.walkAnimation.speed() > 0.05f;
    }

}
