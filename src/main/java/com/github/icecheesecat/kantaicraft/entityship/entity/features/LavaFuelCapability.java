package com.github.icecheesecat.kantaicraft.entityship.entity.features;

import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.entityship.LavaFuelPacket;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class LavaFuelCapability implements ICapabilitySerializable<CompoundTag> {

    private final FluidTank FLUID_TANK;
    private final LazyOptional<IFluidHandler> lazyFluidHandler;

    public LavaFuelCapability(int capacity) {
        FLUID_TANK = new FluidTank(capacity, fluidStack -> fluidStack.getFluid() == Fluids.LAVA) {
            @Override
            protected void onContentsChanged() {
                ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new LavaFuelPacket(getId(), serializeNBT()));
            }
        };
        lazyFluidHandler = LazyOptional.of(() -> FLUID_TANK);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return this.lazyFluidHandler.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.FLUID_TANK.writeToNBT(new CompoundTag());
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.FLUID_TANK.readFromNBT(nbt);
    }

    public LazyOptional<IFluidHandler> getLazyFluidHandler() {
        return lazyFluidHandler;
    }

    public FluidTank getFluidTank() {
        return FLUID_TANK;
    }

    abstract int getId();
}
