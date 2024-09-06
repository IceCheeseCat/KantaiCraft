package com.github.icecheesecat.kantaicraft.util.goal;

import com.github.icecheesecat.kantaicraft.config.ConfigBehaviour;
import com.github.icecheesecat.kantaicraft.entity.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.init.ModShipAttributes;
import com.github.icecheesecat.kantaicraft.util.tickable.ShipTickableAction;
import com.github.icecheesecat.kantaicraft.util.tickable.attack.ShipCannonAttack;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.Path;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class ShipCannonAttackGoal extends Goal {
    // 1. find target, if not null start attacking
    // 2. if target died reset to find next target
    private BasicEntityShip ship;
    private GoalStates states;
    @Nullable
    private LivingEntity target;
    private final boolean mustSee;
    private int redirectTimer = ConfigBehaviour.SHIP_REDIRECTING_TIMER;
    private int continuousFireDelay = ConfigBehaviour.CANNON_CONTINUOUS_FIRE_DELAY;
    private int timerUntilTargetLoss = 0;

    public ShipCannonAttackGoal(BasicEntityShip ship, boolean mustSee) {
        this.ship = ship;
        this.mustSee = mustSee;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.TARGET, Flag.MOVE));
    }

    @Override
    public void stop() {
        this.target = null;
        redirectTimer = ConfigBehaviour.SHIP_REDIRECTING_TIMER;
        continuousFireDelay = 0;
    }

    @Override
    public void start() {
        timerUntilTargetLoss = ConfigBehaviour.CANNON_TARGET_LOSS_TIMER;
        states = GoalStates.RUNNING;
        redirectTimer = ConfigBehaviour.SHIP_REDIRECTING_TIMER;
        continuousFireDelay = 0;
        super.start();
    }

    // TODO other option of targeting
    // TODO move to line of sight in a distance
    @Override
    public void tick() {

        switch (states) {
            case LOSS_TARGET_SIGHT -> {
                if (!doTargeting()) {
                    if (redirectTimer < 0) {
                        // Path find to target
                        PathNavigation pathNavigation = ship.getNavigation();
                        Path path = pathNavigation.createPath(target, (int) Objects.requireNonNull(this.ship.getAttribute(ModShipAttributes.LOS.get())).getValue());
                        if (path != null) {
                            ship.getNavigation().moveTo(path, 1.0d);
                        }

                        redirectTimer = ConfigBehaviour.SHIP_REDIRECTING_TIMER;
                    }
                }
                else {
                    states = GoalStates.RUNNING;
                }

                redirectTimer--;
                timerUntilTargetLoss--;
            }
            case RUNNING -> {
                assert target != null;
                ship.getLookControl().setLookAt(target);
                List<ShipTickableAction> cannons = this.ship.getActionHandler().getActionsByWeaponType(EquipmentType.CANNON);
                if (cannons.size() == 0) {
                    return;
                }

                if (ship.hasLineOfSight(target)) {
                    if (continuousFireDelay > 0) {
                        return;
                    }
                    for (var c: cannons) {
                        if (c instanceof ShipCannonAttack ca) {
                            if (ca.checkCooldown()) {
                                ca.checkAndPerformCanon(target);
                                continuousFireDelay = ConfigBehaviour.CANNON_CONTINUOUS_FIRE_DELAY;
                                break;
                            }
                        }
                    }

                }
                else {
                    states = GoalStates.LOSS_TARGET_SIGHT;
                }

                continuousFireDelay--;
            }
        }
    }


    // Cannon attack targeting method
    private boolean doTargeting() {
        LivingEntity entity =  this.ship.getRadar().getClosestTarget();
        if (entity == null) {
            target = null;
            return false;
        }
        if (mustSee) {
            if (ship.hasLineOfSight(entity)) {
                target = entity;
                timerUntilTargetLoss = ConfigBehaviour.CANNON_TARGET_LOSS_TIMER;
                return true;
            }
        }
        else {
            target = entity;
            timerUntilTargetLoss = ConfigBehaviour.CANNON_TARGET_LOSS_TIMER;
            return true;
        }

        return false;
    }

    @Override
    public boolean canUse() {
        doTargeting();
        return this.timerUntilTargetLoss > 0 && this.ship.getActionHandler().contains(EquipmentType.CANNON);
    }

    // TODO control mechanism
    @Override
    public boolean canContinueToUse() {
        return this.timerUntilTargetLoss > 0 && this.ship.getActionHandler().contains(EquipmentType.CANNON);
    }

}
