package com.github.icecheesecat.kantaicraft.block;

import com.github.icecheesecat.kantaicraft.block.shipyardUtil.BlueprintCell;
import com.github.icecheesecat.kantaicraft.block.shipyardUtil.BuiltShipCell;
import com.github.icecheesecat.kantaicraft.item.ShipBlueprintData;
import com.github.icecheesecat.kantaicraft.menu.shipyard.ShipyardMenu;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ShipyardBlockEntity extends BlockEntity implements MenuProvider {

    NonNullList<BlueprintCell> blueprintCells;
    List<BuiltShipCell> builtShipCells;

    public ShipyardBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlock.SHIPYARD_BETYPE.get(), pPos, pBlockState);
        this.blueprintCells = NonNullList.createWithCapacity(4);
        for (int i = 0; i < 4; i++) {
            this.blueprintCells.add(i, new BlueprintCell());
        }

        this.builtShipCells = new ArrayList<>();
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T be) {
        if (level.isClientSide) return;
        if (be instanceof  ShipyardBlockEntity shipyardBlockEntity) {
            for (var cell: shipyardBlockEntity.blueprintCells) {
                if (cell.tick()) {
                    if (cell.done()) {
                        // add to built slots
                        shipyardBlockEntity.builtShipCells.add(new BuiltShipCell(cell.getData(), cell.getBuilder()));
                        cell.removeBlueprint(true);
                    }
                }
            }
        }
    }



    public int getSlotSize() {
        return this.blueprintCells.size();
    }

    public BlueprintCell getBuildSlot(int i) {
        return this.blueprintCells.get(i);
    }

    public NonNullList<BlueprintCell> getBuildSlots() {
        return this.blueprintCells;
    }


    @Override
    public Component getDisplayName() {
        return Component.translatable("shipyard_menu");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ShipyardMenu(pContainerId, pPlayerInventory, blueprintCells, builtShipCells, ContainerLevelAccess.create(pPlayer.level(), this.getBlockPos()));
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("blueprintslots.size", blueprintCells.size());
        for (int i = 0; i < blueprintCells.size(); i++) {
            pTag.put("blueprintslot" + i, blueprintCells.get(i).serializeNBT());
        }

        pTag.putInt("builtslots.size", builtShipCells.size());
        for (int i = 0; i < builtShipCells.size(); i++) {
            pTag.put("builtslot" + i, builtShipCells.get(i).serializeNBT());
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        int slotSize0 = pTag.getInt("blueprintslots.size");
        this.blueprintCells = NonNullList.withSize(slotSize0, new BlueprintCell());
        for (int i = 0; i < slotSize0; i++) {
            this.blueprintCells.get(i).deserializeNBT(pTag.getCompound("blueprintslot" + i));
        }

        int slotSize1 = pTag.getInt("builtslots.size");
        for (int i = 0; i < slotSize1; i++) {
            this.builtShipCells.add(i, new BuiltShipCell(ShipBlueprintData.empty(), null));
            this.builtShipCells.get(i).deserializeNBT(pTag.getCompound("builtslot" + i));
        }
    }

}
