package com.github.icecheesecat.kantaicraft.menu.ship;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SectionManager implements Renderable {
    int index = 0;
    int maxIndex = 0;
    private List<ScreenSection> allSections = new ArrayList<>();
    private ScreenSection displayedSection;

    public SectionManager() {
    }

    public void addSection(ScreenSection section) {
        if (this.allSections.contains(section)) return;
        section.setShow(false);
        this.allSections.add(section);
        this.maxIndex++;
    }

    public void addSection(int i, ScreenSection section) {
        if (this.allSections.contains(section)) return;
        section.setShow(false);
        this.allSections.add(i, section);
        this.maxIndex++;
    }

    public void addMainSection(ScreenSection section) {
        if (this.allSections.contains(section)) return;
        section.setShow(true);
        this.allSections.add(0, section);
        this.displayedSection = section;
        this.maxIndex++;
    }

    public void nextSection() {
        index = nextIndex();
        this.allSections.forEach(screenSection -> screenSection.setShow(false));
        this.displayedSection = this.allSections.get(index);
        this.displayedSection.setShow(true);
    }
    public void prevSection() {
        index = prevIndex();
        this.allSections.forEach(screenSection -> screenSection.setShow(false));
        this.displayedSection = this.allSections.get(index);
        this.displayedSection.setShow(true);
    }

    private int nextIndex() {
        return (index + 1) % maxIndex;
    }

    private int prevIndex() {
        return  (index - 1 + maxIndex) % maxIndex;
    }

    public void check() {
        if (this.displayedSection == null) {
            throw new IllegalStateException("Must has at least one section on display");
        }
        if (this.allSections.size() == 0) {
            throw new IllegalStateException("Must has at least one section");
        }
    }

    public Component getCurrentSection() {
        return this.displayedSection.getTitle();
    }

    public Component getNextSection() {
        return this.allSections.get(nextIndex()).getTitle();
    }
    public Component getPrevSection() {
        return this.allSections.get(prevIndex()).getTitle();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.displayedSection.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    public void clear() {
        this.allSections.clear();
        this.maxIndex = 0;
        this.index = 0;
    }

}
