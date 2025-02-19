package com.github.icecheesecat.kantaicraft.block;

import com.github.icecheesecat.kantaicraft.block.componentUtil.ComponentPattern;
import com.github.icecheesecat.kantaicraft.block.componentUtil.PatternType;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class FloorBlock extends ComponentBlock{

    public FloorBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
    }

    @Override
    protected void putIfAbsentPatterns() {
        this.allowPatterns.putIfAbsent(PatternType.SHIPYARD, ComponentPattern.ComponentPatternBuilder.start(2, 3, 2, PatternType.SHIPYARD)
                .addBlock(0, 0, 0, ModBlock.FLOOR.get())
                .addBlock(0, 0, 1, ModBlock.FLOOR.get())
                .addBlock(1, 0, 0, ModBlock.FLOOR.get())
                .addBlock(1, 0, 1, Blocks.WATER).build());

    }
}
