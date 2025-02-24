package com.github.icecheesecat.kantaicraft.block.basic;

import com.github.icecheesecat.kantaicraft.block.basic.componentUtil.PatternType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CoreBlockEntity extends BlockEntity {

    List<BlockPos> linkedBlockPos = new ArrayList<>();
    boolean canUse;

    public CoreBlockEntity(BlockEntityType<?> beType, BlockPos pPos, BlockState pBlockState) {
        super(beType, pPos, pBlockState);
    }

    public void addLinked(BlockPos blockPos) {
        if (blockPos.equals(this.getBlockPos())) return;
        this.linkedBlockPos.add(blockPos);
        setChanged();
    }

    /**
     * Reset both ComponentBlockEntity's corePos and blockState
     */
    public void resetLinksAndBlockStates() {

        for (var pos: linkedBlockPos) {
            if (this.level.getBlockState(pos).getBlock().equals(Blocks.AIR)) continue;
            if (this.level.getBlockEntity(pos) instanceof ComponentBlockEntity cbe) {
                cbe.removeCorePos();
                this.level.setBlockAndUpdate(pos, this.level.getBlockState(pos).setValue(BlockStateProperties.PATTERN_TYPE, PatternType.NONE));
            }
        }

        this.linkedBlockPos.clear();
        setChanged();
    }

    public boolean canUse() {
        return canUse;
    }

    public void setCanUse(boolean canUse) {
        this.canUse = canUse;
        setChanged();
        level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("linked", BlockPosHelper.listWriteNbt(this.linkedBlockPos));
        pTag.putBoolean("can_use", this.canUse);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.linkedBlockPos = BlockPosHelper.listReadNbt(pTag.getCompound("linked"));
        this.canUse = pTag.getBoolean("can_use");
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putBoolean("can_use", this.canUse);
        return nbt;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        var nbt = pkt.getTag();
        this.canUse = nbt.getBoolean("can_use");
    }
}
