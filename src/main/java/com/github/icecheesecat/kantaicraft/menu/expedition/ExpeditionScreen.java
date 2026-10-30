package com.github.icecheesecat.kantaicraft.menu.expedition;

import com.github.icecheesecat.kantaicraft.menu.pagescreen.Page;
import com.github.icecheesecat.kantaicraft.menu.pagescreen.PageScreen;
import com.github.icecheesecat.kantaicraft.util.RenderingColor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.glfw.GLFW;

public class ExpeditionScreen extends PageScreen<ExpeditionMenu> {

    private static final int DETAIL_PANEL_WIDTH = 125;
    private static final int PANEL_HEIGHT = 200;
    private static final int PANEL_MARGIN = 6;
    private static final int BORDER_WIDTH = 1;
    private static final int EXPEDITION_ROW_WIDTH = 200;
    private static final int PANEL_GAP = 6;
    private static final int PANEL_WIDTH = DETAIL_PANEL_WIDTH + EXPEDITION_ROW_WIDTH
            + ExpeditionScrollableList.SCROLLBAR_WIDTH + PANEL_GAP + PANEL_MARGIN * 2;

    public ExpeditionScreen(ExpeditionMenu p_menu, Inventory p_inventory, Component p_title) {
        super(p_menu, p_inventory, p_title);
        imageWidth = PANEL_WIDTH;
        imageHeight = PANEL_HEIGHT;
        this.addPage(this::createExpeditionPage);
    }

    @Override
    protected void initVar() {
        imageWidth = Math.min(PANEL_WIDTH, width - PANEL_MARGIN * 2);
        imageHeight = Math.min(PANEL_HEIGHT, height - PANEL_MARGIN * 2);
        leftPos = (width - imageWidth) / 2;
        topPos = (height - imageHeight) / 2;
    }

    protected Page createExpeditionPage() {
        int panelX = leftPos + PANEL_MARGIN;
        int panelY = topPos + PANEL_MARGIN;
        int panelWidth = imageWidth - PANEL_MARGIN * 2;
        int panelHeight = imageHeight - PANEL_MARGIN * 2;
        int listWidth = panelWidth - DETAIL_PANEL_WIDTH - PANEL_GAP - ExpeditionScrollableList.SCROLLBAR_WIDTH;
        int detailX = panelX + listWidth + ExpeditionScrollableList.SCROLLBAR_WIDTH + PANEL_GAP;
        Page page = new Page(Component.translatable("expedition_screen.expedition"),
                panelX, panelY, panelWidth, panelHeight);
        ExpeditionDetailPanel detailPanel = new ExpeditionDetailPanel(detailX, panelY, DETAIL_PANEL_WIDTH, panelHeight);
        ExpeditionScrollableList expeditionList = new ExpeditionScrollableList(
                new ScreenRectangle(panelX, panelY, listWidth, panelHeight));

        detailPanel.setExpeditionName(Component.translatable("menu.kantaicraft.expedition"));
        detailPanel.setParagraph(Component.translatable("expedition_screen.select_expedition"));
        expeditionList.setExpeditions(getMenu().getExpeditions());
        expeditionList.setSelectionListener(p_expedition -> {
            detailPanel.setExpeditionName(Component.translatable(p_expedition.getNameKey()));
            detailPanel.setParagraph(Component.translatable(p_expedition.getDescriptionKey()));
        });
        page.addWidget(expeditionList);
        page.addWidget(detailPanel);

        return page;
    }

    @Override
    public boolean mouseClicked(double p_mouseX, double p_mouseY, int p_button) {
        boolean isHandled = super.mouseClicked(p_mouseX, p_mouseY, p_button);

        if (isHandled) {
            // PageScreen dispatches clicks directly, so establish focus for dragging and keyboard input here.
            getChildAt(p_mouseX, p_mouseY).ifPresent(this::setFocused);
            if (p_button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                setDragging(true);
            }
        }
        return isHandled;
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
