package com.github.icecheesecat.kantaicraft.capability;

import com.github.icecheesecat.kantaicraft.equipment.handler.EquipmentHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegisterCapability
public class EquipmentHandlerCapability implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<EquipmentHandler> TOKEN = CapabilityManager.get(new CapabilityToken<>(){});
    private EquipmentHandler equipmentHandler = null;
    private final LazyOptional<EquipmentHandler> lazyEquipmentHandler = LazyOptional.of(this::getEquipmentHandler);
    private final int slotSize;

    public EquipmentHandlerCapability(int slotSize) {
        this.slotSize = slotSize;
    }

    private EquipmentHandler getEquipmentHandler() {
        if (this.equipmentHandler == null) {
            this.equipmentHandler = new EquipmentHandler(this.slotSize);
        }

        return this.equipmentHandler;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == EquipmentHandlerCapability.TOKEN) {
            return lazyEquipmentHandler.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.getEquipmentHandler().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.getEquipmentHandler().deserializeNBT(nbt);
    }


}
