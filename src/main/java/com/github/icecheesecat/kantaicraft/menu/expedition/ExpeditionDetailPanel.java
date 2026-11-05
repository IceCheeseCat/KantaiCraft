package com.github.icecheesecat.kantaicraft.menu.expedition;

import com.github.icecheesecat.kantaicraft.expedition.ExpeditionDefinition;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.data.TextWrap;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ScrollerView;
import net.minecraft.network.chat.Component;

public class ExpeditionDetailPanel extends ScrollerView {

    private static final String NAME_ID = "expedition_name";
    private static final String OVERVIEW_ID = "expedition_overview";
    private static final String SECTION_CLASS = "expedition_section";
    private static final String TEXT_CLASS = "expedition_text";
    private static final String NAME_KEY = "expedition_screen.name";
    private static final String OVERVIEW_KEY = "expedition_screen.overview";
    private static final String EMPTY_NAME_KEY = "menu.kantaicraft.expedition";
    private static final String EMPTY_OVERVIEW_KEY = "expedition_screen.select_expedition";
    private static final int SECTION_GAP = 6;

    public ExpeditionDetailPanel() {
        setFocusable(true);
        viewContainer(p_view -> p_view.layout(p_layout -> p_layout.gapAll(SECTION_GAP)));
        addScrollViewChildren(
                new Label().setText(Component.translatable(NAME_KEY)).addClass(SECTION_CLASS),
                createParagraph(NAME_ID),
                new Label().setText(Component.translatable(OVERVIEW_KEY)).addClass(SECTION_CLASS),
                createParagraph(OVERVIEW_ID));
        setExpeditionDefinition(null);
    }

    public void setExpeditionDefinition(ExpeditionDefinition p_definition) {
        String nameKey = p_definition == null ? EMPTY_NAME_KEY : p_definition.getNameKey();
        String overviewKey = p_definition == null ? EMPTY_OVERVIEW_KEY : p_definition.getDescriptionKey();

        selectId(NAME_ID, Label.class).forEach(p_label -> p_label.setText(Component.translatable(nameKey)));
        selectId(OVERVIEW_ID, Label.class).forEach(p_label -> p_label.setText(Component.translatable(overviewKey)));
        verticalScroller(p_scroller -> p_scroller.setNormalizedValue(0));
    }

    private static UIElement createParagraph(String p_id) {
        return new Label().textStyle(p_style -> p_style.textWrap(TextWrap.WRAP).adaptiveHeight(true))
                .setId(p_id).addClass(TEXT_CLASS)
                .layout(p_layout -> p_layout.widthPercent(100).flexShrink(0));
    }
}
