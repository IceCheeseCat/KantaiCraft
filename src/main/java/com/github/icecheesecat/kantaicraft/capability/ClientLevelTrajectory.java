package com.github.icecheesecat.kantaicraft.capability;

import com.github.icecheesecat.kantaicraft.util.Trajectory;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientLevelTrajectory extends ServerLevelTrajectory {

    final Map<Integer,List<Vec3>> trajectories_arc_poses;
    final Map<Integer, Integer> lastIndexOfTrajectory;
    final Map<Integer, Vec3> lastHitLocationOfTrajectory;

    public ClientLevelTrajectory(ClientLevel clientLevel) {
        super(clientLevel);
        this.trajectories_arc_poses = new HashMap<>();
        this.lastIndexOfTrajectory = new HashMap<>();
        this.lastHitLocationOfTrajectory = new HashMap<>();
    }

    @Override
    protected void removeNonAliveTrajectory() {
        var removing = this.getNonAliveTrajectories();
        removing.forEach(trajectory -> {
            trajectories_arc_poses.remove(trajectory.getId());
            lastIndexOfTrajectory.remove(trajectory.getId());
            lastHitLocationOfTrajectory.remove(trajectory.getId());
        });
        super.removeNonAliveTrajectory();
    }

    @Override
    public void appendTrajectory(Trajectory trajectory) {
        this.trajectories.add(trajectory);
        this.trajectories_arc_poses.put(trajectory.getId(), new ArrayList<>());
    }

    public void appendLastHitTrajectory(int id, int index, Vec3 pos) {
        this.lastIndexOfTrajectory.put(id, index);
        this.lastHitLocationOfTrajectory.put(id, pos);
    }

    @Override
    public void tick() {
        this.trajectories.forEach(trajectory -> {
            int id = trajectory.getId();
            if (this.lastIndexOfTrajectory.containsKey(trajectory.getId())) { // Trajectory that has hit something
                if (this.lastIndexOfTrajectory.get(id) == this.trajectories_arc_poses.get(id).size() + 1) return; // completed arc
                if (this.lastIndexOfTrajectory.get(id) > this.trajectories_arc_poses.get(id).size()) { // continue unfinished trajectory arc
                    this.trajectories_arc_poses.get(id).add(trajectory.getPhysics().getPos());
                }
                else if (this.lastIndexOfTrajectory.get(id) == this.trajectories_arc_poses.get(id).size()) { // append new trajectory last hit location
                    this.trajectories_arc_poses.get(id).add(this.lastHitLocationOfTrajectory.get(id));
                }
                else { // remove over calculated points
                    var arc = this.trajectories_arc_poses.get(id);
                    this.trajectories_arc_poses.put(id, arc.subList(0, this.lastIndexOfTrajectory.get(id)));
                }
            }
            else { // Trajectory hasn't hit anything yet
                this.trajectories_arc_poses.get(trajectory.getId()).add(trajectory.getPhysics().getPos());
            }
        });
        super.tick();
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
