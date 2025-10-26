package com.github.icecheesecat.kantaicraft.menu.ship;

import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentResourceLocation;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.TogglePlayerShipPacket;
import com.github.icecheesecat.kantaicraft.network.packet.SyncType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class SelectionWidget extends AbstractWidget {

    private static final int width = 100;
    private static final int height = 32;
    private final int imageStartX;
    private final int imageStartY;
    private final int nameStartX;
    private final int textCenterY;
    private static final int WHITE = FastColor.ARGB32.color(255, 0, 0, 0);
    private static final int BACKGROUND_COLOR = FastColor.ARGB32.color(255, 0, 166, 199);

    Equipment equipment;
    EntityShip entityShip;
    int index;

    public SelectionWidget(int pX, int pY, Equipment equipment, EntityShip entityShip, int index) {
        super(pX, pY, width, height, Component.empty());
        this.imageStartX = this.getX() + 10;
        this.imageStartY = this.getY();
        this.nameStartX = this.getX() + 20;
        this.textCenterY = this.getY() + height / 2;
        this.equipment = equipment;
        this.entityShip = entityShip;
        this.index = index;
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        this.renderEquipmentIcon(pGuiGraphics);
        this.renderEquipmentName(pGuiGraphics);
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        ModPacketHandler.INSTANCE.sendToServer(new TogglePlayerShipPacket(SyncType.EQUIPMENT, this.entityShip.getId(), this.equipment, (byte) this.index));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    protected void renderEquipmentIcon(GuiGraphics guiGraphics) {
        ResourceLocation rl = EquipmentResourceLocation.getResourceById(this.equipment.getId());
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
