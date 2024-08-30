package com.github.icecheesecat.shincolle.util.tickable.attack;

import com.github.icecheesecat.shincolle.entity.BasicEntityShip;
import com.github.icecheesecat.shincolle.network.ModPacketHandler;
import com.github.icecheesecat.shincolle.network.packet.TrajectoryPacket;
import com.github.icecheesecat.shincolle.util.Trajectory;
import com.github.icecheesecat.shincolle.equipment.cannon.Cannon;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class ShipCannonAttack extends ShipRangeAttack {

    Cannon cannon;
    List<Trajectory> trajectories = new ArrayList<>();

    public ShipCannonAttack(BasicEntityShip ship, Cannon cannon) {
        super(ship);
        this.cannon = cannon;
    }

    public void checkAndPerformCanon(LivingEntity target) {
        if (this.checkCooldown()) {
            if (target != null && target.isAlive()) {
                LivingEntity ship = getShip();
                Vec3 initPos = new Vec3(ship.getX(), ship.getEyeY(), ship.getZ());

                float canon_vel = cannon.getCannon_vel();
                Vec3 fire_dir = null;
                Vec3 gravity = new Vec3(0, -9.8f, 0);
                Vec3 end_spot = target.position();
                double angle = Trajectory.calculateAngle(ship.position(), end_spot, gravity, canon_vel);
                if ( !Double.isNaN(angle) ) {
                    int id = ship.getRandom().nextInt();
                    fire_dir = Trajectory.calculateFireDir(ship.position(), end_spot, angle, canon_vel);
                    Trajectory t = new Trajectory(initPos, fire_dir, gravity, id);
                    trajectories.add(t);

                    // send to client trajectory
                    ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new TrajectoryPacket(t));
                }




                this.resetCooldown();
            }
        }
    }

    public void doHitTarget(Entity entity) {
        BasicEntityShip ship = getShip();
        entity.hurt(ship.damageSources().mobProjectile(entity, ship), ship.getShipAttrs().getFirePower());
    }


    protected void tickTrajectories() {
        List<Trajectory> invalids = new ArrayList<>();

//        System.out.println(trajectories.size());
        for (var t: trajectories) {
            t.tick(0);

            HitResult hitResult = t.findHitResult(getShip().level());
            if (hitResult.getType() != HitResult.Type.MISS) {
                if (hitResult instanceof EntityHitResult ehr) {
                    Entity entity = ehr.getEntity();
                    doHitTarget(entity);
                }
                invalids.add(t);
            }

            if (t.checkTimeout()) {
                invalids.add(t);
            }
//            System.out.print("Server: ");
//            t.print();
        }

        trajectories.removeAll(invalids);
    }


    @Override
    public void tick() {
        this.tickCooldown();
        this.tickTrajectories();
    }
}

