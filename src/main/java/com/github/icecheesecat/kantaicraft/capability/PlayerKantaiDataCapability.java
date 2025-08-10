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
    private PlayerKantaiData playerKantaiData = null;
    private final LazyOptional<PlayerKantaiData> lazyPlayerKantaiData = LazyOptional.of(this::getPlayerKantaiData);
    private final Player player;

    public PlayerKantaiDataCapability(@NotNull Player player) {
        this.player = player;
    }

    private PlayerKantaiData getPlayerKantaiData() {
        if (this.playerKantaiData == null) {
            this.playerKantaiData = new PlayerKantaiData(this.player);
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
        return this.playerKantaiData.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.playerKantaiData.deserializeNBT(nbt);
    }
}
