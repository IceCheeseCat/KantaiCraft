package com.github.icecheesecat.kantaicraft.entityship.entity;

import com.github.icecheesecat.kantaicraft.blueprint.Blueprint;
import com.github.icecheesecat.kantaicraft.capability.equipment.EquipmentHandlerCapability;
import com.github.icecheesecat.kantaicraft.entityship.animation.BlinkAnimationControl;
import com.github.icecheesecat.kantaicraft.entityship.entity.features.*;
import com.github.icecheesecat.kantaicraft.entityship.stance.HostileStance;
import com.github.icecheesecat.kantaicraft.entityship.stance.Stance;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentClass;
import com.github.icecheesecat.kantaicraft.equipment.handler.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.menu.ship.ShipMenu;
import com.github.icecheesecat.kantaicraft.navigation.ShipPathNavigation;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.equipment.EquipmentHandlerPacket;
import com.github.icecheesecat.kantaicraft.playerkantaidata.PlayerKantaiData;
import com.github.icecheesecat.kantaicraft.registries.*;
import com.github.icecheesecat.kantaicraft.tickable.EquipmentActionHandler;
import com.google.common.collect.ImmutableList;
import io.netty.buffer.Unpooled;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
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
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;

public abstract class EntityShip extends PathfinderMob implements ISlotCheckerEntity, MenuProvider, GeoEntity, Stance {

    public static final EntityDataAccessor<Integer> DATA_AIRCRAFT = SynchedEntityData.defineId(EntityShip.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> DATA_AMMO = SynchedEntityData.defineId(EntityShip.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<ShipAnimationState> DATA_ANIMATION_STATE = SynchedEntityData.defineId(EntityShip.class, ModEntityDataSerializer.ANIMATION_STATE_SERIALIZER.get());
    public static final EntityDataAccessor<Integer> DATA_FOLLOW_DISTANCE = SynchedEntityData.defineId(EntityShip.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> DATA_FORCE_MELEE = SynchedEntityData.defineId(EntityShip.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> DATA_IS_GUARDING = SynchedEntityData.defineId(EntityShip.class, EntityDataSerializers.BOOLEAN);
//    public static final EntityDataAccessor<EmotionState> DATA_EMOTION_STATE = SynchedEntityData.defineId(EntityShip.class, ModEntityDataSerializer.EMOTION_STATE_SERIALIZER.get());
    public static final EntityDataAccessor<Optional<UUID>> DATA_SHIP_OWNER = SynchedEntityData.defineId(EntityShip.class, EntityDataSerializers.OPTIONAL_UUID);
    public static final EntityDataAccessor<Boolean> DATA_SHOULD_PICK_UP_ITEM = SynchedEntityData.defineId(EntityShip.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> DATA_SIT_DOWN = SynchedEntityData.defineId(EntityShip.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Float> DATA_SPEED_MODIFIER = SynchedEntityData.defineId(EntityShip.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Boolean> DATA_WONDER_AROUND = SynchedEntityData.defineId(EntityShip.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<ShipLeveling> DATA_SHIP_LEVELING = SynchedEntityData.defineId(EntityShip.class, ModEntityDataSerializer.SHIP_LEVEL_SERIALIZER.get());
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
    protected static final RawAnimation BURN_OUT_ANIMATION = RawAnimation.begin().thenLoop("burn_out");
    protected static final RawAnimation CANNON_FIRE_ANIMATION = RawAnimation.begin().thenPlayAndHold("cannon_fire");
    protected final List<EquipmentClass> equippableTypes;
    private final BlinkAnimationControl blinkAnimationControl = new BlinkAnimationControl(60, 80, this.random);
    private final ShipClass shipClass;
    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);
    private final LavaFuelCapability lavaFuelCapability;
    SimpleContainer inventory = this.createShipInventory();
    LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> new InvWrapper(inventory));
    protected EquipmentHandler equipmentHandler = new EquipmentHandler(4);
    LazyOptional<EquipmentHandler> lazyEquipmentHandler = LazyOptional.of(() -> equipmentHandler);
    private ShipAnimationState prevAnimationShipAnimationState;
    private long lastEmotionChangedTick = -1;
    private boolean noAnimation = false;
    private final int equipmentCount = 4;
    private final String name;
    private final LevelingModifier levelingModifier;
    private boolean startEatingAmmo = false;

    public EntityShip(EntityType<? extends PathfinderMob> entityType, ShipClass shipClass, Level level, List<EquipmentClass> equippableTypes, String name) {
        super(entityType, level);
        this.equippableTypes = ImmutableList.copyOf(equippableTypes);
        this.name = name;
        this.getCapability(EquipmentHandlerCapability.TOKEN).ifPresent(this::defaultEquipments);
        this.prevAnimationShipAnimationState = ShipAnimationState.IDLE;
        this.shipClass = shipClass;
        this.lavaFuelCapability = new LavaFuelCapability(this.defineFuelTankSize()) {
            @Override
            protected int getId() {
                return EntityShip.this.getId();
            }
        };

        this.levelingModifier = new LevelingModifier(this::getAttributes, this::getShipLeveling, this.defineGrowth());
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
        this.entityData.define(DATA_SHIP_LEVELING, ShipLeveling.levelZero());
        this.setupSyncedDataFromStance(entityData, this.random);
    }

    protected abstract AttributeGrowth defineGrowth();

    protected abstract void defaultEquipments(EquipmentHandler equipmentHandler);

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new ShipPathNavigation(this, pLevel);
    }

    public void useAmmo() {
        this.setAmmo(Math.max(this.getAmmo() - this.getAmmoCost(), 0.0f));
    }

    public boolean hasEnoughAmmo() {
        return this.getAmmo() > this.getAmmoCost();
    }

    public abstract float getAmmoCost();

    public int getAircraft() {
        return this.entityData.get(DATA_AIRCRAFT);
    }

    public void setAircraft(int value) {
        this.entityData.set(DATA_AIRCRAFT, value);
    }

    public int getFuel() {
        return this.lavaFuelCapability.getFluidTank().getFluidAmount();
    }

    public int addFuel(FluidStack fluidStack, IFluidHandler.FluidAction action) {
        return this.lavaFuelCapability.getFluidTank().fill(fluidStack, action);
    }

    public float getAmmo() {
        return this.entityData.get(DATA_AMMO);
    }

    public void addAmmo(float value) {
        this.setAmmo(this.getAmmo() + value);
    }

    public void setAmmo(float value) {
        this.entityData.set(DATA_AMMO, value);
    }

    public boolean hasAircraft() {
        return this.entityData.get(DATA_AIRCRAFT) > 0;
    }

    public boolean hasFuel() {
        return this.lavaFuelCapability.getFluidTank().getFluidAmount() > 0;
    }

    public boolean hasNoFuel() {
        return !this.hasFuel();
    }

    protected abstract int defineFuelTankSize();

    /**
     * @return fuel usage based on current activity
     */
    public int getFuelUsage() {
        var optional = this.getBrain().getActiveNonCoreActivity();
        if (optional.isEmpty()) {
            return this.defineFuelUsage();
        }

        Activity activity = optional.get();
        float scalar;
        switch (activity.getName()) {
            case "idle" -> scalar = 0.2f;
            case "sitting" -> scalar = 0.1f;
            case "fight" -> scalar = 1.5f;
            case "burn_out_fuels" -> scalar = 0.0f;
            default -> scalar = 1.0f;
        }

        return (int) (this.defineFuelUsage() * scalar);
    }

    /**
     * @return defines fuel usage amount per tick
     */
    protected abstract int defineFuelUsage();

    /**
     * @param amount fuel consume amount (lava)
     * @return whether successfully burned fuel or out of fuel
     */
    public boolean burnFuel(int amount) {
        FluidStack simAmount = this.lavaFuelCapability.getFluidTank().drain(amount, IFluidHandler.FluidAction.SIMULATE);
        if (simAmount.getAmount() == amount) {
            this.lavaFuelCapability.getFluidTank().drain(amount, IFluidHandler.FluidAction.EXECUTE);
            return true;
        }
        else {
            this.lavaFuelCapability.getFluidTank().drain(simAmount, IFluidHandler.FluidAction.EXECUTE);
            return false;
        }
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

    public void setFollowOwnerDistance(int num) {
        this.entityData.set(DATA_FOLLOW_DISTANCE, num);
    }

    public int getFollowTooCloseDistance() {
        return 3;
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
    public boolean shouldPickUpItem() {
        return this.entityData.get(DATA_SHOULD_PICK_UP_ITEM);
    }

    public void setShouldPickUpItem(boolean b) {
        this.entityData.set(DATA_SHOULD_PICK_UP_ITEM, b);
    }

    public void toggleSitDown() {
        this.entityData.set(DATA_SIT_DOWN, !this.isSitDown());
        if (this.isSitDown()) {
            this.getBrain().setMemory(ModMemoryModuleType.IS_SITTING.get(), Unit.INSTANCE);
        }
        else {
            this.getBrain().eraseMemory(ModMemoryModuleType.IS_SITTING.get());
        }

        this.navigation.stop();
        this.setTarget(null);
        this.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        this.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        this.getBrain().eraseMemory(MemoryModuleType.PATH);
        this.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("force_melee", this.entityData.get(DATA_FORCE_MELEE));
        nbt.putInt("data_aircraft", this.entityData.get(DATA_AIRCRAFT));
        nbt.putFloat("data_ammo", this.entityData.get(DATA_AMMO));
        nbt.putInt("animation_state", this.entityData.get(DATA_ANIMATION_STATE).ordinal());
        nbt.putInt("previous_animation_state", this.prevAnimationShipAnimationState.ordinal());
//        nbt.putInt("emotion_state", this.entityData.get(DATA_EMOTION_STATE).ordinal());
        nbt.putLong("last_emotion_changed_tick", this.lastEmotionChangedTick);
        nbt.put("ship_leveling", this.entityData.get(DATA_SHIP_LEVELING).serializeNBT());
        nbt.putFloat("speed_modifier", this.getNormalSpeedModifier());
        if (this.getShipInventory() != null)
            nbt.put("inventory", this.saveInventory());
        nbt.putBoolean("is_guarding", this.entityData.get(DATA_IS_GUARDING));
        this.entityData.get(DATA_SHIP_OWNER).ifPresent(uuid ->
                nbt.putUUID("shipowner", uuid));
        nbt.putInt("follow_distance", this.getFollowOwnerDistance());
        nbt.putBoolean("sit_down", this.isSitDown());
        nbt.put("equipmentHandler", this.equipmentHandler.serializeNBT());
        nbt.putString("entityType", this.getType().toString());
        nbt.putBoolean("wonder_around", this.canWonderAround());
        nbt.put("lava_fuel", this.lavaFuelCapability.serializeNBT());
        nbt.putBoolean("should_pick_up_item", this.shouldPickUpItem());
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

        if (nbt.contains("force_melee")) {
            this.entityData.set(DATA_FORCE_MELEE, nbt.getBoolean("force_melee"));
        }
        if (nbt.contains("data_aircraft")) {
            this.entityData.set(DATA_AIRCRAFT, nbt.getInt("data_aircraft"));
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
        if (nbt.contains("ship_leveling")) {
            ShipLeveling shipLeveling = ShipLeveling.create(nbt.getCompound("ship_leveling"));
            this.entityData.set(DATA_SHIP_LEVELING, shipLeveling);
        }
        if (nbt.contains("speed_modifier")) {
            this.entityData.set(DATA_SPEED_MODIFIER, nbt.getFloat("speed_modifier"));
        }
        if (nbt.contains("inventory")) {
            this.loadInventory(nbt.getCompound("inventory"));
        }
        if (nbt.contains("is_guarding")) {
            this.entityData.set(DATA_IS_GUARDING, nbt.getBoolean("is_guarding"));
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
        if (nbt.contains("lava_fuel")) {
            this.lavaFuelCapability.deserializeNBT(nbt.getCompound("lava_fuel"));
        }
        if (nbt.contains("should_pick_up_item")) {
            this.setShouldPickUpItem(nbt.getBoolean("should_pick_up_item"));
        }

    }

    protected void loadInventory(CompoundTag nbt) {
        ListTag listtag = nbt.getList("Items", 10);

        for(int i = 0; i < listtag.size(); ++i) {
            CompoundTag compoundtag = listtag.getCompound(i);
            byte j = compoundtag.getByte("Slot");
            this.inventory.setItem(j, ItemStack.of(compoundtag));
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

            tickEquipmentHandler();
            getAmmoFromInventory();
        }

    }

    private void updateShipLeveling() {
        if (this.getShipLeveling().isDirty()) {
            this.getShipLeveling().setDirty(false);
            this.entityData.set(DATA_SHIP_LEVELING, this.getShipLeveling());
        }
    }

    public void tickEquipmentHandler() {
        if (this.equipmentHandler != null && this.equipmentHandler.isDirty()) {
            ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), EquipmentHandlerPacket.wholeHandlerPacket(this.getId(), this.equipmentHandler));
            this.getBrain().setMemory(ModMemoryModuleType.ACTION_HANDLER.get(), new EquipmentActionHandler(this, equipmentHandler));
            this.equipmentHandler.setDirty(false);
        }
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

    public boolean forceMelee() {
        return this.entityData.get(DATA_FORCE_MELEE);
    }

    public void setForceMelee(boolean canMelee) {
        this.entityData.set(DATA_FORCE_MELEE, canMelee);
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

    public abstract boolean canRangeAttack();

    @Override
    public boolean canStandOnFluid(FluidState fluidState) {
        return fluidState.is(FluidTags.WATER);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent(this::setTarget);
        this.updateActivity();
    }

    @Override
    public Brain<EntityShip> getBrain() {
        return (Brain<EntityShip>) super.getBrain();
    }

    protected void updateActivity() {
        this.getBrain().setActiveActivityToFirstValid(ImmutableList.of(ModActivity.BURN_OUT_FUELS.get(), ModActivity.SITTING.get(), Activity.FIGHT, Activity.IDLE));
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

    public int getLevel() {
        return this.entityData.get(DATA_SHIP_LEVELING).getLevel();
    }

    public ShipLeveling getShipLeveling() {
        return this.entityData.get(DATA_SHIP_LEVELING);
    }

    public int getExp() {
        return this.entityData.get(DATA_SHIP_LEVELING).getExp();
    }

    public abstract int getProcessTime();

    // save blueprint to itemstack nbt
    public Blueprint makeBlueprint() {
        if (this instanceof HostileStance hostileStance) {
            return Blueprint.createWithLevelZero(this, hostileStance);
        }

        return Blueprint.create(this);
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

    protected SimpleContainer createShipInventory() {
        return this.isHostileSide() ? null : new SimpleContainer(36);
    }

    public boolean hasInventory() {
        return this.inventory != null;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        if (capability == EquipmentHandlerCapability.TOKEN) {
            return lazyEquipmentHandler.cast();
        }
        if (capability == ForgeCapabilities.FLUID_HANDLER) {
            return lavaFuelCapability.getCapability(capability, facing);
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEquipmentHandler.invalidate();
        lavaFuelCapability.getLazyFluidHandler().invalidate();
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
        Collection<ItemEntity> drops = new ArrayList<>();

        // drop a blueprint of this ship
        ItemStack itemStack = new ItemStack(ModItem.SHIP_BLUEPRINT.get());
        itemStack.setTag(this.makeBlueprint().serializeNBT());
        ItemEntity blueprintEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), itemStack);
        if (level().addFreshEntity(blueprintEntity)) {
            drops.add(blueprintEntity);
        }

        // drop all item in ship's inventory
        if (hasInventory()) {
            for (int i = 0; i < this.inventory.getMaxStackSize(); i++) {
                ItemStack stack = this.inventory.getItem(i);
                if (stack.isEmpty()) continue;
                ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), stack);
                if (level().addFreshEntity(itemEntity)) {
                    drops.add(blueprintEntity);
                }
            }
        }

        ForgeHooks.onLivingDrops(this, pSource, drops, 0, true);

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

    public void setShipOwner(UUID uuid) {
        if (this.isHostileSide()) return;
        this.entityData.set(DATA_SHIP_OWNER, Optional.of(uuid));
    }

    @Nullable
    public LivingEntity getOwnerEntity() {
        if (this.getShipOwner().isEmpty()) return null;
        if (this.level().isClientSide) {
            return null; // TODO request from client to get Entity id
        }
        else if (this.level() instanceof ServerLevel serverLevel) {
            var entity = serverLevel.getEntity(this.getShipOwner().get());
            return entity instanceof LivingEntity ? (LivingEntity) entity : null;
        }
        return null;
    }

    public Optional<Player> getPlayerOwner() {
        if (this.getOwnerEntity() instanceof Player player) {
            return Optional.of(player);
        }
        return Optional.empty();
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
        if (pSource.is(DamageTypes.PLAYER_ATTACK)) {
            if (this.isShipOwner((Player) pSource.getEntity())) {
                return super.hurt(pSource, pAmount);
            }
            else {
                return super.hurt(pSource, 0.0f);
            }
        }

        return super.hurt(pSource, pAmount);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource pSource) {
        return pSource.is(DamageTypes.FALL) || super.isInvulnerableTo(pSource);
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
            return InteractionResult.SUCCESS;
        }
        if (this.isHostileSide()) {
            return InteractionResult.SUCCESS;
        }

        if (!this.isShipOwner(pPlayer)) {
            return InteractionResult.FAIL;
        }

        var resultSit = playerInteractToSitDown(pPlayer, pHand);
        if (resultSit != InteractionResult.FAIL) {
            return resultSit;
        }

        var resultLavaOrTankInHand = playerInteractToRefill(pPlayer, pHand);
        if (resultLavaOrTankInHand != InteractionResult.FAIL) {
            return resultLavaOrTankInHand;
        }

        var resultMenu = playerInteractToOpenMenu(pPlayer, pHand);
        if (resultMenu != InteractionResult.FAIL) {
            return resultMenu;
        }

        return InteractionResult.PASS;
    }

    protected InteractionResult playerInteractToSitDown(Player pPlayer, InteractionHand pHand) {
        if (pHand == InteractionHand.MAIN_HAND && pPlayer.isShiftKeyDown()) {
            if (this.hasFuel()) {
                this.toggleSitDown();
                return InteractionResult.SUCCESS;
            }
            else {
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.FAIL;
    }

    protected InteractionResult playerInteractToOpenMenu(Player pPlayer, InteractionHand pHand) {
        if (pHand == InteractionHand.MAIN_HAND && pPlayer instanceof ServerPlayer serverPlayer) {
            ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), EquipmentHandlerPacket.wholeHandlerPacket(this.getId(), this.equipmentHandler));
            NetworkHooks.openScreen(serverPlayer, this, (friendlyByteBuf -> {
                friendlyByteBuf.writeInt(this.getId());
            }));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    protected InteractionResult playerInteractToRefill(Player pPlayer, InteractionHand pHand) {
        if (pHand == InteractionHand.MAIN_HAND && pPlayer instanceof ServerPlayer serverPlayer) {
            if (pPlayer.getItemInHand(InteractionHand.MAIN_HAND).is(Items.LAVA_BUCKET)) {
                // try to add one bucket of lava to fuel tank
                this.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(
                        tank -> {
                            int fluidAmountForOneBucket = 1000;
                            int canFillAmount = tank.fill(new FluidStack(Fluids.LAVA, fluidAmountForOneBucket), IFluidHandler.FluidAction.SIMULATE);
                            if (canFillAmount == fluidAmountForOneBucket) {
                                tank.fill(new FluidStack(Fluids.LAVA, fluidAmountForOneBucket), IFluidHandler.FluidAction.EXECUTE);
                                if (!((ServerPlayer) pPlayer).gameMode.isCreative()) {
                                    pPlayer.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET));
                                }
                            }
                        }
                );

                return InteractionResult.SUCCESS;
            }
            else if (pPlayer.getItemInHand(InteractionHand.MAIN_HAND).getCapability(ForgeCapabilities.FLUID_HANDLER).isPresent()) {
                pPlayer.getItemInHand(InteractionHand.MAIN_HAND).getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(
                        tankInHand -> {
                            for (int i = 0; i < tankInHand.getTanks(); i++) {
                                var fluidInTank = tankInHand.getFluidInTank(i);
                                if (fluidInTank.getFluid() == Fluids.LAVA) {

                                    int couldFillAmount = this.lavaFuelCapability.getFluidTank().fill(new FluidStack(Fluids.LAVA, 1000), IFluidHandler.FluidAction.SIMULATE);
                                    var drainedFluid = tankInHand.drain(couldFillAmount, IFluidHandler.FluidAction.EXECUTE);
                                    this.lavaFuelCapability.getFluidTank().fill(drainedFluid, IFluidHandler.FluidAction.EXECUTE);

                                }
                            }
                        }
                );

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.FAIL;
    }


    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isPersistenceRequired() {
        return this.getType().getCategory().isPersistent();
    }

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
        controllers.add(new AnimationController<>(this, "burn_out", 0, this::burnOutAnimationController).triggerableAnim("burn_out", BURN_OUT_ANIMATION).receiveTriggeredAnimations());
        controllers.add(new AnimationController<>(this, "cannon_fire", 0, this::cannonFireAnimation).triggerableAnim("cannon_fire", CANNON_FIRE_ANIMATION).receiveTriggeredAnimations());
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
        if (this.getBrain().getActiveNonCoreActivity().isPresent() && this.getBrain().getActiveNonCoreActivity().get() == Activity.IDLE && hasFuel()) { // BUG : walking animation cannot play with breath animation (because they use same part of the body?)
            return event.setAndContinue(BREATH_ANIMATION);
        }

        return PlayState.STOP;
    }

    protected <E extends EntityShip> PlayState sitAnimationController(final AnimationState<E> event) {
        if (this.isSitDown() && this.hasFuel()) {
            return event.setAndContinue(DUCK_POSE_ANIMATION);
        }
        event.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    protected <E extends EntityShip> PlayState burnOutAnimationController(final AnimationState<E> event) {
        if (noAnimation) return PlayState.STOP;
        if (event.getController().isPlayingTriggeredAnimation()) {
            return event.setAndContinue(BURN_OUT_ANIMATION);
        }
        else if (hasNoFuel()) {
            return event.setAndContinue(BURN_OUT_ANIMATION);
        }

        return PlayState.STOP;
    }

    protected <E extends EntityShip> PlayState cannonFireAnimation(final AnimationState<E> state) {
        if (state.getController().isPlayingTriggeredAnimation()) {
            return state.setAndContinue(state.getController().getTriggeredAnimation());
        }

        return PlayState.STOP;
    }

    public void setNoAnimation() {
        this.noAnimation = true;
    }

    private boolean isWalkingOrRunning() {
        return this.walkAnimation.isMoving() && this.walkAnimation.speed() > 0.05f;
    }

    @Override
    protected void sendDebugPackets() {
        if (this.level().isClientSide) return;

        FriendlyByteBuf friendlybytebuf = new FriendlyByteBuf(Unpooled.buffer());
        friendlybytebuf.writeDouble(this.position().x);
        friendlybytebuf.writeDouble(this.position().y);
        friendlybytebuf.writeDouble(this.position().z);
        friendlybytebuf.writeUUID(this.uuid);
        friendlybytebuf.writeInt(this.getId());
        friendlybytebuf.writeUtf(this.getName().getString());
        friendlybytebuf.writeUtf("no profession");
        friendlybytebuf.writeInt(0);
        friendlybytebuf.writeFloat(this.getHealth());
        friendlybytebuf.writeFloat(this.getMaxHealth());
        if (this.inventory == null) {
            friendlybytebuf.writeUtf("");
        }
        else {
            friendlybytebuf.writeUtf(this.inventory.toString());
        }
        friendlybytebuf.writeBoolean(false); // path
        friendlybytebuf.writeBoolean(false);
        friendlybytebuf.writeInt(0);

        var activities = this.getBrain().getActiveActivities();
        friendlybytebuf.writeVarInt(activities.size());
        for (var act: activities) {
            friendlybytebuf.writeUtf(act.getName());
        }

        var behaviors = this.getBrain().getRunningBehaviors();
        friendlybytebuf.writeVarInt(behaviors.size());
        for (var be: behaviors) {
            friendlybytebuf.writeUtf(be.debugString());
        }

        var memories = this.getBrain().getMemories().entrySet();
        var filtered_memories = memories.stream().filter((entry) -> entry.getValue().isPresent()).toList();
        friendlybytebuf.writeVarInt(filtered_memories.size());
        for (var mem: filtered_memories) {
            friendlybytebuf.writeUtf(mem.getKey().toString() + " -> " + mem.getValue().get().toString());
        }

        friendlybytebuf.writeInt(0); // pois
        friendlybytebuf.writeInt(0); // potential pois
        friendlybytebuf.writeInt(0); // gossip

        Packet<?> packet = new ClientboundCustomPayloadPacket(ClientboundCustomPayloadPacket.DEBUG_BRAIN, friendlybytebuf);

        for(Player player : this.level().players()) {
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(packet);
            }
        }
    }

    public LavaFuelCapability getLavaFuelCapability() {
        return lavaFuelCapability;
    }

    public boolean lavaFuelIsFull() {
        return this.lavaFuelCapability.getFluidTank().getFluidAmount() == this.lavaFuelCapability.getFluidTank().getCapacity();
    }

    public static void handleRetrieveFromLevel(ServerLevel serverLevel, UUID uuid, PlayerKantaiData playerKantaiData) {
        if (serverLevel.getEntity(uuid) instanceof EntityShip entityShip) {
            playerKantaiData.addShipInDock(entityShip);
            entityShip.remove(RemovalReason.DISCARDED);
        }
    }

    public void setLevel(int level) {
        this.levelingModifier.setLevel(level);
    }

    public void addExp(int exp) {
        this.levelingModifier.addExp(exp);
    }

    private void getAmmoFromInventory() {
        double maxAmmo = this.getAttributeValue(ModAttribute.MAX_AMMO.get());
        if (this.getAmmo() / maxAmmo < 0.3f) {
            this.startEatingAmmo = true;
        }
        if (this.startEatingAmmo) {
            if (this.inventory.countItem(ModItem.AMMO.get()) == 0) {
                this.startEatingAmmo = false;
                return;
            }
            for (int i = 0; i < this.inventory.getContainerSize(); i++) {
                if (this.inventory.getItem(i).is(ModItem.AMMO.get())) {
                    this.addAmmo(1);
                    this.inventory.removeItem(i, 1);
                }
            }
        }

    }

}
