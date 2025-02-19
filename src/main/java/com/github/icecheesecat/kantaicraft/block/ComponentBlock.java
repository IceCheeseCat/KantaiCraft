package com.github.icecheesecat.kantaicraft.block;

import com.github.icecheesecat.kantaicraft.block.componentUtil.ComponentPattern;
import com.github.icecheesecat.kantaicraft.block.componentUtil.PatternType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public abstract class ComponentBlock extends Block {

    Map<PatternType, ComponentPattern> allowPatterns = new HashMap<>();

    public ComponentBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        this.putIfAbsentPatterns();
        for (var ap: allowPatterns.entrySet()) {
            if (ap.getValue().findPattern(pLevel, pPos)){
                System.out.println("Success Pattern");
            }
            else {
                System.out.println("Failed Pattern");
            }
        }

        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
    }

    protected abstract void putIfAbsentPatterns();
}
