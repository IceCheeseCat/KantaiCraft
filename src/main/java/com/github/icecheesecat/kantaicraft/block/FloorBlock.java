package com.github.icecheesecat.kantaicraft.block;

import com.github.icecheesecat.kantaicraft.block.componentUtil.ComponentPattern;
import com.github.icecheesecat.kantaicraft.block.componentUtil.PatternType;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class FloorBlock extends ComponentBlock {

    public FloorBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected void putIfAbsentPatterns() {
        if (!this.allowPatterns.containsKey(PatternType.SHIPYARD)) {
            this.allowPatterns.put(PatternType.SHIPYARD, ComponentPattern.Builder.start(2, 2, 2)
                    .addBlock(0, 0, 0, ModBlock.FLOOR.get())
                    .addBlock(0, 0, 1, ModBlock.FLOOR.get())
                    .addBlock(1, 0, 0, Blocks.WATER)
                    .addBlock(1,0,1, Blocks.WATER)
                    .addBlock(0, 1, 1, ModBlock.CRANE.get())
                    .build());
        }

    }
}
