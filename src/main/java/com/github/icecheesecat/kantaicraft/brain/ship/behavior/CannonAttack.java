package com.github.icecheesecat.kantaicraft.brain.ship.behavior;

import com.github.icecheesecat.kantaicraft.customObjects.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicCannonShip;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import com.github.icecheesecat.kantaicraft.util.tickable.EquipmentActionHandler;
import com.github.icecheesecat.kantaicraft.util.tickable.attack.ShipCannonAttack;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Optional;

public class CannonAttack extends Behavior<BasicCannonShip> {

    ShipCannonAttack attack;

    public CannonAttack() {
        super(ImmutableMap.of(ModMemoryModuleType.IS_GUARDING.get(), MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected void start(ServerLevel pLevel, BasicCannonShip pEntity, long pGameTime) {
        Optional<LivingEntity> target = pEntity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if (target.isPresent()) {
            this.attack.checkAndPerformCannon(target.get());
        }
        else {
            System.err.println(pEntity.toString() + " error: target is not present!");
        }
    }

    @Override
    protected void tick(ServerLevel pLevel, BasicCannonShip pOwner, long pGameTime) {
    }

    @Override
    protected void stop(ServerLevel pLevel, BasicCannonShip pEntity, long pGameTime) {
        this.attack = null;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, BasicCannonShip basicCannonShip) {
        // has enough ammo, if has enough, consume ammo
        if (!basicCannonShip.hasEnoughAmmo()) {
            return false;
        }
        else {
            basicCannonShip.useAmmo();
        }

        var opHandler = basicCannonShip.getBrain().getMemory(ModMemoryModuleType.ACTION_HANDLER.get());
        if (opHandler.isPresent()) {
            EquipmentActionHandler handler = opHandler.get();
            var actions = handler.getActionsByWeaponTypeAndNotInCooldown(EquipmentType.CANNON);
            if (!actions.isEmpty()) {
                this.attack = (ShipCannonAttack) actions.get(0);
            }

            return true;
        }
        else {
            return false;
        }
    }
}
