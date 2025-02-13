package com.github.icecheesecat.kantaicraft.block;

import com.github.icecheesecat.kantaicraft.block.shipyardUtil.BlueprintSlot;
import com.github.icecheesecat.kantaicraft.block.shipyardUtil.BuiltShip;
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

import java.util.ArrayList;
import java.util.List;

public class ShipyardBlockEntity extends BlockEntity implements MenuProvider {

    NonNullList<BlueprintSlot> blueprintSlots;
    List<BuiltShip> builtShips;

    public ShipyardBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlock.SHIPYARD_BETYPE.get(), pPos, pBlockState);
        this.blueprintSlots = NonNullList.withSize(4, new BlueprintSlot());
        this.builtShips = new ArrayList<>();
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T be) {
        if (level.isClientSide) return;
        if (be instanceof  ShipyardBlockEntity shipyardBlockEntity) {
            for (var slot: shipyardBlockEntity.blueprintSlots) {
                slot.doProgress();
                if (slot.done()) {
                    var bpStack = slot.getBlueprint();
                    CompoundTag nbt = (CompoundTag) bpStack.getTag().get("ship_data");
                    var data = ShipBlueprintData.read(nbt);
                    // add to finish section
                    shipyardBlockEntity.builtShips.add(new BuiltShip(data));

                    slot.removeBlueprint(true);
                }
            }
        }
    }



    public int getSlotSize() {
        return this.blueprintSlots.size();
    }

    public BlueprintSlot getBuildSlot(int i) {
        return this.blueprintSlots.get(i);
    }

    public NonNullList<BlueprintSlot> getBuildSlots() {
        return this.blueprintSlots;
    }


    @Override
    public Component getDisplayName() {
        return Component.translatable("shipyard_menu");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ShipyardMenu(pContainerId, pPlayerInventory, blueprintSlots, builtShips);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("blueprintslots.size", blueprintSlots.size());
        for (int i = 0; i < blueprintSlots.size(); i++) {
            pTag.put("blueprintslot" + i, blueprintSlots.get(i).serializeNBT());
        }

        pTag.putInt("builtslots.size", builtShips.size());
        for (int i = 0; i < builtShips.size(); i++) {
            pTag.put("builtslot" + i, builtShips.get(i).serializeNBT());
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        int slotSize0 = pTag.getInt("blueprintslots.size");
        this.blueprintSlots = NonNullList.withSize(slotSize0, new BlueprintSlot());
        for (int i = 0; i < slotSize0; i++) {
            this.blueprintSlots.get(i).deserializeNBT(pTag.getCompound("blueprintslot" + i));
        }

        int slotSize1 = pTag.getInt("builtslots.size");
        for (int i = 0; i < slotSize1; i++) {
            this.builtShips.add(i, new BuiltShip(ShipBlueprintData.empty()));
            this.builtShips.get(i).deserializeNBT(pTag.getCompound("builtslot" + i));
        }
    }

}
