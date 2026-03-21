package com.github.icecheesecat.kantaicraft.menu.shipyard;

import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.shipyard.SetShipyardSlotOwnerPacket;
import com.github.icecheesecat.kantaicraft.registries.ModItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BlueprintSlot extends Slot {

    final BlockPos blockPos;
    final int processIndex;
    public BlueprintSlot(Container pContainer, int pSlot, int processIndex, int pX, int pY, BlockPos blockPos) {
        super(pContainer, pSlot, pX, pY);
        this.blockPos = blockPos;
        this.processIndex = processIndex;
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return pStack.is(ModItem.SHIP_BLUEPRINT.get()) && pStack.hasTag();
    }

    @Override
    public void setByPlayer(ItemStack pStack) {
        super.setByPlayer(pStack);
        if (!pStack.isEmpty()) {
            ModPacketHandler.INSTANCE.sendToServer(new SetShipyardSlotOwnerPacket(this.processIndex, this.blockPos));
        }
    }
}
