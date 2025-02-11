package com.github.icecheesecat.kantaicraft.util.tickable.attack;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentStatType;
import com.github.icecheesecat.kantaicraft.network.packet.ClientRemoveTrajectoryPacket;
import com.github.icecheesecat.kantaicraft.registries.ModAttribute;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.TrajectoryPacket;
import com.github.icecheesecat.kantaicraft.util.Trajectory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class ShipCannonAttack extends ShipRangeAttack {

    Equipment cannon;
    List<Trajectory> trajectories = new ArrayList<>();

    public ShipCannonAttack(BasicEntityShip ship, Equipment cannon, int cooldown) {
        super(ship, cooldown);
        this.cannon = cannon;
    }

    public void checkAndPerformCannon(LivingEntity target) {
        if (!this.inCooldown()) {
            if (target != null && target.isAlive()) {
                LivingEntity ship = getShip();
                Vec3 initPos = new Vec3(ship.getX(), ship.getEyeY(), ship.getZ());

                double canon_vel = cannon.getStat(EquipmentStatType.CANNON_MISSLE_VELOCITY);
                Vec3 gravity = new Vec3(0, -9.8f, 0);
                Vec3 end_spot = target.position();
                Vec3 fireVec = Trajectory.calFireVec(ship.position(), end_spot, gravity, canon_vel);
                if (fireVec != null) {
                    int id = ship.getRandom().nextInt();
                    Trajectory t = new Trajectory(initPos, fireVec, gravity, id, Trajectory.DEFAULT_PROJECTILE_SIZE);
                    trajectories.add(t);
                    this.resetCooldown();

                    // send to client trajectory
                    ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new TrajectoryPacket(t));
                }

            }
        }
    }

    public void doHitTarget(Entity entity) {
        BasicEntityShip ship = getShip();
        entity.hurt(ship.damageSources().mobAttack(ship), (float) ship.getAttributeValue(ModAttribute.FIREPOWER.get()));
    }


    protected void tickTrajectories() {
        List<Trajectory> invalids = new ArrayList<>();

//        System.out.println(trajectories.size());
        for (var t: trajectories) {

            HitResult hitResult = t.getHitResult(entity -> entity instanceof LivingEntity livingEntity && this.getShip().isEnemy(livingEntity), getShip().level());
            if (hitResult.getType() != HitResult.Type.MISS) {
                if (hitResult instanceof EntityHitResult ehr) {
                    Entity entity = ehr.getEntity();
                    doHitTarget(entity);
                }

                ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new ClientRemoveTrajectoryPacket(t.getId()));
                invalids.add(t);
            }

            if (t.checkTimeout()) {
                invalids.add(t);
            }

            t.tick(Trajectory.EULER_METHOD);
        }

        trajectories.removeAll(invalids);
    }


    @Override
    public void tick() {
        this.tickCooldown();
        this.tickTrajectories();
    }
}

