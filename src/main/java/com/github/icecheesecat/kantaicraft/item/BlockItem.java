package com.github.icecheesecat.kantaicraft.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class BlockItem extends net.minecraft.world.item.BlockItem implements ItemTagFoil {
    public BlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return ItemTagFoil.super.isFoil(pStack);
    }
}
