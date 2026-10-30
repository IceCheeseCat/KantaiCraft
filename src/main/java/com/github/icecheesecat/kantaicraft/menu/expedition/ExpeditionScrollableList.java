package com.github.icecheesecat.kantaicraft.menu.expedition;

import net.minecraft.client.gui.components.AbstractScrollWidget;
import com.github.icecheesecat.kantaicraft.expedition.ExpeditionDefinition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.function.Consumer;

public class ExpeditionScrollableList extends AbstractScrollWidget {

    public static final int SCROLLBAR_WIDTH = 8;
    private static final int ROW_HEIGHT = 50;
    private static final int ROW_GAP = 2;
    private static final int TEXT_PADDING = 6;
    private static final int NO_SELECTION = -1;
    private static final long TICKS_PER_SECOND = 20L;
    private static final int BACKGROUND_COLOR = 0xFFF3EBDD;
    private static final int ROW_COLOR = 0xFFE8DFCD;
    private static final int HOVER_COLOR = 0xFFD5EAE4;
    private static final int SELECTED_COLOR = 0xFFAFE2DF;
    private static final int BORDER_COLOR = 0xFFC9B98A;
    private static final int SELECTED_BORDER_COLOR = 0xFF247D85;
    private static final int TEXT_COLOR = 0xFF4E4B40;
    private static final String LIST_KEY = "expedition_screen.list";
    private static final String EMPTY_KEY = "expedition_screen.empty";
    private static final String DURATION_KEY = "expedition_screen.duration";

    private List<ExpeditionDefinition> m_expeditions = List.of();
    private int m_selectedIndex = NO_SELECTION;
    private Consumer<ExpeditionDefinition> m_selectionListener = p_expedition -> {};

    public ExpeditionScrollableList(ScreenRectangle p_bounds) {
        super(p_bounds.left(), p_bounds.top(), p_bounds.width(), p_bounds.height(), Component.translatable(LIST_KEY));
    }

    public List<ExpeditionDefinition> getExpeditions() {
        return m_expeditions;
    }

    public void setExpeditions(List<ExpeditionDefinition> p_expeditions) {
        m_expeditions = List.copyOf(p_expeditions);
        m_selectedIndex = NO_SELECTION;
        setScrollAmount(0);
    }

    public int getSelectedIndex() {
        return m_selectedIndex;
    }

    public void setSelectedIndex(int p_selectedIndex) {
        if (p_selectedIndex == NO_SELECTION) {
            m_selectedIndex = NO_SELECTION;
            return;
        }

        ExpeditionDefinition expedition = getExpeditions().get(p_selectedIndex);

        m_selectedIndex = p_selectedIndex;
        revealSelection();
        getSelectionListener().accept(expedition);
    }

    public Consumer<ExpeditionDefinition> getSelectionListener() {
        return m_selectionListener;
    }

    public void setSelectionListener(Consumer<ExpeditionDefinition> p_selectionListener) {
        m_selectionListener = p_selectionListener;
    }

    @Override
    protected int getInnerHeight() {
        return getExpeditions().size() * ROW_HEIGHT;
    }

    @Override
    protected double scrollRate() {
        return ROW_HEIGHT;
    }

    @Override
    protected void renderBackground(GuiGraphics p_guiGraphics) {
        p_guiGraphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), BACKGROUND_COLOR);
        p_guiGraphics.renderOutline(getX(), getY(), getWidth(), getHeight(), BORDER_COLOR);
    }

    @Override
    protected void renderContents(GuiGraphics p_guiGraphics, int p_mouseX, int p_mouseY, float p_partialTick) {
        if (getExpeditions().isEmpty()) {
            p_guiGraphics.drawWordWrap(Minecraft.getInstance().font, Component.translatable(EMPTY_KEY),
                    getX() + innerPadding(), getY() + innerPadding(), getWidth() - totalInnerPadding(), TEXT_COLOR);
            return;
        }

        int hoveredIndex = withinContentAreaPoint(p_mouseX, p_mouseY) ? getRowIndex(p_mouseY) : NO_SELECTION;

        for (int index = 0; index < getExpeditions().size(); index++) {
            int rowY = getY() + innerPadding() + index * ROW_HEIGHT;

            if (withinContentAreaTopBottom(rowY, rowY + ROW_HEIGHT)) {
                renderRow(p_guiGraphics, index, index == hoveredIndex);
            }
        }
    }

    private void renderRow(GuiGraphics p_guiGraphics, int p_index, boolean p_isHovered) {
        Font font = Minecraft.getInstance().font;
        ExpeditionDefinition expedition = getExpeditions().get(p_index);
        int rowX = getX() + innerPadding();
        int rowY = getY() + innerPadding() + p_index * ROW_HEIGHT;
        int rowWidth = getWidth() - totalInnerPadding();
        boolean isSelected = p_index == getSelectedIndex();
        int backgroundColor = isSelected ? SELECTED_COLOR : p_isHovered ? HOVER_COLOR : ROW_COLOR;
        String name = font.plainSubstrByWidth(Component.translatable(expedition.getNameKey()).getString(),
                rowWidth - TEXT_PADDING * 2);
        Component duration = Component.translatable(DURATION_KEY, expedition.getDurationTicks() / TICKS_PER_SECOND);

        p_guiGraphics.fill(rowX, rowY, rowX + rowWidth, rowY + ROW_HEIGHT - ROW_GAP, backgroundColor);
        if (isSelected) {
            p_guiGraphics.renderOutline(rowX, rowY, rowWidth, ROW_HEIGHT - ROW_GAP, SELECTED_BORDER_COLOR);
        }

        p_guiGraphics.drawString(font, name, rowX + TEXT_PADDING, rowY + TEXT_PADDING, TEXT_COLOR, false);
        p_guiGraphics.drawString(font, duration, rowX + TEXT_PADDING,
                rowY + ROW_HEIGHT - ROW_GAP - TEXT_PADDING - font.lineHeight, TEXT_COLOR, false);
    }

    private int getRowIndex(double p_mouseY) {
        return (int) Math.floor((p_mouseY - getY() - innerPadding() + scrollAmount()) / ROW_HEIGHT);
    }

    private void revealSelection() {
        int rowTop = innerPadding() + getSelectedIndex() * ROW_HEIGHT;
        int rowBottom = rowTop + ROW_HEIGHT;

        if (rowTop < scrollAmount()) {
            setScrollAmount(rowTop);
        } else if (rowBottom > scrollAmount() + getHeight() - innerPadding()) {
            setScrollAmount(rowBottom - getHeight() + innerPadding());
        }
    }

    @Override
    public boolean mouseClicked(double p_mouseX, double p_mouseY, int p_button) {
        if (!active || p_button != GLFW.GLFW_MOUSE_BUTTON_LEFT
                || !super.mouseClicked(p_mouseX, p_mouseY, p_button)) {
            return false;
        }

        int index = getRowIndex(p_mouseY);

        if (withinContentAreaPoint(p_mouseX, p_mouseY) && index >= 0 && index < getExpeditions().size()) {
            setSelectedIndex(index);
        }
        return true;
    }

    @Override
    public boolean isMouseOver(double p_mouseX, double p_mouseY) {
        int scrollbarWidth = scrollbarVisible() ? SCROLLBAR_WIDTH : 0;

        return visible && p_mouseX >= getX() && p_mouseX < getX() + getWidth() + scrollbarWidth
                && p_mouseY >= getY() && p_mouseY < getY() + getHeight();
    }

    @Override
    public boolean keyPressed(int p_keyCode, int p_scanCode, int p_modifiers) {
        if (getExpeditions().isEmpty() || (p_keyCode != GLFW.GLFW_KEY_UP && p_keyCode != GLFW.GLFW_KEY_DOWN)) {
            return super.keyPressed(p_keyCode, p_scanCode, p_modifiers);
        }

        int direction = p_keyCode == GLFW.GLFW_KEY_DOWN ? 1 : -1;
        int index = getSelectedIndex() == NO_SELECTION ? 0 : getSelectedIndex() + direction;

        setSelectedIndex(Mth.clamp(index, 0, getExpeditions().size() - 1));
        return true;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_narrationElementOutput) {
        Component title = getSelectedIndex() == NO_SELECTION ? getMessage()
                : Component.translatable(getExpeditions().get(getSelectedIndex()).getNameKey());

        p_narrationElementOutput.add(NarratedElementType.TITLE, title);
    }
}
