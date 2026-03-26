package com.github.icecheesecat.kantaicraft.entityship.brain.behavior;

import com.github.icecheesecat.kantaicraft.entityship.entity.cannoship.CannonEntityShip;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentClass;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.tickable.EquipmentActionHandler;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class CannonAttackBehavior extends Behavior<CannonEntityShip> {

    EquipmentActionHandler actionHandler;
    LivingEntity target;

    public CannonAttackBehavior() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleType.ACTION_HANDLER.get(), MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleType.ATTACK_TARGET_IN_SIGHT.get(), MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected void start(ServerLevel pLevel, CannonEntityShip pEntity, long pGameTime) {
        this.target = pEntity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        pEntity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(this.target, true));
    }

    @Override
    protected void tick(ServerLevel pLevel, CannonEntityShip pOwner, long pGameTime) {
        var action = actionHandler.getReadyCannonAction();
        if (action != null) {
            action.checkAndPerformCannon(target);
        }
    }

    @Override
    protected void stop(ServerLevel pLevel, CannonEntityShip pEntity, long pGameTime) {
        this.actionHandler = null;
    }

    @Override
    protected boolean canStillUse(ServerLevel pLevel, CannonEntityShip pEntity, long pGameTime) {
        return pEntity.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && pEntity.canRangeAttack();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, CannonEntityShip cannonShip) {

        if (cannonShip.forceMelee()) {
            return false;
        }

        if (!cannonShip.canRangeAttack()) {
            return false;
        }

        // has action handler and cannon action
        var actionHandler = cannonShip.getBrain().getMemory(ModMemoryModuleType.ACTION_HANDLER.get());
        if (actionHandler.isEmpty()) return false;
        if (actionHandler.get().getActionsByWeaponType(EquipmentClass.SMALL_CANNON).isEmpty()) return false;

        this.actionHandler = actionHandler.get();
        return true;
    }
}
