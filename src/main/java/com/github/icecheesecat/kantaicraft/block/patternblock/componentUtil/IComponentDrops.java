package com.github.icecheesecat.kantaicraft.block.patternblock.componentUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IComponentDrops {

    void dropAllWhenPatternDestryed(Level pLevel, BlockPos pPos);

}
