package com.github.icecheesecat.kantaicraft.block.patternblock;

import com.github.icecheesecat.kantaicraft.block.patternblock.componentUtil.ComponentPattern;
import com.github.icecheesecat.kantaicraft.block.patternblock.componentUtil.PatternType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PatternBlock extends BaseEntityBlock {

    protected Map<PatternType, ComponentPattern> allowPatterns = new HashMap<>();

    protected PatternBlock(Properties pProperties) {
        super(pProperties);
    }

    protected abstract void putIfAbsentPatterns();

    protected List<BlockPos> checkPattern(Level pLevel, BlockPos pPos) {
        for (var ap: allowPatterns.entrySet()) {
            List<BlockPos> blockPoses = ap.getValue().findPattern(pLevel, pPos);

            if (blockPoses != null) return blockPoses;
        }

        return null;
    }

}
