package com.github.icecheesecat.kantaicraft.item;

import net.minecraft.world.item.ItemStack;

public interface ItemTagFoil {
    default boolean isFoil(ItemStack itemStack) {
        return itemStack.hasTag() || itemStack.isEnchanted();
    }
}
