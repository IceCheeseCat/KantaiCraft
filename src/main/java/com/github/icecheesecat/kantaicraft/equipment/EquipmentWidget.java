package com.github.icecheesecat.kantaicraft.equipment;

import com.github.icecheesecat.kantaicraft.config.ConfigEquipmentData;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.menu.screen.SelectionWidget;
import com.github.icecheesecat.kantaicraft.menu.screen.WidgetState;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.DoEquipmentLevelUpPacket;
import com.github.icecheesecat.kantaicraft.network.packet.SyncShipC2SPacket;
import com.github.icecheesecat.kantaicraft.network.packet.SyncType;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.jline.reader.Widget;

import java.util.ArrayList;
import java.util.List;

public class EquipmentWidget extends AbstractWidget {
    private BasicEntityShip ship;
    private Equipment equipment;
    private int index;
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
    private static final int CHILD_OFFSET_X = 120;
    private static final int CHILD_OFFSET_Y = height;
    public WidgetState state;
    private List<SelectionWidget> childrenWidget = new ArrayList<>();

    public EquipmentWidget(int pX, int pY, BasicEntityShip ship, Equipment equipment, int index) {
        super(pX, pY, width, height, Component.empty());
        this.imageStartX = this.getX() + 10;
        this.imageStartY = this.getY();
        this.nameStartX = this.getX() + 20;
        this.textCenterY = this.getY() + height / 2;
        this.ship = ship;
        this.equipment = equipment;
        this.index = index;
        this.evaluateState();
    }

    public List<SelectionWidget> getChildrenWidget() {
        return childrenWidget;
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        this.renderEquipmentLevel(pGuiGraphics);
        this.renderEquipmentIcon(pGuiGraphics);
        this.renderEquipmentName(pGuiGraphics);
        this.childrenWidget.forEach(widget -> widget.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }



    @Override
    public void onClick(double pMouseX, double pMouseY) {
        switch (this.state) {
            case EMPTY_EQUIPMENT -> {
                this.emptyEquipmentCase();
            }
            case SELECTING -> {
                // handled in mouseClicked method
            }
            case LEVEL_UP_EQUIPMENT -> {
                ModPacketHandler.INSTANCE.sendToServer(new DoEquipmentLevelUpPacket(this.ship.getId(), (byte) this.index));
            }
            case UPGRADE_EQUIPMENT -> {
                this.upgradeEquipmentCase();
            }
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == InputConstants.MOUSE_BUTTON_LEFT && this.state == WidgetState.SELECTING) {
            boolean flag = false;
            for (var widget: childrenWidget) {
                if (widget.mouseClicked(pMouseX, pMouseY, pButton)) {
                    flag = true;
                }
            }

            if (!flag) {
                this.childrenWidget.clear();
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    private void emptyEquipmentCase() {
        var list = ConfigEquipmentData.getEquipmentById(this.equipment.getId());
        List<Equipment> el = list.stream().map(Equipments::getEquipmentInstanceById).toList();
        el = this.ship.evaluateEquipments(el);
        for (int i = 0; i < el.size(); i++) {
            var widget = new SelectionWidget(this.getX() + CHILD_OFFSET_X, this.getY() + CHILD_OFFSET_Y, el.get(i), this.ship, this.index);
            widget.setParent(this);
            this.childrenWidget.add(widget);
        }

        this.state = WidgetState.SELECTING;
    }

    private void upgradeEquipmentCase() {
        var list = ConfigEquipmentData.getEquipmentById(this.equipment.getId());
        List<Equipment> el = list.stream().map(Equipments::getEquipmentInstanceById).toList();
        el = this.ship.evaluateEquipments(el);
        for (int i = 0; i < el.size(); i++) {
            this.childrenWidget.add(new SelectionWidget(this.getX() + CHILD_OFFSET_X, this.getY() + CHILD_OFFSET_Y, el.get(i), this.ship, this.index));
        }

        this.state = WidgetState.SELECTING;
    }


    protected void renderEquipmentLevel(GuiGraphics guiGraphics) {
        if (this.equipment.getId() < 0) {
            return;
        }
        int equipmentLevel = this.equipment.getLevel();

        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(equipmentLevel), this.getX() + 2, this.getY() + textCenterY, WHITE);
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

    private void evaluateState() {
        if (this.equipment.getId() == -1) {
            this.state = WidgetState.EMPTY_EQUIPMENT;
        }
        else if (this.equipment.getLevel() == Equipment.MAX_LEVEL) {
            this.state = WidgetState.UPGRADE_EQUIPMENT;
        }
        else {
            this.state = WidgetState.LEVEL_UP_EQUIPMENT;
        }
    }

}
