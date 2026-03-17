package com.github.icecheesecat.kantaicraft.block.facilities.fuelstation;

import com.github.icecheesecat.kantaicraft.block.facilities.FacilityCoreBlockEntity;
import com.github.icecheesecat.kantaicraft.block.facilities.pattern.FindPatternResult;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.fuelstation.FuelStationPacket;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FuelStationBlockEntity extends FacilityCoreBlockEntity implements GeoBlockEntity {

    private final FluidTank lavaTank = new FluidTank(FluidType.BUCKET_VOLUME * 100);
    private final LazyOptional<FluidTank> lazyFluidTank = LazyOptional.of(() -> lavaTank);
    private State state = State.IDLE;
    private int fullCountdown = 0;
    private static final int FULL_COUNTDOWN = 600;
    private EntityShip entityShip;
    private AABB findArea;

    public FuelStationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlock.FUEL_STATION_BETYPE.get(), pPos, pBlockState);
    }

    @Override
    public void setup(FindPatternResult result) {
        super.setup(result);
        createFindArea();
    }

    public void createFindArea() {
        if (this.start != null && this.end != null) {
            this.findArea = new AABB(this.start, this.end);
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidTank.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyFluidTank.invalidate();
    }

    @Override
    public String toString() {
        return this.getClass().getName() + this.getBlockPos() + this.lavaTank.getFluid().getFluid().getFluidType().getDescription() + " : " + this.lavaTank.getFluid().getAmount();
    }

    public Component getWorkingStatus() {
        String statusString = this.state.name() + " lava :" + this.lavaTank.getFluidAmount();

        if (entityShip != null) {
            statusString += this.entityShip.getName().getString();
        }

        return Component.translatable(statusString);
    }

    @Override
    public @NotNull CompoundTag saveExtraData() {
        CompoundTag nbt = new CompoundTag();
        nbt = lavaTank.writeToNBT(nbt);
//        nbt.putInt("state", this.state.ordinal());
        return nbt;
    }

    @Override
    public void loadExtraData(@NotNull CompoundTag nbt) {
        this.lavaTank.readFromNBT(nbt);
//        this.state = State.values()[nbt.getInt("state")];
        createFindArea();
    }

    protected void findNearbyEntity() {
        if (entityShip == null) {
            //find
            var list = level.getEntitiesOfClass(EntityShip.class, findArea, EntityShip::isPlayerShip);
            this.entityShip = list.isEmpty() ? null : list.get(0);
            this.fullCountdown = 0;
        }
        else {
            // out of range
            if (!this.entityShip.getBoundingBox().intersects(findArea)) {
                this.entityShip = null;
            }
        }
    }

    protected void tickFacility() {

        switch (this.state) {
            case WORKING -> {
                executeFillLavaToShip();
            }
            case FILLED_SHIP -> {
                executeFillLavaToShip();
                this.fullCountdown--;
            }
        }

    }

    private void executeFillLavaToShip() {
        int drainAmount = 10;
        FluidStack drainedFluid = this.lavaTank.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
        this.entityShip.getLavaFuelCapability().getFluidTank().fill(drainedFluid, IFluidHandler.FluidAction.EXECUTE);
        this.setChanged();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void evaluateState() {
        // change state to STATION_TANK_EMPTY when below 100, and change to others when above 1000
        State orign = this.state;
        switch (this.state) {
            case IDLE -> {
                if (this.entityShip != null) {
                    this.state = State.WORKING;
                }
            }
            case WORKING -> {
                if (this.entityShip == null) {
                    this.state = State.IDLE;
                }
                else if (this.lavaTank.isEmpty()) {
                    this.state = State.STATION_TANK_EMPTY;
                } else if (this.entityShip.lavaFuelIsFull()) {
                    this.state = State.FILLED_SHIP;
                }
            }
            case STATION_TANK_EMPTY -> {
                if (this.entityShip == null) {
                    this.state = State.IDLE;
                }
                if (this.lavaTank.getFluidAmount() >= 1000) {
                    this.state = State.IDLE;
                    this.evaluateState();
                }
            }
            case FILLED_SHIP -> {
                if (this.entityShip == null) {
                    this.state = State.IDLE;
                }
                else if (this.lavaTank.isEmpty()) {
                    this.state = State.STATION_TANK_EMPTY;
                }
                else if (this.fullCountdown <= 0) {
                    this.state = State.IDLE;
                }
                else if (this.entityShip.lavaFuelIsFull()) {
                    this.fullCountdown = FULL_COUNTDOWN;
                }
            }
        }

        if (orign != this.state) {
            ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new FuelStationPacket(this));
        }
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, FuelStationBlockEntity blockEntity) {

        if (!blockEntity.isTickable()) return;
        if (level.isClientSide) return;
        blockEntity.findNearbyEntity();
        blockEntity.evaluateState();
        blockEntity.tickFacility();

    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenPlay("idle");
    private static final RawAnimation REFILL_ANIMATION = RawAnimation.begin().thenLoop("refill");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", this::idleAnimation));
        controllers.add(new AnimationController<>(this, "working", this::workingAnimation));
    }

    private PlayState workingAnimation(AnimationState<FuelStationBlockEntity> fuelStationBlockEntityAnimationState) {
        if (this.state == State.WORKING) {
            return fuelStationBlockEntityAnimationState.setAndContinue(REFILL_ANIMATION);
        }

        return PlayState.STOP;
    }

    private PlayState idleAnimation(AnimationState<FuelStationBlockEntity> fuelStationBlockEntityAnimationState) {
        if (this.state == State.IDLE) {
            return fuelStationBlockEntityAnimationState.setAndContinue(IDLE_ANIMATION);
        }

        return PlayState.STOP;
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public enum State {

        IDLE,
        WORKING,
        FILLED_SHIP,
        STATION_TANK_EMPTY,
        PAUSED;

    }
}
