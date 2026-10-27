package com.github.icecheesecat.kantaicraft.menu.expedition;

import com.github.icecheesecat.kantaicraft.menu.pagescreen.Page;
import com.github.icecheesecat.kantaicraft.menu.pagescreen.PageScreen;
import com.github.icecheesecat.kantaicraft.util.RenderingColor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ExpeditionScreen extends PageScreen<ExpeditionMenu> {

    private static final int PANEL_WIDTH = 176;
    private static final int PANEL_HEIGHT = 100;
    private static final int BORDER_WIDTH = 1;

    public ExpeditionScreen(ExpeditionMenu p_menu, Inventory p_inventory, Component p_title) {
        super(p_menu, p_inventory, p_title);
        imageWidth = PANEL_WIDTH;
        imageHeight = PANEL_HEIGHT;
    }

    @Override
    protected void initVar() {

    }

    protected Page createExpeditionPage() {
        Page page = new Page(Component.translatable("expedition_screen.expedition"), this.leftPos + 16, this.topPos + 14, 224, 120);


        return page;
    }

    @Override
    protected void renderBg(GuiGraphics p_guiGraphics, float p_partialTick, int p_mouseX, int p_mouseY) {
        p_guiGraphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight,
                RenderingColor.GUI_BORDER);
        p_guiGraphics.fill(leftPos + BORDER_WIDTH, topPos + BORDER_WIDTH,
                leftPos + imageWidth - BORDER_WIDTH, topPos + imageHeight - BORDER_WIDTH,
                RenderingColor.GUI_BACKGROUND);
    }

    @Override
    public void render(GuiGraphics p_guiGraphics, int p_mouseX, int p_mouseY, float p_partialTick) {
        renderBackground(p_guiGraphics);
        super.render(p_guiGraphics, p_mouseX, p_mouseY, p_partialTick);
    }
}
