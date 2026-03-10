package com.github.icecheesecat.kantaicraft.entityship.brain.behavior;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModActivity;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class ReturnToSitDownActivity extends ActivityReturnCountdown {
    public ReturnToSitDownActivity() {
        super(ModMemoryModuleType.SIT_BACK_DOWN_COUNTDOWN.get(),
                MemoryStatus.VALUE_PRESENT,
                ModActivity.SITTING.get(),
                ReturnToSitDownActivity::pauseCondition,
                ReturnToSitDownActivity::resetCondition,
                50,
                ReturnToSitDownActivity::action);
    }

    private static boolean pauseCondition(ServerLevel serverLevel, LivingEntity livingEntity, long gameTime) {
        var brain = livingEntity.getBrain();
        boolean hasItems = false;
        if (livingEntity.getBrain().hasMemoryValue(ModMemoryModuleType.ITEMS_TO_PICK_UP.get())) {
            if (!livingEntity.getBrain().getMemory(ModMemoryModuleType.ITEMS_TO_PICK_UP.get()).get().isEmpty()) {
                hasItems = true;
            }
        }

        return brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET) || brain.hasMemoryValue(ModMemoryModuleType.NEAREST_WANTED_ITEM.get()) || hasItems;
    }
    private static boolean resetCondition(ServerLevel serverLevel, LivingEntity livingEntity, long gameTime) {
        return false;
    }

    private static void action(ServerLevel serverLevel, LivingEntity livingEntity, long gameTime) {
        if (livingEntity instanceof EntityShip entityShip) {
            entityShip.toggleSitDown();
        }
    }
}
