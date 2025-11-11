package com.github.icecheesecat.kantaicraft.menu.pagescreen;

import com.github.icecheesecat.kantaicraft.menu.HoveredCreator;
import com.github.icecheesecat.kantaicraft.menu.HoveringPage;
import com.github.icecheesecat.kantaicraft.menu.commandcenter.CustomScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class PageScreen<T extends AbstractContainerMenu> extends CustomScreen<T> {

    int index = 0;
    int maxIndex = 0;
    private final List<Supplier<Page>> pageSuppliers = new ArrayList<>();
    private Page currentPage;
    private final List<Component> pageTitle = new ArrayList<>();
    private Map<HoveredCreator<?>, HoveringPage> activeHoveredCreators = new HashMap<>();

    public PageScreen(T pMenu, Inventory pPlayerInventory, Component title) {
        super(pMenu, pPlayerInventory, title);
    }

    public void addPage(Supplier<Page> pageSupplier) {
        this.addPage(maxIndex, pageSupplier);
    }

    public void addPage(int i, Supplier<Page> pageSupplier) {
        this.pageSuppliers.add(i, pageSupplier);
        this.pageTitle.add(i, pageSupplier.get().getTitle());
        this.maxIndex++;
    }

    public void gotoNextPage() {
        if (index == nextIndex()) return;
        this.clear();
        index = nextIndex();
        this.init();
    }
    public void gotoPrevPage() {
        if (index == prevIndex()) return;
        this.clear();
        index = prevIndex();
        this.init();
    }

    public void jumpToPage(int index) {
        if (index < 0 || index >= maxIndex) return;
        this.clear();
        this.index = index;
        this.init();
    }

    private void clearPageAndWidgetsOnScreen(Page page) {
        this.removeWidget(page);
        page.getPageWidgets().forEach(this::removeWidget);
        this.activeHoveredCreators.values().forEach(this::removeWidget);
    }

    private int nextIndex() {
        return (index + 1) % maxIndex;
    }

    private int prevIndex() {
        return  (index - 1 + maxIndex) % maxIndex;
    }

    public Component getCurrentPageTitle() {
        return this.pageTitle.get(index);
    }

    public Component getNextPageTitle() {
        return this.pageTitle.get(nextIndex());
    }
    public Component getPrevPageTitle() {
        return this.pageTitle.get(prevIndex());
    }

    public Page getCurrentPage() {
        return this.currentPage;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
//        this.getCurrentPage().render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        if (this.currentPage == null) return;
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        // handle hovered creators
        this.getCurrentPage().getPageWidgets().forEach(widget -> {
            if (widget instanceof HoveredCreator<?> hoveredCreator) {
                if (hoveredCreator.hoveredLongEnough() && !hoveredCreator.hasCreated()) {
                    HoveringPage nPage = hoveredCreator.createPage();
                    this.activeHoveredCreators.put(hoveredCreator, nPage);
                    this.addRenderableWidget(nPage);
                    hoveredCreator.setCreated(true);
                }
            }
        });
    }

    public void clear() {
//        this.tempIndex = index;
        this.activeHoveredCreators.values().forEach(this::clearPageAndWidgetsOnScreen);
        if (currentPage != null)
            this.clearPageAndWidgetsOnScreen(this.getCurrentPage());
    }

//    int tempIndex = 0;
//    private void loadBeforeInitIndex() {
//        if (tempIndex < 0 || tempIndex >= maxIndex) {
//            this.index = 0;
//        }
//        else {
//            this.index = tempIndex;
//        }
//    }

    @Override
    protected void init() {
        super.init();
        this.clear();
        this.initCurrentPage();
    }

    private void initCurrentPage() {
        this.createPage();
        this.addPageAndWidgetsOnScreen(this.getCurrentPage());
        Map<HoveredCreator<?>, HoveringPage> n_hoveredMap = new HashMap<>();
        this.activeHoveredCreators.forEach((hoveredCreator, page) -> {
            n_hoveredMap.put(hoveredCreator, hoveredCreator.createPage());
        });
        this.activeHoveredCreators = n_hoveredMap;
        this.activeHoveredCreators.values().forEach(this::addRenderableWidget);
    }

    private void createPage() {
        this.currentPage = this.pageSuppliers.get(index).get();
    }

    private void addPageAndWidgetsOnScreen(Page page) {
        this.addRenderableWidget(page);
        page.getPageWidgets().forEach(this::addRenderableWidget);
    }


    public void refresh() {
        this.init();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        for (var child: children()) {
            if (child.mouseClicked(pMouseX, pMouseY, pButton)) {
                // handle close hoveringPage
                this.activeHoveredCreators.entrySet().removeIf((entry) -> {
                    if (entry.getValue().isClosed()) {
                        this.removeWidget(entry.getValue());
                        entry.getKey().setCreated(false);
                        return true;
                    }
                    return false;
                });
                return true;
            }
        }

        return super.mouseClicked(pMouseX,pMouseY,pButton);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        for (var child: children()) {
            child.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        }

        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }
}
