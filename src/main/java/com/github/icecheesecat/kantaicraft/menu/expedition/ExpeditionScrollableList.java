package com.github.icecheesecat.kantaicraft.menu.expedition;

import com.github.icecheesecat.kantaicraft.expedition.ExpeditionDefinition;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.data.Horizontal;
import com.lowdragmc.lowdraglib2.gui.ui.data.TextWrap;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Button;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ScrollerView;
import dev.vfyjxf.taffy.style.FlexDirection;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;

public class ExpeditionScrollableList extends ScrollerView {

    private static final int ROW_HEIGHT = 48;
    private static final int ROW_PADDING = 5;
    private static final int ROW_GAP = 3;
    private static final long TICKS_PER_SECOND = 20L;
    private static final String ROW_CLASS = "expedition_row";
    private static final String ROW_SELECTOR = "." + ROW_CLASS;
    private static final String SELECTED_CLASS = "expedition_selected";
    private static final String TEXT_CLASS = "expedition_text";
    private static final String EMPTY_KEY = "expedition_screen.empty";
    private static final String DURATION_KEY = "expedition_screen.duration";

    public ExpeditionScrollableList(List<ExpeditionDefinition> p_expeditions,
                                   Consumer<ExpeditionDefinition> p_onSelect) {
        setFocusable(true);
        viewContainer(p_view -> p_view.layout(p_layout -> p_layout.gapAll(ROW_GAP)));
        if (p_expeditions.isEmpty()) {
            addScrollViewChild(new Label().setText(Component.translatable(EMPTY_KEY)).addClass(TEXT_CLASS));
        }
        p_expeditions.forEach(p_expedition -> addScrollViewChild(createRow(p_expedition, p_onSelect)));
    }

    public void setSelectedExpedition(ExpeditionDefinition p_definition) {
        select(ROW_SELECTOR).forEach(p_row -> {
            if (p_definition != null && p_row.getId().equals(p_definition.getId().toString())) {
                p_row.addClass(SELECTED_CLASS);
            } else {
                p_row.removeClass(SELECTED_CLASS);
            }
        });
    }

    public void revealSelection(ExpeditionDefinition p_definition) {
        selectId(p_definition.getId().toString()).findFirst().ifPresent(this::revealRow);
    }

    private void revealRow(UIElement p_row) {
        viewPort(p_port -> verticalScroller(p_scroller -> {
            float viewportHeight = p_port.getContentHeight();
            float scrollRange = Math.max(0, getContainerHeight() - viewportHeight);

            if (scrollRange == 0) return;

            float scrollTop = p_scroller.getNormalizedValue() * scrollRange;
            float rowTop = p_row.getPositionY() - p_port.getContentY() + scrollTop;
            float rowBottom = rowTop + p_row.getSizeHeight();
            float nextTop = rowTop < scrollTop ? rowTop
                    : rowBottom > scrollTop + viewportHeight ? rowBottom - viewportHeight : scrollTop;

            p_scroller.setNormalizedValue(nextTop / scrollRange);
        }));
    }

    private static Button createRow(ExpeditionDefinition p_definition, Consumer<ExpeditionDefinition> p_onSelect) {
        Button row = new Button().noText().setOnClick(p_event -> p_onSelect.accept(p_definition));

        row.setId(p_definition.getId().toString());
        row.addClass(ROW_CLASS);
        row.layout(p_layout -> p_layout.height(ROW_HEIGHT).flexShrink(0)
                .paddingAll(ROW_PADDING).gapAll(ROW_GAP).flexDirection(FlexDirection.COLUMN));
        row.addChildren(
                new Label().setText(Component.translatable(p_definition.getNameKey()))
                        .textStyle(p_style -> p_style.textAlignHorizontal(Horizontal.LEFT)
                                .textWrap(TextWrap.HOVER_ROLL))
                        .setOverflowVisible(false).addClass(TEXT_CLASS),
                new Label().setText(Component.translatable(DURATION_KEY,
                        p_definition.getDurationTicks() / TICKS_PER_SECOND)).addClass(TEXT_CLASS));
        return row;
    }
}
