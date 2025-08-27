package com.github.icecheesecat.kantaicraft.menu.shipyard;

import com.github.icecheesecat.kantaicraft.block.shipyard.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.ShipyardSpawnEntityPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

public class BuiltShipButton extends AbstractButton {

    final ShipyardBlockEntity be;
    final int index;
    public BuiltShipButton(int index, int pX, int pY, int pWidth, int pHeight, Component pMessage, ShipyardBlockEntity be) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.be = be;
        this.index = index;
    }

    @Override
    public void onPress() {
        ModPacketHandler.INSTANCE.sendToServer(new ShipyardSpawnEntityPacket(be.getBlockPos(), this.index));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
//        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        if (!isHovered) {
            pGuiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, FastColor.ARGB32.color(102, 0, 0, 0));
        }
        else {
            pGuiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, FastColor.ARGB32.color(102, 255, 255, 255));
        }
    }
}
