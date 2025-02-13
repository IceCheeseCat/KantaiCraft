package com.github.icecheesecat.kantaicraft.item;

import com.github.icecheesecat.kantaicraft.registries.ModItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class BlueprintItem extends Item {
    public BlueprintItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Rarity getRarity(ItemStack pStack) {
        if (pStack.hasTag()) {
            var nbt = pStack.getTag();
            byte r = nbt.getByte("rarity");
            switch (r) {
                case 1 -> {
                    return Rarity.UNCOMMON;
                }
                case 2 -> {
                    return Rarity.RARE;
                }
                case 3 -> {
                    return Rarity.EPIC;
                }
                default -> {
                    return Rarity.COMMON;
                }
            }
        }

        return Rarity.COMMON;
    }

    public boolean isValidStack(ItemStack stack) {
        return stack.getItem() == ModItem.SHIP_BLUEPRINT.get() && stack.hasTag() && stack.getTag().contains("progress");
    }

    public int getMaxProgress(ItemStack stack) {
        return stack.getTag().getInt("progress");
    }

}
