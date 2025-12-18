package com.github.icecheesecat.kantaicraft.menu.commandcenter;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.menu.Refreshable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class CustomScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements Refreshable {

    public CustomScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    protected void init() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }

    protected final void renderWithOriginalSize(ResourceLocation resourceLocation, GuiGraphics guiGraphics, int imageWidth, int imageHeight) {
        guiGraphics.blit(resourceLocation, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);
    }

    protected final void renderWithScreenSize(ResourceLocation resourceLocation, GuiGraphics guiGraphics) {
        guiGraphics.blit(resourceLocation, 0, 0, -100, 0, 0, Minecraft.getInstance().screen.width, Minecraft.getInstance().screen.height, Minecraft.getInstance().screen.width, Minecraft.getInstance().screen.height);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
//        super.renderLabels(pGuiGraphics, pMouseX, pMouseY);
    }
}
