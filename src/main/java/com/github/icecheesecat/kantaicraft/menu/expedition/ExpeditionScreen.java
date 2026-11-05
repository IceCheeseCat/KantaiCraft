package com.github.icecheesecat.kantaicraft.menu.expedition;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.capability.kantaidata.PlayerKantaiDataCapability;
import com.github.icecheesecat.kantaicraft.expedition.ExpeditionDefinition;
import com.github.icecheesecat.kantaicraft.menu.Refreshable;
import com.github.icecheesecat.kantaicraft.playerkantaidata.PlayerExpeditionData;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvents;
import com.lowdragmc.lowdraglib2.gui.ui.style.StylesheetManager;
import com.lowdragmc.lowdraglib2.math.Size;
import dev.vfyjxf.taffy.style.FlexDirection;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Optional;

public class ExpeditionScreen extends AbstractContainerScreen<ExpeditionMenu> implements Refreshable {

    private static final int PANEL_WIDTH = 370;
    private static final int PANEL_HEIGHT = 220;
    private static final int PANEL_MARGIN = 6;
    private static final int PANEL_GAP = 6;
    private static final int BUTTON_HEIGHT = 20;
    private static final int CATALOG_WEIGHT = 3;
    private static final int DETAILS_WEIGHT = 2;
    private static final float KEY_SCROLL_STEP = 0.1f;
    private static final String CATALOG_ID = "expedition_catalog";
    private static final String DETAILS_ID = "expedition_details";
    private static final String STATUS_ID = "expedition_status";
    private static final String CATALOG_SELECTOR = "#" + CATALOG_ID;
    private static final String DETAILS_SELECTOR = "#" + DETAILS_ID;
    private static final String STATUS_SELECTOR = "#" + STATUS_ID;
    private static final String PANEL_CLASS = "expedition_panel";
    private static final String TEXT_CLASS = "expedition_text";
    private static final String LIST_KEY = "expedition_screen.list";
    private static final String DETAILS_KEY = "expedition_screen.detail_panel";
    private static final String STATUS_ONLY_KEY = "expedition_screen.status_only";
    private static final ResourceLocation STYLESHEET = new ResourceLocation(KantaiCraft.MODID, "lss/expedition.lss");

    private ModularUI m_modularUi;
    private ExpeditionDefinition m_selectedExpedition;

    public ExpeditionScreen(ExpeditionMenu p_menu, Inventory p_inventory, Component p_title) {
        super(p_menu, p_inventory, p_title);
        setModularUi(createModularUi());
    }

    public ModularUI getModularUi() {
        return m_modularUi;
    }

    public void setModularUi(ModularUI p_modularUi) {
        m_modularUi = p_modularUi;
    }

    public ExpeditionDefinition getSelectedExpedition() {
        return m_selectedExpedition;
    }

    public void setSelectedExpedition(ExpeditionDefinition p_expedition) {
        m_selectedExpedition = p_expedition;
        updateSelection();
    }

    private ModularUI createModularUi() {
        ExpeditionScrollableList catalog = new ExpeditionScrollableList(getMenu().getExpeditions(),
                this::setSelectedExpedition);
        ExpeditionDetailPanel details = new ExpeditionDetailPanel();
        ExpeditionToggleButton status = new ExpeditionToggleButton();
        UIElement root = new UIElement();
        UIElement body = new UIElement();
        UIElement detailsColumn = new UIElement();

        catalog.setId(CATALOG_ID).layout(p_layout -> p_layout.flex(CATALOG_WEIGHT).minWidth(0));
        details.setId(DETAILS_ID).layout(p_layout -> p_layout.flex(1).minHeight(0));
        status.setId(STATUS_ID).layout(p_layout -> p_layout.height(BUTTON_HEIGHT).flexShrink(0));
        addFocusNarration(catalog, Component.translatable(LIST_KEY));
        addFocusNarration(details, Component.translatable(DETAILS_KEY));
        status.addEventListener(UIEvents.FOCUS, p_event -> status.announceState());
        detailsColumn.layout(p_layout -> p_layout.flex(DETAILS_WEIGHT).minWidth(0).gapAll(PANEL_GAP))
                .addChildren(details, status);
        body.layout(p_layout -> p_layout.flex(1).minHeight(0).gapAll(PANEL_GAP).flexDirection(FlexDirection.ROW))
                .addChildren(catalog, detailsColumn);
        root.addClass(PANEL_CLASS).layout(p_layout -> p_layout.paddingAll(PANEL_MARGIN).gapAll(PANEL_GAP))
                .addChildren(new Label().setText(getTitle()).addClass(TEXT_CLASS), body,
                        new Label().setText(Component.translatable(STATUS_ONLY_KEY)).addClass(TEXT_CLASS));
        return new ModularUI(UI.of(root,
                List.of(StylesheetManager.INSTANCE.getStylesheet(StylesheetManager.MC),
                        StylesheetManager.INSTANCE.getStylesheet(STYLESHEET)), ExpeditionScreen::fitPanel));
    }

    private static Size fitPanel(Size p_screenSize) {
        return Size.of(Math.min(PANEL_WIDTH, p_screenSize.getWidth() - PANEL_MARGIN * 2),
                Math.min(PANEL_HEIGHT, p_screenSize.getHeight() - PANEL_MARGIN * 2));
    }

    @Override
    protected void init() {
        getModularUi().setScreenAndInit(this);
        imageWidth = (int) getModularUi().getWidth();
        imageHeight = (int) getModularUi().getHeight();
        super.init();
        addRenderableWidget(getModularUi().getWidget());
        setFocused(getModularUi().getWidget());
        updateSelection();
    }

    private void updateSelection() {
        getModularUi().select(CATALOG_SELECTOR, ExpeditionScrollableList.class)
                .forEach(p_catalog -> p_catalog.setSelectedExpedition(getSelectedExpedition()));
        getModularUi().select(DETAILS_SELECTOR, ExpeditionDetailPanel.class)
                .forEach(p_details -> p_details.setExpeditionDefinition(getSelectedExpedition()));
        refresh();
    }

    @Override
    public void refresh() {
        ExpeditionState state = ExpeditionState.resolve(getSelectedExpedition(),
                getPlayerExpeditionData().orElse(null));

        getModularUi().select(STATUS_SELECTOR, ExpeditionToggleButton.class).forEach(p_button -> {
            p_button.setExpeditionState(state);
            if (!p_button.isActive() && p_button.isFocused()) {
                getModularUi().getElementById(CATALOG_ID).focus();
            }
        });
    }

    private Optional<PlayerExpeditionData> getPlayerExpeditionData() {
        if (minecraft == null || minecraft.player == null) return Optional.empty();

        return minecraft.player.getCapability(PlayerKantaiDataCapability.TOKEN).resolve()
                .map(p_data -> p_data.getPlayerExpeditionData());
    }

    @Override
    public boolean keyPressed(int p_keyCode, int p_scanCode, int p_modifiers) {
        if (p_keyCode == GLFW.GLFW_KEY_TAB) {
            moveFocus();
            return true;
        }

        UIElement focusedElement = getModularUi().getFocusedElement();
        boolean isScrollKey = p_keyCode == GLFW.GLFW_KEY_UP || p_keyCode == GLFW.GLFW_KEY_DOWN;

        if (focusedElement instanceof ExpeditionScrollableList && isScrollKey) {
            selectAdjacentExpedition(p_keyCode == GLFW.GLFW_KEY_DOWN ? 1 : -1);
            return true;
        }
        if (focusedElement instanceof ExpeditionDetailPanel details && isScrollKey) {
            float direction = p_keyCode == GLFW.GLFW_KEY_DOWN ? KEY_SCROLL_STEP : -KEY_SCROLL_STEP;

            details.verticalScroller(p_scroller -> p_scroller.scrollValue(direction));
            return true;
        }
        if (focusedElement instanceof ExpeditionToggleButton status && isActivationKey(p_keyCode)) {
            if (status.isActive()) status.announceState();
            return true;
        }
        return super.keyPressed(p_keyCode, p_scanCode, p_modifiers);
    }

    private void moveFocus() {
        List<UIElement> targets = getModularUi().getAllElements().stream()
                .filter(p_element -> p_element.isFocusable() && p_element.isActive() && p_element.isDisplayed())
                .toList();
        int currentIndex = targets.indexOf(getModularUi().getFocusedElement());
        int direction = hasShiftDown() ? -1 : 1;
        int nextIndex = currentIndex < 0 ? (hasShiftDown() ? targets.size() - 1 : 0)
                : Math.floorMod(currentIndex + direction, targets.size());

        targets.get(nextIndex).focus();
    }

    private void selectAdjacentExpedition(int p_direction) {
        List<ExpeditionDefinition> expeditions = getMenu().getExpeditions();

        if (expeditions.isEmpty()) return;

        int currentIndex = getSelectedExpedition() == null ? -1 : expeditions.indexOf(getSelectedExpedition());
        int nextIndex = currentIndex < 0 ? 0 : Mth.clamp(currentIndex + p_direction, 0, expeditions.size() - 1);
        ExpeditionDefinition selected = expeditions.get(nextIndex);

        setSelectedExpedition(selected);
        getModularUi().select(CATALOG_SELECTOR, ExpeditionScrollableList.class)
                .forEach(p_catalog -> p_catalog.revealSelection(selected));
        minecraft.getNarrator().sayNow(Component.translatable(selected.getNameKey()));
    }

    private static boolean isActivationKey(int p_keyCode) {
        return p_keyCode == GLFW.GLFW_KEY_ENTER || p_keyCode == GLFW.GLFW_KEY_KP_ENTER
                || p_keyCode == GLFW.GLFW_KEY_SPACE;
    }

    private void addFocusNarration(UIElement p_element, Component p_text) {
        p_element.addEventListener(UIEvents.FOCUS, p_event -> minecraft.getNarrator().sayNow(p_text));
    }

    @Override
    protected void renderBg(GuiGraphics p_guiGraphics, float p_partialTick, int p_mouseX, int p_mouseY) {
    }

    @Override
    protected void renderLabels(GuiGraphics p_guiGraphics, int p_mouseX, int p_mouseY) {
    }

    @Override
    public void render(GuiGraphics p_guiGraphics, int p_mouseX, int p_mouseY, float p_partialTick) {
        renderBackground(p_guiGraphics);
        super.render(p_guiGraphics, p_mouseX, p_mouseY, p_partialTick);
    }
}
