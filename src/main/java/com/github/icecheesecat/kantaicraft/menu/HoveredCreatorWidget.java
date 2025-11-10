package com.github.icecheesecat.kantaicraft.menu;

import com.eliotlash.mclib.math.functions.classic.Abs;
import com.github.icecheesecat.kantaicraft.menu.pagescreen.Page;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

public abstract class HoveredCreatorWidget<T extends HoveringPage> extends AbstractWidget implements HoveredCreator<T> {

    static int WIDGET_ID = 0;
    int id = WIDGET_ID++;
    float hoveredTick = 0.0f;
    boolean hasCreated = false;

    public HoveredCreatorWidget(int pX, int pY, int pWidth, int pHeight, Component pMessage) {
        super(pX, pY, pWidth, pHeight, pMessage);
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (isHovered && !hasCreated) {
            this.hoveredTick += pPartialTick;
            this.renderHovered(pGuiGraphics, pMouseX, pMouseY);
        }
        else {
            this.hoveredTick = 0.0f;
        }

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }
    @Override
    public boolean hoveredLongEnough() {
        return this.hoveredTick >= HOVERING_TICKS;
    }

    @Override
    public void setCreated(boolean b) {
        this.hasCreated = b;
    }

    @Override
    public boolean hasCreated() {
        return this.hasCreated;
    }

    @Override
    public void renderHovered(GuiGraphics guiGraphics, double mouseX, double mouseY) {
        float box_size = 5.0f;
        float percentage = this.hoveredTick / HOVERING_TICKS;
        guiGraphics.fill((int) (mouseX - box_size/2), (int) (mouseY - box_size/2), (int) (mouseX+box_size/2), (int) (mouseY + box_size/2), FastColor.ARGB32.color(128,0,0,0));
        guiGraphics.fill((int) (mouseX - box_size/2), (int) ((mouseY - box_size/2) + box_size * percentage), (int) (mouseX+box_size/2), (int) (mouseY + box_size/2), FastColor.ARGB32.color(255,255,255,255));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HoveredCreatorWidget<?> widget) {
            return this.id == widget.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
