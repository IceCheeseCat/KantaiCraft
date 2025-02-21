package com.github.icecheesecat.kantaicraft.block;

import com.github.icecheesecat.kantaicraft.block.basic.ComponentBlock;
import com.github.icecheesecat.kantaicraft.block.basic.componentUtil.ComponentPattern;
import com.github.icecheesecat.kantaicraft.block.basic.componentUtil.PatternType;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

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
