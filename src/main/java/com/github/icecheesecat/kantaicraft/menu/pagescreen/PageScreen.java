package com.github.icecheesecat.kantaicraft.menu.pagescreen;

import com.github.icecheesecat.kantaicraft.menu.commandcenter.CustomScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class PageScreen<T extends AbstractContainerMenu> extends CustomScreen<T> {

    final PageManager pageManager = new PageManager();

    public PageScreen(T pMenu, Inventory pPlayerInventory, Component title) {
        super(pMenu, pPlayerInventory, title);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.pageManager.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    public PageManager getPageManager() {
        return this.pageManager;
    }

    @Override
    protected void init() {
        super.init();
        this.pageManager.clear();
    }

}
