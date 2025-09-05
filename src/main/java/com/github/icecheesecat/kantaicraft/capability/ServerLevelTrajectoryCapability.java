package com.github.icecheesecat.kantaicraft.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegisterCapability
public class ServerLevelTrajectoryCapability implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<ServerLevelTrajectory> TOKEN = CapabilityManager.get(new CapabilityToken<>(){});
    private ServerLevelTrajectory serverLevelTrajectory = null;
    private final LazyOptional<ServerLevelTrajectory> levelTrajectoryLazyOptional = LazyOptional.of(this::getLevelTrajectory);
    Level level;

    public ServerLevelTrajectoryCapability(Level level) {
        this.level = level;
    }

    private ServerLevelTrajectory getLevelTrajectory() {
        if (this.serverLevelTrajectory == null) {
            this.serverLevelTrajectory = new ServerLevelTrajectory(level);
        }
        return this.serverLevelTrajectory;
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == TOKEN) {
            return this.levelTrajectoryLazyOptional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.serverLevelTrajectory.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.serverLevelTrajectory.deserializeNBT(nbt);
    }
}
