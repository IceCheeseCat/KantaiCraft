package com.github.icecheesecat.kantaicraft.block.shipyard;

import com.github.icecheesecat.kantaicraft.block.patternblock.CoreBlock;
import com.github.icecheesecat.kantaicraft.block.patternblock.componentUtil.ComponentPattern;
import com.github.icecheesecat.kantaicraft.block.patternblock.componentUtil.PatternType;
import com.github.icecheesecat.kantaicraft.blueprint.Blueprint;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import com.github.icecheesecat.kantaicraft.registries.ModItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShipyardCoreBlock extends CoreBlock {

    public ShipyardCoreBlock(Properties pProperties) {
        super(pProperties, PatternType.SHIPYARD);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ShipyardBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlock.SHIPYARD_BETPYE.get(), ShipyardBlockEntity::tick);
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
                    .addBlock(0,1,0, ModBlock.SHIPYARD_CORE.get())
                    .build());
        }
    }

    @Override
    public void dropAllWhenPatternDestryed(Level pLevel, BlockPos pPos) {
        if (pLevel.getBlockEntity(pPos) instanceof ShipyardBlockEntity shipyardBlockEntity) {
            double x = pPos.getCenter().x;
            double y = pPos.getCenter().y;
            double z = pPos.getCenter().z;

            // drop itemHandler
            shipyardBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(
                    itemHandler -> {
                        for (int i = 0; i < itemHandler.getSlots(); i++) {
                            ItemStack itemStack = itemHandler.getStackInSlot(i);
                            if (itemStack.isEmpty()) continue;
                            ItemEntity itemEntity = new ItemEntity(pLevel, x, y, z, itemStack);
                            pLevel.addFreshEntity(itemEntity);
                        }
                    }
            );

        }
    }
}
