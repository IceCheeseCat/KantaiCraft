package com.github.icecheesecat.kantaicraft.block.facilities;

import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import com.github.icecheesecat.kantaicraft.util.CompoundTagHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FacilityBlockEntity extends BlockEntity {
    private boolean facilityRemoved = false;
    private BlockPos corePos = new BlockPos(0, -100, 0);
    public FacilityBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlock.FACILITY_BETYPE.get(), pPos, pBlockState);
    }

    public FacilityBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pPos, BlockState pBlockState) {
        super(blockEntityType, pPos, pBlockState);
    }

    public void setCorePos(BlockPos corePos) {
        this.corePos = corePos;
    }

    public BlockPos getCorePos() {
        return corePos;
    }

    public @Nullable FacilityCoreBlockEntity getCoreBlockEntity() {
        return this.level.getBlockEntity(this.corePos) instanceof FacilityCoreBlockEntity fc ? fc : null;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("core_pos", CompoundTagHelper.writeBlockPos(corePos));
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("core_pos")) {
            this.corePos = CompoundTagHelper.readBlockPos(pTag.getCompound("core_pos"));
        }
    }

    public void onRemove() {
        this.facilityRemoved = true;
        this.corePos = BlockPos.ZERO;
    }

    public boolean isFacilityRemoved() {
        return this.facilityRemoved;
    }
}
