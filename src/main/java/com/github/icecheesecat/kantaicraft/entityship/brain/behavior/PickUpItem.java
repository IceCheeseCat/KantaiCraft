package com.github.icecheesecat.kantaicraft.entityship.brain.behavior;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.entityship.DropIndicationPacket;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

/**
 * {@link EntityShip} behaviour to pick up Mob drops they killed.
 */

public class PickUpItem extends Behavior<EntityShip> {

    private static final int TIMEOUT = 200;
    private static final int PAUSE_COOLDOWN = 100;
    private ItemEntity itemEntity;
    private WalkTarget walkTarget;
    private boolean isRemoved;
    private boolean stoppedByFight;
    private boolean isPickedUp;

    public PickUpItem() {
        super(
                ImmutableMap.of(ModMemoryModuleType.NEAREST_WANTED_ITEM.get(), MemoryStatus.VALUE_PRESENT,
                        ModMemoryModuleType.ITEMS_TO_PICK_UP.get(), MemoryStatus.REGISTERED,
                        MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT,
                        MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED,
                        MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED,
                        MemoryModuleType.PATH, MemoryStatus.REGISTERED,
                        ModMemoryModuleType.PICK_UP_COOLDOWN.get(), MemoryStatus.VALUE_ABSENT),
                TIMEOUT);
    }

    @Override
    protected void start(@NotNull ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        System.out.println("pick up behaviour");
        this.isRemoved = this.stoppedByFight = this.isPickedUp = false;
        this.itemEntity = pEntity.getBrain().getMemory(ModMemoryModuleType.NEAREST_WANTED_ITEM.get()).get();
    }

    @Override
    protected void tick(@NotNull ServerLevel pLevel, @NotNull EntityShip pOwner, long pGameTime) {
        if (this.itemEntity == null || this.itemEntity.isRemoved()) {
            this.isRemoved = true;
            return;
        }

        double dis = pOwner.distanceTo(itemEntity);
        if (dis < 1.5d) {
            pOwner.shipPickUpItem(this.itemEntity);
            this.isPickedUp = true;
        }

        pOwner.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(itemEntity.blockPosition(), 1.0f, 0));
    }



    /**
     * When behaviour stopped
     * 1. if successfully picked up item, remove from memory
     * 2. if timeout remove from memory
     * 3. if was paused from fight, set cooldown memory for behaviour
     */
    @Override
    protected void stop(@NotNull ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        if (this.timedOut(pGameTime)) {
            indicateItemDropToClient(this.itemEntity, pEntity);
            removeItemFromMemory(this.itemEntity, pEntity);
        }
        this.itemEntity = null;
    }

    private void indicateItemDropToClient(ItemEntity itemEntity, EntityShip pEntity) {
        pEntity.getPlayerOwner().ifPresent(player -> {
            ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new DropIndicationPacket(itemEntity.getId()));
        });
    }

    private void removeItemFromMemory(ItemEntity itemEntity, EntityShip entityShip) {
        entityShip.getBrain().getMemory(ModMemoryModuleType.ITEMS_TO_PICK_UP.get()).ifPresent(itemEntities -> itemEntities.remove(itemEntity));
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel pLevel, @NotNull EntityShip pEntity, long pGameTime) {
        if (pEntity.getBrain().checkMemory(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)) {
            stoppedByFight = true;
            return false;
        }

        return !this.isRemoved && this.itemEntity != null && !this.itemEntity.isRemoved();
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel pLevel, EntityShip pOwner) {
        return pOwner.hasInventory();
    }

}
