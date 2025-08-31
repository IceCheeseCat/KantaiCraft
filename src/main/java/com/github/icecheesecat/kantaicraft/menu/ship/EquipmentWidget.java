package com.github.icecheesecat.kantaicraft.menu.ship;

import com.github.icecheesecat.kantaicraft.capability.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.capability.EquipmentHandlerCapability;
import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentResourceLocation;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.C2SEquipmentOptionsPacket;
import com.github.icecheesecat.kantaicraft.network.packet.TogglePlayerShipPacket;
import com.github.icecheesecat.kantaicraft.network.packet.SyncType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class EquipmentWidget extends AbstractWidget {
    private EntityShip ship;
    private EquipmentHandler equipmentHandler;
    private int index;
    private static final int WHITE = FastColor.ARGB32.color(255, 0, 0, 0);
    private static final int BACKGROUND_COLOR = FastColor.ARGB32.color(200, 175, 154, 39);
    public static final int sizeX = 32;
    public static final int sizeY = 32;
    private static final int width = 200;
    private static final int height = 32;
    private final int imageStartX;
    private final int imageStartY;
    private final int nameStartX;
    private final int textCenterY;
    private final int levelTextStartX;
    private static final int CHILD_OFFSET_X = 120;
    private static final int CHILD_OFFSET_Y = height;
    public WidgetState state;

    public EquipmentWidget(int pX, int pY, EntityShip ship, int index) {
        super(pX, pY, width, height, Component.empty());
        this.imageStartX = this.getX() + 30;
        this.imageStartY = this.getY();
        this.nameStartX = this.getX() + 60;
        this.levelTextStartX = this.getX() + 10;
        this.textCenterY = this.getY() + height / 2;
        this.ship = ship;
        this.index = index;
        ship.getCapability(EquipmentHandlerCapability.TOKEN).ifPresent(
                handler -> this.equipmentHandler = handler
        );

        this.evaluateState();
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
    public void onClick(double pMouseX, double pMouseY) {
        switch (this.state) {
            case EMPTY_EQUIPMENT, UPGRADE_EQUIPMENT -> {
                ModPacketHandler.INSTANCE.sendToServer(new C2SEquipmentOptionsPacket(this.ship.getId(), this.index, this.equipmentHandler.getEquipment(index).getId()));
            }
            case LEVEL_UP_EQUIPMENT -> {
                ModPacketHandler.INSTANCE.sendToServer(new TogglePlayerShipPacket(SyncType.LEVEL_UP_EQUIPMENT, this.ship.getId(), 1, (byte) this.index));
            }
        }
    }


    protected void renderEquipmentLevel(GuiGraphics guiGraphics) {
        if (this.equipmentHandler.getEquipment(index).getId() < 0) {
            return;
        }
        int equipmentLevel = this.equipmentHandler.getEquipment(index).getLevel();

        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(equipmentLevel), levelTextStartX, textCenterY, WHITE);
    }

    protected void renderEquipmentIcon(GuiGraphics guiGraphics) {
        ResourceLocation rl = EquipmentResourceLocation.getResourceById(this.equipmentHandler.getEquipment(index).getId());
        if (rl == null) return;

        guiGraphics.blit(rl,
                this.imageStartX, this.imageStartY,
                0, 0,
                32, 32,
                32, 32
                );
    }

    protected void renderEquipmentName(GuiGraphics guiGraphics) {
        guiGraphics.drawString(Minecraft.getInstance().font, this.equipmentHandler.getEquipment(index).getName(), this.getX() + nameStartX, textCenterY, WHITE);
    }

    protected void renderBackground(GuiGraphics guiGraphics) {
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), BACKGROUND_COLOR);
    }

    public void evaluateState() {
        if (this.equipmentHandler.getEquipment(index).getId() == -1) {
            this.state = WidgetState.EMPTY_EQUIPMENT;
        }
        else if (this.equipmentHandler.getEquipment(index).getLevel() == Equipment.MAX_LEVEL) {
            this.state = WidgetState.UPGRADE_EQUIPMENT;
        }
        else {
            this.state = WidgetState.LEVEL_UP_EQUIPMENT;
        }
    }

    public enum WidgetState {
        EMPTY_EQUIPMENT,
        LEVEL_UP_EQUIPMENT,
        UPGRADE_EQUIPMENT,
        DO_NOTHING
    }
}
