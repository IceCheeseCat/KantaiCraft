package com.github.icecheesecat.kantaicraft.menu.pagescreen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PageManager implements Renderable {
    int index = 0;
    int maxIndex = 0;
    private List<Page> allPages = new ArrayList<>();
    private List<Runnable> setupOnDisplay = new ArrayList<>();
//    private Page displayedPage;

    public PageManager() {
    }

    public void addPage(int i, Page page, Runnable runnable, boolean displayed) {
        if (this.allPages.contains(page)) return;
        page.setShow(displayed);
        this.allPages.add(i, page);
        this.setupOnDisplay.add(i, runnable);
        this.maxIndex++;
    }

    public void addPage(Page page) {
        this.addPage(this.maxIndex, page, null, false);
    }

    public void addPage(int i, Page page) {
        this.addPage(i, page, null, false);
    }

    public void addPage(Page page, Runnable runnable) {
        this.addPage(maxIndex, page, runnable, false);
    }

    public void addPage(Page page, Runnable runnable, boolean diplayed) {
        this.addPage(maxIndex, page, runnable, diplayed);
    }

    public void addDisplayingPage(Page page, Runnable runnable) {
        this.addPage(page, runnable, true);
    }

    public void addDisplayingPage(Page page) {
        this.addDisplayingPage(page, null);
    }

    public void nextPage() {
        index = nextIndex();
        actuallyTurnPage();
    }
    public void prevPage() {
        index = prevIndex();
        actuallyTurnPage();
    }

    public void jumpToPage(int index) {
        if (index < 0 || index >= maxIndex) return;
        this.index = index;
        actuallyTurnPage();
    }

    private void actuallyTurnPage() {
        this.allPages.forEach(screenPage -> screenPage.setShow(false));
        this.allPages.get(index).setShow(true);
        if (this.setupOnDisplay.get(index) != null) {
            this.setupOnDisplay.get(index).run();
        }
    }

    private int nextIndex() {
        return (index + 1) % maxIndex;
    }

    private int prevIndex() {
        return  (index - 1 + maxIndex) % maxIndex;
    }

    public void check() {
        if (this.allPages.get(this.index) == null) {
            throw new IllegalStateException("Must has at least one page on display");
        }
        if (this.allPages.size() == 0) {
            throw new IllegalStateException("Must has at least one page");
        }
    }

    public Component getCurrentPageTitle() {
        return this.allPages.get(index).getTitle();
    }

    public Component getNextPageTitle() {
        return this.allPages.get(nextIndex()).getTitle();
    }
    public Component getPrevPageTitle() {
        return this.allPages.get(prevIndex()).getTitle();
    }

    public Page getCurrentPage() {
        return this.allPages.get(index);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.getCurrentPage().render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    public void clear() {
        this.allPages.clear();
        this.maxIndex = 0;
        this.index = 0;
    }

    public void replaceCurrentPage(Page page) {
        this.allPages.set(this.index, page);
    }

    public void refresh() {
        this.getCurrentPage().refresh();
    }

    public void init() {
        this.getCurrentPage().init();
    }

}
