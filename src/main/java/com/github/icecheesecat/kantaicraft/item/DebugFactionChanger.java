package com.github.icecheesecat.kantaicraft.item;

import com.github.icecheesecat.kantaicraft.common.CommonEntityData;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DebugFactionChanger extends Item {
    public DebugFactionChanger(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity livingEntity, InteractionHand hand) {
        Level level = player.level();
        if (level.isClientSide) return InteractionResult.PASS;

        if (livingEntity instanceof BasicEntityShip ship) {
            int factionId = ship.getFactionId();
            if (factionId == CommonEntityData.hostileFaction) {
                ((BasicEntityShip) livingEntity).setFactionId(CommonEntityData.noFaction);
                player.sendSystemMessage(Component.literal("Set " + livingEntity.getName() + " " + livingEntity.getId() + " to hostile"));
            }
            else {
                ((BasicEntityShip) livingEntity).setFactionId(CommonEntityData.hostileFaction);
                player.sendSystemMessage(Component.literal("Set " + livingEntity.getName() + " " + livingEntity.getId() + " to neutral"));
            }
        }

        return InteractionResult.SUCCESS;
    }
}
