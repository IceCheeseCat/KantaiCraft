package com.github.icecheesecat.kantaicraft.entityship.brain.sensor;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModItem;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ContainerWithAmmoSensor extends Sensor<EntityShip> {

    public ContainerWithAmmoSensor() {
        super(5);
    }

    @Override
    protected void doTick(ServerLevel pLevel, EntityShip pEntity) {
        if (!pEntity.isPlayerShip()) return;
        findNearbyChestWithAmmo(pLevel, pEntity);
    }

    private void findNearbyChestWithAmmo(ServerLevel pLevel, EntityShip pEntity) {
        List<BlockPos> poses = withinStream(pEntity.blockPosition(), 16, 2, 16);
        List<BlockPos> chestPoses = new ArrayList<>();

        for (var pos: poses) {
            if (pLevel.getBlockEntity(pos) == null) continue;
            if (pLevel.getBlockEntity(pos).getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
                pLevel.getBlockEntity(pos).getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(itemHandler -> {
                    if (hasAmmoInContainer(itemHandler)) {
                        chestPoses.add(pos);
                    }
                });
            }
        }

        var filtered = filterNotOpenableChest(pLevel, chestPoses);
        rememberChest(pEntity, filtered);
    }

    private List<BlockPos> filterNotOpenableChest(ServerLevel serverLevel, List<BlockPos> chestPoses) {
        return chestPoses.stream().filter(blockPos -> {
            if (serverLevel.getBlockState(blockPos).is(Blocks.CHEST)) {
                return !ChestBlock.isChestBlockedAt(serverLevel, blockPos);
            }
            return true;
        }).toList();
    }

    private static List<BlockPos> withinStream(BlockPos center, int sizeX, int sizeY, int sizeZ) {
        BlockPos corner = center.subtract(new Vec3i(sizeX, sizeY, sizeZ));
//        BlockPos corner1 = center.offset(new Vec3i(sizeX, sizeY, sizeZ));

        List<BlockPos> poses = new ArrayList<>();
        for (int i = 0; i < sizeX*2+1; i++) {
            for (int j = 0; j < sizeY*2+1; j++) {
                for (int k = 0; k < sizeZ*2+1; k++) {

                    BlockPos nPos = corner.offset(i, j, k);
                    poses.add(nPos);
                }
            }
        }

        return poses;
    }

    public static boolean hasAmmoInContainer(IItemHandler itemHandler) {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (hasAmmoAtSlot(itemHandler, i)) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasAmmoAtSlot(IItemHandler itemHandler, int slot) {
        return itemHandler.getStackInSlot(slot).is(ModItem.AMMO.get());
    }

    private void rememberChest(EntityShip pEntity, @NotNull List<BlockPos> chestPoses) {
        pEntity.getBrain().setMemory(ModMemoryModuleType.NEARBY_CHEST_WITH_AMMO.get(), chestPoses);
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return Set.of(ModMemoryModuleType.NEED_RESUPPLY.get());
    }
}
