package com.github.icecheesecat.shincolle.entity.util.goal;

import com.github.icecheesecat.shincolle.entity.util.BasicEntityShip;
import com.github.icecheesecat.shincolle.entity.util.attack.ShipMeleeAttack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class ShipMeleeAttackGoal extends MeleeAttackGoal {

    private ShipMeleeAttack shipMeleeAttack;
    public ShipMeleeAttackGoal(BasicEntityShip ship, double speedModifier, boolean mustSee, ShipMeleeAttack shipMeleeAttack) {
        super(ship, speedModifier, mustSee);
        this.shipMeleeAttack = shipMeleeAttack;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target, double _d) {
//        double d0 = this.getAttackReachSqr(target);
        shipMeleeAttack.checkAndPerformMelee(target);
    }

    @Override
    public void start() {
        if (((BasicEntityShip)this.mob).isCanMelee()) {
            super.start();
        }
    }

    @Override
    public boolean canUse() {
        return super.canUse() && ((BasicEntityShip)this.mob).isCanMelee();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && ((BasicEntityShip)this.mob).isCanMelee();
    }


}
