package com.github.icecheesecat.kantaicraft.menu.ship;

import com.github.icecheesecat.kantaicraft.menu.IconWithTextElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class GridSection extends ScreenSection {
    List<GridLayout> gridLayout = new ArrayList<>();

    public GridSection(Component title, int x, int y, int width, int height) {
        super(title, x, y, width, height);
        setShow(true);
    }

    @Override
    public void setShow(boolean h) {
        gridLayout.forEach(gridLayout1 -> {
            gridLayout1.visitWidgets(widget -> {
                widget.visible = widget.active = h;
            });
            gridLayout1.visitChildren(le -> {
                if (le instanceof IconWithTextElement iconWithTextElement) {
                    iconWithTextElement.setShow(h);
                }
            });
        });
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    public void appendGridlayout(GridLayout gridLayout) {
        this.gridLayout.add(gridLayout);
    }
}
