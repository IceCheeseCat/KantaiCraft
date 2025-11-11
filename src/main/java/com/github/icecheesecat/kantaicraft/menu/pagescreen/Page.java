package com.github.icecheesecat.kantaicraft.menu.pagescreen;

import com.github.icecheesecat.kantaicraft.menu.HoveredCreator;
import com.github.icecheesecat.kantaicraft.menu.HoveredCreatorWidget;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Page implements Renderable, GuiEventListener, NarratableEntry {

    private static final int BACKGROUND = FastColor.ARGB32.color(102, 0, 0, 0);
    private final Component title;
    protected int x, y, width, height;
    private final List<ImageDisplay> imageDisplays = new ArrayList<>();
    private List<TextInstance> textInstances = new ArrayList<>();
    private List<AbstractWidget> pageWidgets = new ArrayList<>();

    public Page(Component title, int x, int y, int width, int height) {
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // render background
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0,0, -100);
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.pose().popPose();

        // draw image
        RenderSystem.enableBlend();
        imageDisplays.forEach(imageDisplay -> {
            guiGraphics.setColor(1.0f, 1.0f, 1.0f, imageDisplay.alpha);
            guiGraphics.blit(imageDisplay.resourceLocation, imageDisplay.x, imageDisplay.y,  imageDisplay.blitOffset, 0,  0, imageDisplay.imageWidth, imageDisplay.imageHeight, imageDisplay.textureWidth, imageDisplay.textureHeight);
            guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        });
        RenderSystem.disableBlend();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 100);
        // draw text
        this.textInstances.forEach(instance -> {
            guiGraphics.drawString(Minecraft.getInstance().font, instance.text, instance.x, instance.y, instance.fontColor);
        });
        guiGraphics.pose().popPose();

    }

    protected void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(this.x, this.y, this.x + this.width, this.y, + this.height, BACKGROUND);
    }

    public Component getTitle() {
        return this.title;
    }

    public void addTextInstance(TextInstance textInstance) {
        this.textInstances.add(textInstance);
    }

    public record ImageDisplay(int x, int y, int blitOffset, int imageWidth, int imageHeight, int textureWidth, int textureHeight, ResourceLocation resourceLocation, float alpha) {
        public ImageDisplay(int x, int y, int imageWidth, int imageHeight, ResourceLocation resourceLocation) {
            this(x, y, 0, imageWidth, imageHeight, 256, 256, resourceLocation, 1.0f);
        }
        public ImageDisplay(int x, int y, int imageWidth, int imageHeight, ResourceLocation resourceLocation, float alpha) {
            this(x, y, 0, imageWidth, imageHeight, 256, 256, resourceLocation, alpha);
        }
        public ImageDisplay(int x, int y, int blitOffset, int imageWidth, int imageHeight, ResourceLocation resourceLocation, float alpha) {
            this(x, y, blitOffset, imageWidth, imageHeight, 256, 256, resourceLocation, alpha);
        }
    }

    public void addImageDisplay(ImageDisplay imageDisplay) {
        this.imageDisplays.add(imageDisplay);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Page page) {
            return page.getTitle().getString().equals(this.title.getString());
        }

        return false;
    }

    public void addWidget(AbstractWidget widget) {
        this.pageWidgets.add(widget);
    }

    public void addAllWidget(List<AbstractWidget> widgets) {
        this.pageWidgets.addAll(widgets);
    }

    public List<AbstractWidget> getPageWidgets() {
        return pageWidgets;
    }

    public void clearWidgets() {
        this.pageWidgets.clear();
    }

    public record TextInstance(int x, int y, String text, int fontColor) {
    }

    boolean focused = false;

    @Override
    public void setFocused(boolean pFocused) {
        this.focused = pFocused;
    }

    @Override
    public boolean isFocused() {
        return this.focused;
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

}

