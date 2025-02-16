package com.github.icecheesecat.kantaicraft.capability;

import com.github.icecheesecat.kantaicraft.block.shipyardUtil.ShipBlueprintStackHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegisterCapability
public class ShipBlueprintStackHandlerCapability implements ICapabilitySerializable<CompoundTag> {

    LazyOptional<ShipBlueprintStackHandler> lazyOptional = LazyOptional.of(this::getContent);
    ShipBlueprintStackHandler stackHandler;

    int size;

    public ShipBlueprintStackHandlerCapability(int size) {
        this.size = size;
    }

    private ShipBlueprintStackHandler getContent() {
        if (this.stackHandler == null) {
            this.stackHandler = new ShipBlueprintStackHandler(4);
        }

        return this.stackHandler;
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.lazyOptional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.getContent().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.getContent().deserializeNBT(nbt);
    }
}
