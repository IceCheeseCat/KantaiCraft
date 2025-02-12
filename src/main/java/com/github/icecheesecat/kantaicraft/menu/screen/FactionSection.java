package com.github.icecheesecat.kantaicraft.menu.screen;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class FactionSection extends ScreenSection {

    private ResourceLocation icon;
    private static final int ICON_SIZE = 32;
    private int ICON_X;
    private int ICON_Y;
    private int FONT_X;
    private int FONT_Y;
    private int FONT_COLOR = FastColor.ARGB32.color(255, 255, 255, 255);
    private final BasicEntityShip ship;

    public FactionSection(Component title, int x, int y, int width, int height, BasicEntityShip ship, ResourceLocation icon) {
        super(title, x, y, width, height);
        this.icon = icon;
        this.ICON_X = this.x + 20;
        this.ICON_Y = this.y + 20;
        this.FONT_X = ICON_X + ICON_SIZE / 2;
        this.FONT_Y = ICON_Y + ICON_SIZE + 20;
        this.ship = ship;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if (!this.isShowing()) return;
        guiGraphics.blit(icon, ICON_X, ICON_Y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, "Faction Id: " + ship.getFactionId(), FONT_X, FONT_Y, FONT_COLOR);
    }
}
