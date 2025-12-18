package com.github.icecheesecat.kantaicraft.capability;

import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.trajectory.TrajectoryPacket;
import com.github.icecheesecat.kantaicraft.util.Trajectory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class ServerLevelTrajectory implements INBTSerializable<CompoundTag> {
    final List<Trajectory> trajectories;
    final Level level;

    public ServerLevelTrajectory(Level level) {
        this.trajectories = new ArrayList<>();
        this.level = level;
    }

    public void tick() {
        this.trajectories.forEach(trajectory -> {
            trajectory.tick(this.level);
        });
        removeNonAliveTrajectory();
    }

    protected void removeNonAliveTrajectory() {
        this.trajectories.removeAll(this.getNonAliveTrajectories());
    }

    protected List<Trajectory> getNonAliveTrajectories() {
        return this.trajectories.stream().filter(Trajectory::checkTimeout).toList();
    }

    public void appendTrajectory(Trajectory trajectory) {
        this.trajectories.add(trajectory);
        ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new TrajectoryPacket(trajectory)); // Synced to client
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("size", this.trajectories.size());
        for (int i = 0; i < trajectories.size(); i++) {
            nbt.put("trajectory." + i, this.trajectories.get(i).serializeNBT());
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        int size = nbt.getInt("size");
        this.trajectories.clear();
        for (int i = 0; i < size; i++) {
            this.trajectories.add(i, Trajectory.create(nbt.getCompound("trajectory."+i)));
        }
    }

    public List<Trajectory> getTrajectories() {
        return trajectories;
    }
}
