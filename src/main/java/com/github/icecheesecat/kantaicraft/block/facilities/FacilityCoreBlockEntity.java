package com.github.icecheesecat.kantaicraft.block.facilities;

import com.github.icecheesecat.kantaicraft.block.BlockStateProperties;
import com.github.icecheesecat.kantaicraft.block.facilities.pattern.FindPatternResult;
import com.github.icecheesecat.kantaicraft.util.CompoundTagHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class FacilityCoreBlockEntity extends FacilityBlockEntity {
    protected List<BlockPos> linkedFacilityBlocks = new ArrayList<>();
    protected BlockPos start, end;
    public FacilityCoreBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public boolean isTickable() {
        return getBlockState().getValue(BlockStateProperties.WORKING_FACILITY);
    }

    public List<BlockPos> getLinkedFacilityBlocks() {
        return linkedFacilityBlocks;
    }

    public void setup(FindPatternResult result) {
        this.linkedFacilityBlocks = new ArrayList<>(result.getBlockPoses());
        this.start = result.getStart();
        this.end = result.getEnd();
    }

    @Override
    protected final void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (linkedFacilityBlocks != null) {
            CompoundTagHelper.serializeList(pTag,"linked_facility_blocks", linkedFacilityBlocks, CompoundTagHelper::writeBlockPos);
        }
        if (this.start != null) {
            pTag.put("start", CompoundTagHelper.writeBlockPos(this.start));
        }
        if (this.end != null) {
            pTag.put("end", CompoundTagHelper.writeBlockPos(this.end));
        }
        pTag.put("extra_data", saveExtraData());
    }

    @Override
    public final void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("linked_facility_blocks")) {
            this.linkedFacilityBlocks = CompoundTagHelper.deserializeList(pTag,"linked_facility_blocks", CompoundTagHelper::readBlockPos);
        }
        if (pTag.contains("start")) {
            this.start = CompoundTagHelper.readBlockPos(pTag.getCompound("start"));
        }
        if (pTag.contains("end")) {
            this.end = CompoundTagHelper.readBlockPos(pTag.getCompound("end"));
        }
        if (pTag.contains("extra_data")) {
            this.loadExtraData(pTag.getCompound("extra_data"));
        }
    }

    /**
     * Saved extra data and sync to client
     */
    public abstract @NotNull CompoundTag saveExtraData();

    /**
     * Load extra data and sync with server
     */

    public abstract void loadExtraData(@NotNull CompoundTag nbt);

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("extra_data", saveExtraData());
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if (tag.contains("extra_data")) {
            this.loadExtraData(tag.getCompound("extra_data"));
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
