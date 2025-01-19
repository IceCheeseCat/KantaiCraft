package com.github.icecheesecat.kantaicraft.equipment;

import com.github.icecheesecat.kantaicraft.entity.ship.ISlotCheckerEntity;
import com.github.icecheesecat.kantaicraft.util.ShipFields;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AutoRegisterCapability
public class EquipmentProvider implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<EquipmentHandler> EQUIPMENT_HANDLER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    private EquipmentHandler equipmentHandler = null;
    private final LazyOptional<EquipmentHandler> lazyEquipmentHandler = LazyOptional.of(this::getEquipmentHandler);
    private final int slotSize;

    public EquipmentProvider(int slotSize) {
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
        if (cap == EQUIPMENT_HANDLER_CAPABILITY) {
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
