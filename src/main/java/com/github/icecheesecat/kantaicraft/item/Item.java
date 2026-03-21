package com.github.icecheesecat.kantaicraft.item;

import net.minecraft.world.item.ItemStack;

public class Item extends net.minecraft.world.item.Item implements ItemTagFoil {
    public Item(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return ItemTagFoil.super.isFoil(pStack);
    }
}
