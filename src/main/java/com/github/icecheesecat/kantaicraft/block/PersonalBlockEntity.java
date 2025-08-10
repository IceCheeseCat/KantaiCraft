package com.github.icecheesecat.kantaicraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class PersonalBlockEntity extends BlockEntity {

    private UUID bindedPlayer;
    private boolean visitable = false;

    public PersonalBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public boolean canVisit() {
        return this.visitable || this.bindedPlayer == null;
    }

    public void setVisitable(boolean bool) {
        this.visitable = bool;
    }

    public void bindPlayer(Player player) {
        if (canBind(player)) {
            this.bindTo(player);
        }
    }

    public boolean canBind(Player player) {
        return bindedPlayer == null;
    }

    public void bindTo(Player player) {
        this.bindedPlayer = player.getUUID();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (this.bindedPlayer != null) {
            pTag.putUUID("bindedplayer", this.bindedPlayer);
        }
        pTag.putBoolean("visitable", this.visitable);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("bindedplayer")) {
            this.bindedPlayer = pTag.getUUID("bindedplayer");
        }
        this.visitable = pTag.getBoolean("visitable");
    }
}
