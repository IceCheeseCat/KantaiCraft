package com.github.icecheesecat.kantaicraft.block;

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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.Queue;

public class ShipyardBlockEntity extends BlockEntity implements MenuProvider {

    NonNullList<BuildSlot> buildSlots;
    Queue<BuiltShip> builtShips;

    public ShipyardBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlock.SHIPYARD_BETYPE.get(), pPos, pBlockState);
        this.buildSlots = NonNullList.withSize(4, new BuildSlot());
        this.builtShips = new LinkedList<>();
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T be) {
        if (level.isClientSide) return;
        if (be instanceof  ShipyardBlockEntity shipyardBlockEntity) {
            for (var slot: shipyardBlockEntity.buildSlots) {
                slot.doProgress();
                if (slot.done()) {
                    var bpStack = slot.getBlueprint();
                    CompoundTag nbt = (CompoundTag) bpStack.getTag().get("ship_data");
                    var data = ShipBlueprintData.read(nbt);
                    // add to finish section
                    shipyardBlockEntity.builtShips.add(new BuiltShip(data));

                    slot.removeBlueprint();
                }
            }
        }
    }

    public int getSlotSize() {
        return this.buildSlots.size();
    }

    public BuildSlot getBuildSlot(int i) {
        return this.buildSlots.get(i);
    }

    public NonNullList<BuildSlot> getBuildSlots() {
        return this.buildSlots;
    }


    @Override
    public Component getDisplayName() {
        return Component.translatable("shipyard_menu");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ShipyardMenu(pContainerId, pPlayerInventory, buildSlots);
    }
}
