package com.github.icecheesecat.kantaicraft.block;

import com.github.icecheesecat.kantaicraft.block.basic.ComponentBlockEntity;
import com.github.icecheesecat.kantaicraft.block.basic.IComponentDrops;
import com.github.icecheesecat.kantaicraft.block.shipyardUtil.BuiltData;
import com.github.icecheesecat.kantaicraft.block.shipyardUtil.ShipBlueprintStackHandler;
import com.github.icecheesecat.kantaicraft.capability.ShipBlueprintCapability;
import com.github.icecheesecat.kantaicraft.menu.shipyard.ShipyardMenu;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.ShipyardBuiltDataPacket;
import com.github.icecheesecat.kantaicraft.network.packet.ShipyardPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShipyardBlockEntity extends ComponentBlockEntity implements MenuProvider, IComponentDrops {

    public final int processShipSize;
    int[] processTime;
    int[] totalProcessTime;
    List<BuiltData> builtData = new ArrayList<>();

    public ShipyardBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(pPos, pBlockState);
        this.processShipSize = 4;
        this.processTime = new int[this.processShipSize];
        this.totalProcessTime = new int[this.processShipSize];
        for (int i = 0; i < processShipSize; i++) {
            this.processTime[i] = this.totalProcessTime[i] = -1;
        }
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T be) {
        if (level.isClientSide) return;
        if (be instanceof  ShipyardBlockEntity shipyardBlockEntity) {
            shipyardBlockEntity.tickAllProcesses();

            // changed in item stack handler
            shipyardBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(
                itemHandler -> {
                    itemChangedInBlueprintStackHandler(itemHandler, shipyardBlockEntity);
                }
            );

            // sync processes
            for (int i = 0; i < shipyardBlockEntity.processShipSize; i++) {
                if (shipyardBlockEntity.totalProcessTime[i] == -1) continue;
                ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new ShipyardPacket(shipyardBlockEntity.getBlockPos(), (byte)i, shipyardBlockEntity.processTime[i]));
            }

            if (level.getGameTime() % 20 == 0) {
                shipyardBlockEntity.setChanged();
            }
        }
    }

    private static void itemChangedInBlueprintStackHandler(IItemHandler itemHandler, ShipyardBlockEntity shipyardBlockEntity) {
        if (itemHandler instanceof ShipBlueprintStackHandler stackHandler) {

            int dirtyIndex = stackHandler.getDirty();
            if (dirtyIndex == -1) { // no changed found
                return;
            }

            ItemStack itemStack = stackHandler.getStackInSlot(dirtyIndex);
            if (itemStack.isEmpty()) {
                shipyardBlockEntity.resetProcess(dirtyIndex);
            }
            else {
                itemStack.getCapability(ShipBlueprintCapability.TOKEN).ifPresent(
                        data -> {
                            shipyardBlockEntity.setProcessTime((byte) dirtyIndex, 0);
                            shipyardBlockEntity.setTotalProcessTime((byte) dirtyIndex, data.getProcessTime());
                        }
                );
            }

            ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new ShipyardPacket(shipyardBlockEntity.getBlockPos(), (byte)dirtyIndex, shipyardBlockEntity.processTime[dirtyIndex], shipyardBlockEntity.totalProcessTime[dirtyIndex], true));
            shipyardBlockEntity.setChanged();
        }
    }

    private void tickAllProcesses() {
        for (int i = 0; i < processShipSize; i++) {
            if (hasProcess(i)) {
                this.processTime[i]++;
                if (this.processTime[i] >= this.totalProcessTime[i]) {
                    this.addBuiltData(i);
                    this.removeProcessItem(i);
                    this.setChanged();
                }

                System.out.println(i + " " +processTime[i]);
            }
        }
    }

    private void resetProcess(int i) {
        this.processTime[i] = this.totalProcessTime[i] = -1;
    }

    private void removeProcessItem(int i) {
        this.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(
                itemHandler -> {
                    if (itemHandler instanceof ShipBlueprintStackHandler stackHandler) {
                        stackHandler.extractItem(i, 1, false);
                    }
                }
        );
    }

    private void addBuiltData(int i) {
        this.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(
                itemHandler -> {
                    if (itemHandler instanceof ShipBlueprintStackHandler stackHandler) {
                        ItemStack stack = stackHandler.getStackInSlot(i);
                        stack.getCapability(ShipBlueprintCapability.TOKEN).ifPresent(
                                data -> this.builtData.add(new BuiltData(data))
                        );
                    }
                }
        );

        ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new ShipyardBuiltDataPacket(this.getBlockPos(), this.builtData));
    }

    public int hasBuildDataAt(UUID uuid) {
        for (int i = 0; i < this.builtData.size(); i++) {
            if (this.getBuiltData().get(i).getUuid().compareTo(uuid) == 0) {
                return i;
            }
        }

        return -1;
    }

    public List<BuiltData> getBuiltData() {
        return builtData;
    }

    public void setProcessTime(byte index, int processTime) {
        this.processTime[index] = processTime;
    }

    public void setTotalProcessTime(byte index, int totalProcessTime) {
        this.totalProcessTime[index] = totalProcessTime;
    }

    public int getRemainTime(int i) {
        return this.totalProcessTime[i] - this.processTime[i];
    }

    public boolean hasProcess(int i) {
        return this.processTime[i] != -1 && this.totalProcessTime[i] != -1;
    }

    public int getProcessShipSize() {
        return this.processShipSize;
    }

    public void clientSetBuiltData(List<BuiltData> builtData) {
        this.builtData = builtData;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("shipyard_menu");
    }

    /**
     * Sync both processes and built data before client open this block entity's menu
     */
    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        for (int i = 0; i < this.processShipSize; i++) {
            ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new ShipyardPacket(this.getBlockPos(), (byte)i, this.processTime[i], this.totalProcessTime[i], true));
        }
        ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new ShipyardBuiltDataPacket(this.getBlockPos(), this.builtData));
        return new ShipyardMenu(pContainerId, pPlayerInventory, this, ContainerLevelAccess.create(pPlayer.level(), this.getBlockPos()));
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
       super.saveAdditional(nbt);
        nbt.putIntArray("process_time", this.processTime);
        nbt.putIntArray("total_process_time", this.totalProcessTime);
        nbt.putInt("built_data_size", this.builtData.size());
        for (int i = 0; i < builtData.size(); i++) {
            nbt.put("built_data" + i, this.builtData.get(i).serializeNBT());
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.processTime = nbt.getIntArray("process_time");
        this.totalProcessTime = nbt.getIntArray("total_process_time");
        int size = nbt.getInt("built_data_size");
        for (int i = 0; i < size; i++) {
            this.builtData.add(i, BuiltData.read(nbt.getCompound("built_data" + i)));
        }
    }

    @Override
    public void dropAllWhenPatternDestryed() {
        double x = this.getBlockPos().getCenter().x;
        double y = this.getBlockPos().getCenter().y;
        double z = this.getBlockPos().getCenter().z;

        // drop itemHandler
        this.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(
                itemHandler -> {

                    for (int i = 0; i < itemHandler.getSlots(); i++) {
                        ItemStack itemStack = itemHandler.getStackInSlot(i);
                        if (itemStack.isEmpty()) continue;
                        ItemEntity itemEntity = new ItemEntity(level, x, y, z, itemStack);
                        level.addFreshEntity(itemEntity);
                    }
                }
        );

        // drop built data
        List<BuiltData> builtData = this.getBuiltData();
        for (var data: builtData) {
            ItemStack itemStack = data.getData().createItemStack();
            ItemEntity itemEntity = new ItemEntity(level, x, y, z, itemStack);
            level.addFreshEntity(itemEntity);
        }
    }

}
