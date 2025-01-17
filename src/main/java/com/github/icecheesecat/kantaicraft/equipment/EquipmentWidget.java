package com.github.icecheesecat.kantaicraft.equipment;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class EquipmentWidget extends AbstractWidget {
    private BasicEntityShip ship;
    private Equipment equipment;
    private static final int WHITE = FastColor.ARGB32.color(255, 0, 0, 0);
    private static final int BACKGROUND_COLOR = FastColor.ARGB32.color(255, 0, 166, 199);
    public static final int sizeX = 32;
    public static final int sizeY = 32;
    private static final int width = 100;
    private static final int height = 32;
    private final int imageStartX;
    private final int imageStartY;
    private final int nameStartX;
    private final int textCenterY;

    public EquipmentWidget(int pX, int pY, BasicEntityShip ship, Equipment equipment) {
        super(pX, pY, width, height, Component.empty());
        this.imageStartX = this.getX() + 10;
        this.imageStartY = this.getY();
        this.nameStartX = this.getX() + 20;
        this.textCenterY = this.getY() + height / 2;
        this.ship = ship;
        this.equipment = equipment;
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        this.renderEquipmentLevel(pGuiGraphics);
        this.renderEquipmentIcon(pGuiGraphics);
        this.renderEquipmentName(pGuiGraphics);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
        return super.keyReleased(pKeyCode, pScanCode, pModifiers);
    }

    protected void renderEquipmentLevel(GuiGraphics guiGraphics) {
        if (this.equipment.getUid() < 0) {
            return;
        }
        int equipmentLevel = this.equipment.getEquipmentLevel().getLevel();

        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(equipmentLevel), this.getX() + 2, this.getY() + textCenterY, WHITE);
    }

    protected void renderEquipmentIcon(GuiGraphics guiGraphics) {
        ResourceLocation rl = EquipmentResourceLocation.getResourceById(this.equipment.getUid());
        if (rl == null) return;

        guiGraphics.blit(rl,
                this.imageStartX, this.imageStartY,
                0, 0,
                32, 32,
                32, 32
                );
    }

    protected void renderEquipmentName(GuiGraphics guiGraphics) {
        guiGraphics.drawString(Minecraft.getInstance().font, this.equipment.getName(), this.getX() + nameStartX, textCenterY, WHITE);
    }

    protected void renderBackground(GuiGraphics guiGraphics) {
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), BACKGROUND_COLOR);
    }

}
