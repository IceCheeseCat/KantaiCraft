package com.github.icecheesecat.kantaicraft.capability.trajectory;

import com.github.icecheesecat.kantaicraft.util.Trajectory;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientLevelTrajectory extends ServerLevelTrajectory {

    public final Map<Integer,List<Vec3>> trajectories_arc_poses;

    public ClientLevelTrajectory(Level clientLevel) {
        super(clientLevel);
        this.trajectories_arc_poses = new HashMap<>();
    }

    @Override
    protected void removeNonAliveTrajectory() {
        var removing = this.getNonAliveTrajectories();
        removing.forEach(trajectory -> {
            trajectories_arc_poses.remove(trajectory.getId());
        });
        super.removeNonAliveTrajectory();
    }

    @Override
    public void appendTrajectory(Trajectory trajectory) {
        this.trajectories.add(trajectory);
        this.trajectories_arc_poses.put(trajectory.getId(), new ArrayList<>());
    }

    @Override
    public void tick() {
        this.trajectories.forEach(trajectory -> {
            this.trajectories_arc_poses.get(trajectory.getId()).add(trajectory.getPhysics().getPos());
        });
        this.trajectories.forEach(Trajectory::tick);
        removeNonAliveTrajectory();
    }

    public List<Vec3> getPointsOfArc(int i) {
        if (this.trajectories_arc_poses.containsKey(i)) {
            return this.trajectories_arc_poses.get(i);
        }
        return null;
    }

    public void setPointsOfArc(int i, List<Vec3> nPointsOfArc) {
        if (this.trajectories_arc_poses.containsKey(i)) {
            this.trajectories_arc_poses.put(i, nPointsOfArc);
        }
    }
}
