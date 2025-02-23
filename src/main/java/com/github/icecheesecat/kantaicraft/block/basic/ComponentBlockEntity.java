package com.github.icecheesecat.kantaicraft.block.basic;

import com.github.icecheesecat.kantaicraft.block.basic.componentUtil.PatternType;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class ComponentBlockEntity extends BlockEntity {

    BlockPos corePos;

    public ComponentBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlock.COMPONENT_BETYPE.get(), pPos, pBlockState);
    }

    public BlockPos getCorePos() {
        return this.corePos;
    }

    public void setCorePos(BlockPos corePos) {
//        if (this.corePos != null) {
//            throw new IllegalStateException("core pos is not null");
//        }

        this.corePos = corePos;
        setChanged();
    }

    public void removeCorePos() {
        this.corePos = null;
        setChanged();
    }

    public CoreBlockEntity getCoreBlockEntity(Level level) {
        if (this.getCorePos() == null) {
            return null;
        }

        BlockEntity be = level.getBlockEntity(this.getCorePos());
        if (be instanceof CoreBlockEntity coreBlockEntity) {
            return coreBlockEntity;
        }

        return null;
    }

    public CoreBlock getCoreBlock(Level level) {
        if (level.getBlockState(corePos).getBlock() instanceof CoreBlock coreBlock) {
            return coreBlock;
        }
        return null;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (corePos != null) {
            CompoundTag coreNbt = BlockPosHelper.writeNbt(this.corePos);
            pTag.put("core_pos", coreNbt);
        }

    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("core_pos")) {
            this.corePos = BlockPosHelper.readNbt(pTag.getCompound("core_pos"));
        }

    }

}
