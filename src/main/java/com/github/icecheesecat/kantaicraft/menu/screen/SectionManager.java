package com.github.icecheesecat.kantaicraft.menu.screen;

import java.util.HashMap;
import java.util.Map;

public class SectionManager {

    private Map<SectionSelector, ScreenSection> allSections = new HashMap<>();

    public void addSection(SectionSelector selector, ScreenSection section) {
        this.allSections.put(selector, section);
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

}
