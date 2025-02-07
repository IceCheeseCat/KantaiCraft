package com.github.icecheesecat.kantaicraft.menu.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

import java.util.ArrayList;
import java.util.List;

public class ScreenSection {

    public final List<AbstractWidget> widgets = new ArrayList<>();
    private static final int BACKGROUND = FastColor.ARGB32.color(102, 0, 0, 0);
    private boolean isShow;
    private final Component title;
    private int x, y, width, height;

    public ScreenSection(Component title, int x, int y, int width, int height) {
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setShow(boolean h) {
        this.isShow = h;
        for (var w: widgets) {
            w.active = w.visible = h;
        }
    }

    public boolean isShowing() {
        return isShow;
    }

    public void addWidget(AbstractWidget widget) {
        this.widgets.add(widget);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!isShow) return;
        guiGraphics.fill(x, y, x+width, y+height, BACKGROUND);
    }

}
