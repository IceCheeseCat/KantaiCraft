package com.github.icecheesecat.kantaicraft.item;

import com.github.icecheesecat.kantaicraft.blueprint.Blueprint;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlueprintItem extends Item {

    public BlueprintItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Rarity getRarity(ItemStack pStack) {
        if (pStack.hasTag()) {
            var blueprint = Blueprint.createFromTag(pStack.getTag());
            return blueprint.getRarity();

        }

        return Rarity.COMMON;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.hasTag()) {
            Blueprint blueprint = Blueprint.createFromTag(pStack.getTag());
            pTooltipComponents.add(Component.literal(blueprint.getShipClass().name()));
            pTooltipComponents.add(blueprint.getEntityType().get().getDescription());
        }
    }
}
