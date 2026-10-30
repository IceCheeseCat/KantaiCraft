package com.github.icecheesecat.kantaicraft.menu.expedition;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ExpeditionDetailPanel extends AbstractWidget {

    private static final int TEXT_PADDING = 6;
    private static final int LABEL_PADDING = 3;
    private static final int SECTION_SPACING = 3;
    private static final int BORDER_WIDTH = 1;
    private static final float NAME_SCALE = 1.2F;
    private static final int BACKGROUND_COLOR = 0xFFF3EBDD;
    private static final int BORDER_COLOR = 0xFFC9B98A;
    private static final int DIVIDER_COLOR = 0xFFD9CCB7;
    private static final int TEXT_COLOR = 0xFF4E4B40;
    private static final int LABEL_TOP_COLOR = 0xFF4ED4DA;
    private static final int LABEL_BOTTOM_COLOR = 0xFF1898A7;
    private static final int LABEL_BORDER_COLOR = 0xFF247D85;
    private static final int LABEL_TEXT_COLOR = 0xFFFFFFFF;
    private static final String DETAIL_PANEL_TRANSLATION_KEY = "expedition_screen.detail_panel";
    private static final Component NAME_LABEL = Component.translatable("expedition_screen.name");
    private static final Component OVERVIEW_LABEL = Component.translatable("expedition_screen.overview");

    private Component m_expeditionName = Component.empty();
    private Component m_paragraph = Component.empty();

    public ExpeditionDetailPanel(int p_x, int p_y, int p_width, int p_height) {
        super(p_x, p_y, p_width, p_height, Component.translatable(DETAIL_PANEL_TRANSLATION_KEY));
    }

    public Component getParagraph() {
        return m_paragraph;
    }

    public void setParagraph(Component p_paragraph) {
        m_paragraph = p_paragraph;
    }

    public Component getExpeditionName() {
        return m_expeditionName;
    }

    public void setExpeditionName(Component p_expeditionName) {
        m_expeditionName = p_expeditionName;
    }

    @Override
    protected void renderWidget(GuiGraphics p_guiGraphics, int p_mouseX, int p_mouseY, float p_partialTick) {
        int textX = getX() + TEXT_PADDING;
        int textY = getY() + TEXT_PADDING;
        int textWidth = getWidth() - TEXT_PADDING * 2;
        int textBottom = getY() + getHeight() - TEXT_PADDING;

        if (textWidth <= 0 || textBottom <= textY) {
            return;
        }

        p_guiGraphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), BACKGROUND_COLOR);
        p_guiGraphics.renderOutline(getX(), getY(), getWidth(), getHeight(), BORDER_COLOR);
        p_guiGraphics.enableScissor(textX, textY, textX + textWidth, textBottom);
        int nameY = renderSectionLabel(p_guiGraphics, NAME_LABEL, textY);
        int dividerY = renderName(p_guiGraphics, nameY, textWidth) + SECTION_SPACING;

        p_guiGraphics.fill(textX, dividerY, textX + textWidth, dividerY + BORDER_WIDTH, DIVIDER_COLOR);
        int overviewY = renderSectionLabel(p_guiGraphics, OVERVIEW_LABEL, dividerY + SECTION_SPACING);

        p_guiGraphics.drawWordWrap(Minecraft.getInstance().font, getParagraph(),
                textX, overviewY, textWidth, TEXT_COLOR);
        p_guiGraphics.disableScissor();
    }

    private int renderSectionLabel(GuiGraphics p_guiGraphics, Component p_label, int p_y) {
        Font font = Minecraft.getInstance().font;
        int labelX = getX() + TEXT_PADDING;
        int labelWidth = font.width(p_label) + LABEL_PADDING * 2;
        int labelHeight = font.lineHeight + LABEL_PADDING * 2;

        p_guiGraphics.fillGradient(labelX, p_y, labelX + labelWidth, p_y + labelHeight,
                LABEL_TOP_COLOR, LABEL_BOTTOM_COLOR);
        p_guiGraphics.renderOutline(labelX, p_y, labelWidth, labelHeight, LABEL_BORDER_COLOR);
        p_guiGraphics.drawString(font, p_label, labelX + LABEL_PADDING, p_y + LABEL_PADDING,
                LABEL_TEXT_COLOR, false);
        return p_y + labelHeight + SECTION_SPACING;
    }

    private int renderName(GuiGraphics p_guiGraphics, int p_y, int p_width) {
        Font font = Minecraft.getInstance().font;
        int nameWidth = Math.max(BORDER_WIDTH, (int) (p_width / NAME_SCALE));

        p_guiGraphics.pose().pushPose();
        p_guiGraphics.pose().translate(getX() + TEXT_PADDING, p_y, 0);
        p_guiGraphics.pose().scale(NAME_SCALE, NAME_SCALE, 1.0F);
        p_guiGraphics.drawWordWrap(font, getExpeditionName(), 0, 0, nameWidth, TEXT_COLOR);
        p_guiGraphics.pose().popPose();
        return p_y + (int) Math.ceil(font.wordWrapHeight(getExpeditionName(), nameWidth) * NAME_SCALE);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_narrationElementOutput) {
        p_narrationElementOutput.add(NarratedElementType.TITLE,
                Component.empty().append(getExpeditionName()).append(CommonComponents.SPACE).append(getParagraph()));
    }
}
