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
    private final Equipment defaultEquipment;

    public EquipmentProvider(int slotSize, Equipment defaultEquipment) {
        this.slotSize = slotSize;
        this.defaultEquipment = defaultEquipment;
    }

    private EquipmentHandler getEquipmentHandler() {
        if (this.equipmentHandler == null) {
            this.equipmentHandler = new EquipmentHandler(this.slotSize, this.defaultEquipment);
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
