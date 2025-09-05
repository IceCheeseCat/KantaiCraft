package com.github.icecheesecat.kantaicraft.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegisterCapability
public class PlayerKantaiDataCapability implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<PlayerKantaiData> TOKEN = CapabilityManager.get(new CapabilityToken<>(){});
    private PlayerKantaiData playerKantaiData;
    LazyOptional<PlayerKantaiData> lazyPlayerKantaiData = LazyOptional.of(this::getPlayerKantaiData);

    public PlayerKantaiDataCapability() {
    }

    private PlayerKantaiData getPlayerKantaiData() {
        if (this.playerKantaiData == null) {
            this.playerKantaiData = new PlayerKantaiData();
        }

        return this.playerKantaiData;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PlayerKantaiDataCapability.TOKEN) {
            return lazyPlayerKantaiData.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.getPlayerKantaiData().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.getPlayerKantaiData().deserializeNBT(nbt);
    }
}
