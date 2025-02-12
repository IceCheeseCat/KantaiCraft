package com.github.icecheesecat.kantaicraft.faction;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegisterCapability
public class FactionTagCapability implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<FactionTag> FACTION_TAG = CapabilityManager.get(new CapabilityToken<>() {});
    LazyOptional<FactionTag> lazyFactionTag = LazyOptional.of(this::getContent);
    FactionTag factionTag;

    private FactionTag getContent() {
        if (this.factionTag == null) {
            this.factionTag = new FactionTag(-1, "", FactionType.NEUTRAL);
        }

        return this.factionTag;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == FACTION_TAG) {
            return lazyFactionTag.cast();
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
