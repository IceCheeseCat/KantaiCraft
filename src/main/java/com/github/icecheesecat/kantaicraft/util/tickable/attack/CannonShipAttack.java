package com.github.icecheesecat.kantaicraft.util.tickable.attack;

import com.github.icecheesecat.kantaicraft.capability.ServerLevelTrajectoryCapability;
import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentStatType;
import com.github.icecheesecat.kantaicraft.registries.ModAttribute;
import com.github.icecheesecat.kantaicraft.util.Trajectory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class CannonShipAttack extends ShipRangeAttack {

    Equipment cannon;

    public CannonShipAttack(EntityShip entityShip, Equipment cannon, int cooldown) {
        super(entityShip, cooldown);
        this.cannon = cannon;
    }

    public void checkAndPerformCannon(LivingEntity target) {
        if (!this.inCooldown()) {
            if (target != null && target.isAlive()) {
                LivingEntity ship = this.entityShip;
                Vec3 initPos = new Vec3(ship.getX(), ship.getEyeY(), ship.getZ()); // change to cannon model fire spot

                double canon_vel = cannon.getStat(EquipmentStatType.CANNON_MISSILE_VELOCITY);
                Vec3 end_spot = target.position();
                Vec3 fireVec = Trajectory.calFireVec(ship.position(), end_spot, GRAVITY, canon_vel);
                if (fireVec != null) {
                    int id = ship.getRandom().nextInt();
                    Trajectory newTrajectory = new Trajectory(id, this.entityShip.getUUID(), initPos, fireVec, GRAVITY, Trajectory.SMALL_PROJECTILE_SIZE, calculateDamage(this.cannon, this.entityShip), (entity -> this.entityShip.canAttack((LivingEntity) entity)));
                    addTrajectoryToLevel((ServerLevel) ship.level(), newTrajectory);
                    this.resetCooldown();
                }

            }
        }
    }

    // TODO damage calculation
    public static float calculateDamage(Equipment equipment, EntityShip entityShip) {
        return (float) (entityShip.getAttributeValue(ModAttribute.FIREPOWER.get()) + equipment.getStat(EquipmentStatType.FIREPOWER));
    }

//    protected void tickTrajectories() {
//        List<Trajectory> invalids = new ArrayList<>();
//
//        for (var t: trajectories) {
//
//            HitResult hitResult = t.getHitResult(entity -> entity instanceof LivingEntity livingEntity && this.ship.canAttack(livingEntity), this.ship.level());
//            if (hitResult.getType() != HitResult.Type.MISS) {
//                if (hitResult instanceof EntityHitResult ehr) {
//                    Entity entity = ehr.getEntity();
//                    doHitTarget(entity);
//                }
//
//                ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new ClientRemoveTrajectoryPacket(t.getId()));
//                invalids.add(t);
//            }
//
//            if (t.checkTimeout()) {
//                invalids.add(t);
//            }
//
//            t.tick();
//        }
//
//        trajectories.removeAll(invalids);
//    }


    @Override
    public void tick() {
        this.tickCooldown();
    }

    private void addTrajectoryToLevel(ServerLevel serverLevel, Trajectory trajectory) {
        serverLevel.getCapability(ServerLevelTrajectoryCapability.TOKEN).ifPresent(
                levelTrajectory -> {
                    levelTrajectory.appendTrajectory(trajectory);
                }
        );
    }
}

