package com.github.icecheesecat.kantaicraft.menu.shipyard;

import com.github.icecheesecat.kantaicraft.block.shipyard.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.shipyard.ShipyardSpawnEntityPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class BuiltShipButton extends AbstractButton {
    final ShipyardBlockEntity be;
    private final ResourceLocation background;
    final int index;
    public BuiltShipButton(int index, int pX, int pY, int pWidth, int pHeight, Component pMessage, ShipyardBlockEntity be, ResourceLocation background) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.be = be;
        this.index = index;
        this.background = background;
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
        if (be.isProcessDone(index)) {
            pGuiGraphics.blit(background, this.getX(), this.getY(), 100, 176, 0, 16, 16, 256, 256);
            if (isHovered) {
                pGuiGraphics.fill(this.getX(), this.getY(), this.getX()+this.getWidth(), this.getY()+this.getHeight(), FastColor.ARGB32.color(50, 0, 0, 0));
            }
        }
    }
}
