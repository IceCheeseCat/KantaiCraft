package com.github.icecheesecat.kantaicraft.entityship.brain.behavior;

import com.github.icecheesecat.kantaicraft.entityship.brain.sensor.ContainerWithAmmoSensor;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModItem;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

import java.util.Comparator;

public class ResupplyAmmoFromContainer extends Behavior<EntityShip> {

    BlockPos target;
    boolean chestOpened = false;

    public ResupplyAmmoFromContainer() {
        super(ImmutableMap.of(
                ModMemoryModuleType.NEED_RESUPPLY.get(), MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.PATH, MemoryStatus.VALUE_ABSENT,
                ModMemoryModuleType.NEARBY_CHEST_WITH_AMMO.get(), MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT,
                ModMemoryModuleType.IS_SITTING.get(), MemoryStatus.VALUE_ABSENT
                ),
                400);
    }

    private BlockPos findClosetContainer(EntityShip pEntity) {
        return pEntity.getBrain().getMemory(ModMemoryModuleType.NEARBY_CHEST_WITH_AMMO.get()).get().stream().min(Comparator.comparingInt(pos -> pEntity.blockPosition().distManhattan(pos))).get();
    }

    @Override
    protected void start(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        this.target = findClosetContainer(pEntity);
        pEntity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.target));
        pEntity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(this.target, pEntity.getRunSpeedModifier(), 1));
    }

    @Override
    protected void stop(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        this.target = null;
        pEntity.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        pEntity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        pEntity.getBrain().eraseMemory(MemoryModuleType.PATH);
        pEntity.getNavigation().stop();
        pEntity.getBrain().eraseMemory(ModMemoryModuleType.NEED_RESUPPLY.get());
    }

    private void closeChest(ServerLevel pLevel, EntityShip entityShip) {
        if (!chestOpened) return;
        BlockEntity be = pLevel.getBlockEntity(this.target);
        if (pLevel.getBlockState(this.target).is(Blocks.CHEST)) {
            if (be instanceof ChestBlockEntity chestBlockEntity) {
                entityShip.getPlayerOwner().ifPresent(chestBlockEntity::stopOpen);
            }
        }
        this.chestOpened = false;
    }

    @Override
    protected void tick(ServerLevel pLevel, EntityShip pOwner, long pGameTime) {
        pOwner.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.target));
        pOwner.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(this.target, pOwner.getRunSpeedModifier(), 1));
        if (pOwner.blockPosition().distManhattan(this.target) > 2) {
            return;
        }
        BlockEntity be = pLevel.getBlockEntity(this.target);
        if (be != null) {
            extractAmmoFromContainer(pOwner, be);
        }
    }

    public static void extractAmmoFromContainer(EntityShip pOwner, BlockEntity be) {
        be.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(itemHandler -> {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack originStack = itemHandler.getStackInSlot(i);
                if (originStack.is(ModItem.AMMO.get())) {
                    if (pOwner.hasRoomForAmmo(1)) {
                        itemHandler.extractItem(i, 1, false);
                        pOwner.addAmmo(1);
                        return;
                    }
                }
            }
            }
        );
    }

    private void openChest(ServerLevel pLevel, EntityShip entityShip) {
        if (chestOpened) return;
        BlockEntity be = pLevel.getBlockEntity(this.target);
        if (pLevel.getBlockState(this.target).is(Blocks.CHEST)) {
            if (be instanceof ChestBlockEntity chestBlockEntity) {
                entityShip.getPlayerOwner().ifPresent(chestBlockEntity::startOpen);
            }
        }
        this.chestOpened = true;
    }


    @Override
    protected boolean canStillUse(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        return stillHasAmmoInContainer(pLevel, this.target) && !pEntity.ammoIsFull();
    }

    private boolean stillHasAmmoInContainer(ServerLevel pLevel, BlockPos blockPos) {
        if (pLevel.getBlockState(blockPos).isAir()) return false;
        BlockEntity be = pLevel.getBlockEntity(blockPos);
        if (be == null || be.isRemoved()) return false;
        if (be.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
            IItemHandler itemHandler = be.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get();
            return ContainerWithAmmoSensor.hasAmmoInContainer(itemHandler);
        }

         return false;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, EntityShip pOwner) {
        if (pOwner.getBrain().getMemory(ModMemoryModuleType.NEARBY_CHEST_WITH_AMMO.get()).isPresent()) {
            return !pOwner.getBrain().getMemory(ModMemoryModuleType.NEARBY_CHEST_WITH_AMMO.get()).get().isEmpty();
        }
        return false;
    }

}
