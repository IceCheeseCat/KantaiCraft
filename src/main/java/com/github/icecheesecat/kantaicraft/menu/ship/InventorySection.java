package com.github.icecheesecat.kantaicraft.menu.ship;

import com.github.icecheesecat.kantaicraft.menu.ToggleSlot;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;

import java.util.List;

public class InventorySection extends ScreenSection {
    List<Slot> slots;
    public InventorySection(Component title, int x, int y, int width, int height, List<Slot> slots) {
        super(title, x, y, width, height);
        this.slots = slots;
    }

    @Override
    public void setShow(boolean h) {
        super.setShow(h);
        this.slots.forEach(slot -> {
            if (slot instanceof ToggleSlot toggleSlot) {
                toggleSlot.setActive(h);
            }
        });
    }
}
