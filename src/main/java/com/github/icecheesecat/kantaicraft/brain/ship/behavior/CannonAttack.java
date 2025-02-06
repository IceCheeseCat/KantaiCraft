package com.github.icecheesecat.kantaicraft.brain.ship.behavior;

import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicCannonShip;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import com.github.icecheesecat.kantaicraft.util.tickable.EquipmentActionHandler;
import com.github.icecheesecat.kantaicraft.util.tickable.attack.ShipCannonAttack;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class CannonAttack extends Behavior<BasicCannonShip> {

    EquipmentActionHandler actionHandler;
    LivingEntity target;
    int roundRobinCooㄌldown;
    final static int MAX_ROUND_ROBIN_COOLDOWN = 20;

    public CannonAttack() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleType.ACTION_HANDLER.get(), MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected void start(ServerLevel pLevel, BasicCannonShip pEntity, long pGameTime) {
        pEntity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
    }

    @Override
    protected void tick(ServerLevel pLevel, BasicCannonShip pOwner, long pGameTime) {
        var action = (ShipCannonAttack) actionHandler.getActionByWeaponTypeAndNotInCooldown(EquipmentType.CANNON);
        if (action != null) {
            action.checkAndPerformCannon(target);
        }
    }

    @Override
    protected void stop(ServerLevel pLevel, BasicCannonShip pEntity, long pGameTime) {
        pEntity.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        this.actionHandler = null;
    }

    @Override
    protected boolean canStillUse(ServerLevel pLevel, BasicCannonShip pEntity, long pGameTime) {
        return pEntity.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && pEntity.hasEnoughAmmo() && !actionHandler.getActionsByWeaponType(EquipmentType.CANNON).isEmpty();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, BasicCannonShip basicCannonShip) {
        var target = basicCannonShip.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if (target.isEmpty()) return false;
        else this.target = target.get();

        if (!basicCannonShip.hasEnoughAmmo()) {
            return false;
        }

        // has action handler and cannon action
        var actionHandler = basicCannonShip.getBrain().getMemory(ModMemoryModuleType.ACTION_HANDLER.get());
        if (actionHandler.isPresent() && !actionHandler.get().getActionsByWeaponType(EquipmentType.CANNON).isEmpty()) {
            this.actionHandler = actionHandler.get();
            return true;
        }

        return false;
    }
}
