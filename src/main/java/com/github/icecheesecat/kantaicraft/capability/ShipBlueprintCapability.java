package com.github.icecheesecat.kantaicraft.capability;

import com.github.icecheesecat.kantaicraft.item.ShipBlueprintData;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegisterCapability
public class ShipBlueprintCapability implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<ShipBlueprintData> TOKEN = CapabilityManager.get(new CapabilityToken<>() {});
    LazyOptional<ShipBlueprintData> shipBlueprintDataLazyOptional = LazyOptional.of(this::getContent);
    ShipBlueprintData data;

    private ShipBlueprintData getContent() {
        if (data == null) {
            this.data = ShipBlueprintData.empty();
        }

        return this.data;
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == TOKEN) {
            return shipBlueprintDataLazyOptional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.getContent().write();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.data = ShipBlueprintData.read(nbt);
    }
}
