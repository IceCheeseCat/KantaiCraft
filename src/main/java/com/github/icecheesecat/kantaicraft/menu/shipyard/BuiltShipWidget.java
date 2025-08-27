package com.github.icecheesecat.kantaicraft.menu.shipyard;

import com.github.icecheesecat.kantaicraft.block.shipyard.ShipyardBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

public class BuiltShipWidget extends AbstractWidget {

    boolean removed = false;
    ShipyardBlockEntity shipyardBlockEntity;
    public BuiltShipWidget(int pX, int pY, ShipyardBlockEntity shipyardBlockEntity) {
        super(pX, pY, 40, 20, Component.empty());
        this.shipyardBlockEntity = shipyardBlockEntity;

    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        int stringColor = FastColor.ARGB32.color(255, 0, 0, 0);
        int color = FastColor.ARGB32.color(255, 248, 234, 213);
        pGuiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), color);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);

        this.removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }
}
