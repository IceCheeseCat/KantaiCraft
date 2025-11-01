package com.github.icecheesecat.kantaicraft.block.patternblock;

import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import com.github.icecheesecat.kantaicraft.util.CompoundTagHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ComponentBlockEntity extends BlockEntity {

    BlockPos corePos;

    public ComponentBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlock.COMPONENT_BETYPE.get(), pPos, pBlockState);
    }

    public BlockPos getCorePos() {
        return this.corePos;
    }

    public void setCorePos(BlockPos corePos) {
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
        if (getCorePos() == null) return null;
        if (level.getBlockState(corePos).getBlock() instanceof CoreBlock coreBlock) {
            return coreBlock;
        }
        return null;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (corePos != null) {
            pTag.put("core_pos", CompoundTagHelper.getNbtFromBlockPos(this.corePos));
        }

    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("core_pos")) {
            this.corePos = CompoundTagHelper.getBlockPos(pTag.getCompound("core_pos"));
        }

    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        if (this.corePos != null) {
            nbt.put("core_pos", CompoundTagHelper.getNbtFromBlockPos(this.corePos));
        }

        return nbt;
    }

}
