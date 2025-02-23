package com.github.icecheesecat.kantaicraft.block.basic;

import com.github.icecheesecat.kantaicraft.block.basic.componentUtil.PatternType;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class CoreBlockEntity extends BlockEntity {

    List<BlockPos> linkedBlockPos = new ArrayList<>();

    public CoreBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlock.CORE_BETYPE.get(), pPos, pBlockState);
    }

    public void addLinked(BlockPos blockPos) {
        if (blockPos.equals(this.getBlockPos())) return;
        this.linkedBlockPos.add(blockPos);
        setChanged();
    }

    public void resetLinked() {
        for (var pos: linkedBlockPos) {
            if (this.level.getBlockEntity(pos) instanceof ComponentBlockEntity cbe) {
                cbe.removeCorePos();
            }
        }

        this.linkedBlockPos.clear();
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("linked", BlockPosHelper.listWriteNbt(this.linkedBlockPos));
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.linkedBlockPos = BlockPosHelper.listReadNbt(pTag.getCompound("linked"));
    }

}
