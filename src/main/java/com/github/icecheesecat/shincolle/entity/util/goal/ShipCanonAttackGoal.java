package com.github.icecheesecat.shincolle.entity.util.goal;

import com.github.icecheesecat.shincolle.entity.util.BasicCanonShip;
import com.github.icecheesecat.shincolle.entity.util.BasicEntityShip;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class ShipCanonAttackGoal<T extends LivingEntity> extends Goal {
    // 1. find target, if not null start attacking
    // 2. if target died reset to find next target
    private BasicCanonShip ship;
    @Nullable
    private LivingEntity target;

    public ShipCanonAttackGoal(BasicCanonShip ship, Class<T> c) {
        this.ship = ship;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.TARGET, Flag.MOVE));
    }

    @Override
    public void stop() {

    }

    @Override
    public void start() {
        super.start();
    }

    // TODO other option of targeting
    // TODO move to line of sight in a distance
    @Override
    public void tick() {
        for (var sca: this.ship.getShipCanonAttack()) {
            sca.checkAndPerformCanon(this.ship.getRadar().getClosestTarget());
        }

    }


    @Override
    public boolean canUse() {
        List<LivingEntity> list = this.ship.getRadar().getTargets();
        return !list.isEmpty();
    }

    // TODO control mechanism
    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

}
