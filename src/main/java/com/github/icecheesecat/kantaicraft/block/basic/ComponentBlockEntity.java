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
    List<BlockPos> neighbors = new ArrayList<>();

    public ComponentBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlock.COMPONENT_BETYPE.get(), pPos, pBlockState);
    }

    public BlockPos getCorePos() {
        return this.corePos;
    }

    public void setCorePos(BlockPos corePos) {
        if (this.corePos != null) {
            throw new IllegalStateException("core pos is not null");
        }

        this.corePos = corePos;
        setChanged();
    }

    public void removeCorePos() {
        this.corePos = null;
        setChanged();
    }

    public BlockEntity getCoreBlockEntity(Level level) {
        if (this.getCorePos() == null) {
            return null;
        }

        BlockEntity be = level.getBlockEntity(this.getCorePos());
        if (be instanceof ComponentBlockEntity cbe) {
            return cbe.getCoreBlockEntity(level);
        }

        return null;
    }

    public void setNeighbors(List<BlockPos> neighbors) {
        this.neighbors = neighbors;
        setChanged();
    }

    public void removeNeighbors() {
        this.neighbors.clear();
        setChanged();
    }

    public boolean insidePatternPoses(BlockPos pos) {
        return this.neighbors.stream().anyMatch(pos::equals);
    }

    public void setCoreToComponentBlockEntity(BlockState blockState) {
        level.setBlockEntity(new ComponentBlockEntity(this.corePos, blockState));
        setChanged();
    }



    public void resetAllNeighbors() {

        for (var pos: this.neighbors) {
            BlockState blockState = level.getBlockState(pos).setValue(ComponentBlock.PATTERN_TYPE, PatternType.NONE).setValue(ComponentBlock.IS_CORE, false);
            if (pos.equals(corePos)) {
                this.setCoreToComponentBlockEntity(blockState);
            }

            if (level.getBlockEntity(pos) instanceof ComponentBlockEntity cbe && !pos.equals(this.getBlockPos())) {
                if (cbe instanceof IComponentDrops iComponentDrops) {
                    iComponentDrops.dropAllWhenPatternDestryed();
                }
                cbe.removeCorePos();
                cbe.removeNeighbors();
            }
        }

        this.removeCorePos();
        this.removeNeighbors();
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (corePos != null) {
            CompoundTag coreNbt = BlockPosHelper.writeNbt(this.corePos);
            pTag.put("core_pos", coreNbt);
        }

        pTag.put("neighbor_poses", BlockPosHelper.listWriteNbt(this.neighbors));
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("core_pos")) {
            this.corePos = BlockPosHelper.readNbt(pTag.getCompound("core_pos"));
        }

        this.neighbors = BlockPosHelper.listReadNbt(pTag.getCompound("neighbor_poses"));
    }

}
