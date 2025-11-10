package com.github.icecheesecat.kantaicraft.menu;

import com.github.icecheesecat.kantaicraft.menu.pagescreen.Page;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;

/**
 * Draggable Page
 * Right click to close
 */
public abstract class HoveringPage extends Page {
    boolean closed = false;
    public HoveringPage(Component title, int x, int y, int width, int height) {
        super(title, x, y, width, height);
    }

    public boolean isClosed() {
        return closed;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == InputConstants.MOUSE_BUTTON_RIGHT) {
            this.closed = true;
            return true;
        }

        boolean b = pButton == InputConstants.MOUSE_BUTTON_LEFT && this.x <= pMouseX && this.x + this.width >= pMouseX && this.y <= pMouseY && this.y + this.height >= pMouseY;
        this.setFocused(b);
        return b;
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.isFocused() && this.mouseClicked(pMouseX, pMouseY, pButton)) {
            this.x += pDragX;
            this.y += pDragY;
            return true;
        }
        return false;
    }
}
