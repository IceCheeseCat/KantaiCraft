package com.github.icecheesecat.kantaicraft.menu.pagescreen;

import com.github.icecheesecat.kantaicraft.menu.ship.CustomTextureButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class PageButton extends CustomTextureButton {

    protected final PageManager pageManager;

    public PageButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, ResourceLocation texture, PageManager pageManager) {
        super(pX, pY, pWidth, pHeight, pMessage, texture);
        this.pageManager = pageManager;
    }
    public PageButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, ResourceLocation texture, ResourceLocation hoveredTexture, PageManager pageManager) {
        super(pX, pY, pWidth, pHeight, pMessage, texture, hoveredTexture);
        this.pageManager = pageManager;
    }

    public static class Next extends PageButton {

        public Next(int pX, int pY, int pWidth, int pHeight, Component pMessage, ResourceLocation texture, ResourceLocation hoveredTexture, PageManager pageManager) {
            super(pX, pY, pWidth, pHeight, pMessage, texture, hoveredTexture, pageManager);
        }

        @Override
        public void onPress() {
            pageManager.nextPage();
        }
    }

    public static class Previous extends PageButton {

        public Previous(int pX, int pY, int pWidth, int pHeight, Component pMessage, ResourceLocation texture, ResourceLocation hoveredTexture, PageManager pageManager) {
            super(pX, pY, pWidth, pHeight, pMessage, texture, hoveredTexture, pageManager);
        }

        @Override
        public void onPress() {
            pageManager.prevPage();
        }
    }



}
