package com.github.icecheesecat.kantaicraft.menu.ship;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class SectionButton extends CustomTextureButton {

    protected final SectionManager sectionManager;

    public SectionButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, ResourceLocation texture, SectionManager sectionManager) {
        super(pX, pY, pWidth, pHeight, pMessage, texture);
        this.sectionManager = sectionManager;
    }
    public SectionButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, ResourceLocation texture, ResourceLocation hoveredTexture, SectionManager sectionManager) {
        super(pX, pY, pWidth, pHeight, pMessage, texture, hoveredTexture);
        this.sectionManager = sectionManager;
    }

}
