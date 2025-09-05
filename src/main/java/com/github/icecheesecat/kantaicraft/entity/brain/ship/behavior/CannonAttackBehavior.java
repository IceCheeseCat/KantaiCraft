package com.github.icecheesecat.kantaicraft.entity.brain.ship.behavior;

import com.github.icecheesecat.kantaicraft.entity.ship.CannonShip;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import com.github.icecheesecat.kantaicraft.util.tickable.EquipmentActionHandler;
import com.github.icecheesecat.kantaicraft.util.tickable.attack.CannonShipAttack;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class CannonAttackBehavior extends Behavior<CannonShip> {

    EquipmentActionHandler actionHandler;
    LivingEntity target;

    public CannonAttackBehavior() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleType.ACTION_HANDLER.get(), MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected void start(ServerLevel pLevel, CannonShip pEntity, long pGameTime) {
        pEntity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
    }

    @Override
    protected void tick(ServerLevel pLevel, CannonShip pOwner, long pGameTime) {
        var action = (CannonShipAttack) actionHandler.getActionByWeaponTypeAndNotInCooldown(EquipmentType.SMALL_CANNON);
        if (action != null) {
            action.checkAndPerformCannon(target);
        }
    }

    @Override
    protected void stop(ServerLevel pLevel, CannonShip pEntity, long pGameTime) {
        pEntity.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        this.actionHandler = null;
    }

    @Override
    protected boolean canStillUse(ServerLevel pLevel, CannonShip pEntity, long pGameTime) {
        return pEntity.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && pEntity.hasEnoughAmmo() && !actionHandler.getActionsByWeaponType(EquipmentType.SMALL_CANNON).isEmpty();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, CannonShip cannonShip) {
        var target = cannonShip.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if (target.isEmpty()) return false;
        else this.target = target.get();

        if (!cannonShip.hasEnoughAmmo()) {
            return false;
        }

        // has action handler and cannon action
        var actionHandler = cannonShip.getBrain().getMemory(ModMemoryModuleType.ACTION_HANDLER.get());
        if (actionHandler.isPresent() && !actionHandler.get().getActionsByWeaponType(EquipmentType.SMALL_CANNON).isEmpty()) {
            this.actionHandler = actionHandler.get();
            return true;
        }

        return false;
    }
}
