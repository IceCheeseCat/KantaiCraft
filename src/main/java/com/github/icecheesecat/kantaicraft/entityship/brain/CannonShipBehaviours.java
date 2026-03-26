package com.github.icecheesecat.kantaicraft.entityship.brain;

import com.github.icecheesecat.kantaicraft.entityship.entity.cannoship.CannonEntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModAttribute;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class CannonShipBehaviours {

    public static OneShot<LivingEntity> setLookTargetToAttackTarget(CannonEntityShip cannonEntityShip) {
        return SetEntityLookTarget.create(livingEntity -> cannonEntityShip.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).filter(
                attack_target -> attack_target.is(livingEntity)
        ).isPresent(), (float) cannonEntityShip.getAttributeValue(ModAttribute.LOS.get()));
    }

}
