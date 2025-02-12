package com.github.icecheesecat.kantaicraft.menu.screen;

import net.minecraft.client.gui.GuiGraphics;

import java.util.HashMap;
import java.util.Map;

public class SectionManager {

    private boolean hasSelected = false;
    private Map<SectionSelector, ScreenSection> allSections = new HashMap<>();

    public void addSection(SectionSelector selector, ScreenSection section) {
        this.allSections.put(selector, section);
        if (!hasSelected) {
            selector.setSelected(true);
            this.hasSelected = true;
        }
        else {
            selector.setSelected(false);
        }
    }

    public void addSectionAndSetSelected(SectionSelector selector, ScreenSection section) {
        this.allSections.forEach((ss, ss2) -> ss.setSelected(false));
        this.allSections.put(selector, section);
        selector.setSelected(true);
    }

    public void controlSections(double pMouseX, double pMouseY, int pButton) {
        allSections.forEach(
                (ss, section) -> {
                    if (ss.mouseClicked(pMouseX, pMouseY, pButton)) {
                        allSections.forEach((ss1, section1) -> {
                            ss1.setSelected(false);
                        });
                        ss.setSelected(true);
                    }
                }
        );
    }

    public void renderAll(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.allSections.values().forEach(screenSection -> screenSection.render(guiGraphics, mouseX, mouseY, partialTick));
    }

}
