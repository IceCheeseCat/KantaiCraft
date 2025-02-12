package com.github.icecheesecat.kantaicraft.faction;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegisterCapability
public class LevelFactionCapability implements ICapabilitySerializable<CompoundTag> {

    public static Capability<Faction> FACTION = CapabilityManager.get(new CapabilityToken<>() {});
    LazyOptional<Faction> lazyFaction;
    Faction faction;

    private Faction getContent() {
        if (this.faction == null) {
            return new Faction();
        }

        return this.faction;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == FACTION) {
            return lazyFaction.cast();
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
