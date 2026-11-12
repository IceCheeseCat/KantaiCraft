package com.github.icecheesecat.kantaicraft.block.example;

import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib2.syncdata.annotation.UpdateListener;
import com.lowdragmc.lowdraglib2.syncdata.holder.blockentity.ISyncBlockEntity;
import com.lowdragmc.lowdraglib2.syncdata.holder.blockentity.ISyncPersistRPCBlockEntity;
import com.lowdragmc.lowdraglib2.syncdata.storage.FieldManagedStorage;
import com.lowdragmc.lowdraglib2.syncdata.storage.IManagedStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LDLibBlockEntity extends BlockEntity implements ISyncPersistRPCBlockEntity {

    private final FieldManagedStorage managedStorage = new FieldManagedStorage(this);

    @Persisted
    @DescSynced
    @UpdateListener(methodName = "OnLeftClick")
    private int leftClickCount = 0;
    @Persisted
    @DescSynced
    @UpdateListener(methodName = "OnRightClick")
    private int rightClickCount = 0;

    public LDLibBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlock.LDLIB_BETYPE.get(), pPos, pBlockState);
    }

    @Override
    public IManagedStorage getSyncStorage() {
        return this.managedStorage;
    }

    private void OnLeftClick(int oldValue, int newValue) {
        System.out.println("Left Click " + oldValue + " -> " + newValue);
    }

    private void OnRightClick(int oldValue, int newValue) {
        System.out.println("Right Click " + oldValue + " -> " + newValue);
    }

    public void addLeft(int pValue) {
        this.leftClickCount += pValue;
    }

    public void addRight(int pValue) {
        this.rightClickCount += pValue;
    }

    @Override
    public void asyncTick(long periodID) {
        ISyncPersistRPCBlockEntity.super.asyncTick(periodID);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, LDLibBlockEntity ldLibBlockEntity) {
        if (pLevel.isClientSide) {
            if (pLevel.getGameTime() % 20 == 0) {
                System.out.printf("Client value: L = %d, R = %d%n", ldLibBlockEntity.leftClickCount, ldLibBlockEntity.rightClickCount);
            }
        }
        else {
            if (pLevel.getGameTime() % 20 == 0) {
                System.out.printf("Server value: L = %d, R = %d%n", ldLibBlockEntity.leftClickCount, ldLibBlockEntity.rightClickCount);
            }
        }
    }
}