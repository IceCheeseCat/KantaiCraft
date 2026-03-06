package com.github.icecheesecat.kantaicraft.capability.trajectory;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegisterCapability
public class ClientLevelTrajectoryCapability implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<ClientLevelTrajectory> TOKEN = CapabilityManager.get(new CapabilityToken<>(){});
    ClientLevel level;
    private ClientLevelTrajectory clientLevelTrajectory = null;
    private final LazyOptional<ClientLevelTrajectory> clientLevelTrajectoryLazyOptional = LazyOptional.of(this::getClientLevelTrajectory);

    public ClientLevelTrajectoryCapability(ClientLevel level) {
        this.level = level;
    }

    private ClientLevelTrajectory getClientLevelTrajectory() {
        if (this.clientLevelTrajectory == null) {
            this.clientLevelTrajectory = new ClientLevelTrajectory(level);
        }
        return this.clientLevelTrajectory;
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == TOKEN) {
            return this.clientLevelTrajectoryLazyOptional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.clientLevelTrajectory.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.clientLevelTrajectory.deserializeNBT(nbt);
    }

}
