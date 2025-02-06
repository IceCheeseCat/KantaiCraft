package com.github.icecheesecat.kantaicraft.menu.screen;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.capability.EquipmentProvider;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class EquipmentScreen extends Screen {

    // TO-DO config
    private int left;
    private int top;
    private int widgetGap = 10;
    BasicEntityShip ship;

    public List<EquipmentWidget> equipmentWidgets = new ArrayList<>();

    protected EquipmentScreen(Component pTitle, BasicEntityShip ship, Screen preScreen) {
        super(pTitle);
        this.width = preScreen.width;
        this.height = preScreen.height;
        this.left = this.width / 90;
        this.top = this.height / 90;
        this.ship = ship;

        ship.getCapability(EquipmentProvider.EQUIPMENT_HANDLER_CAPABILITY).ifPresent(
                handler -> {
                    for (int i = 0; i < handler.getSlotSize(); i++)     {
                        this.equipmentWidgets.add(new EquipmentWidget(this.left, this.top + (EquipmentWidget.sizeY + widgetGap) * i, ship, handler.getEquipment(i), i));
                    }
                }
        );

        for (var b : this.equipmentWidgets) {
            this.addRenderableWidget(b);
        }

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public void tick() {
        this.ship.getCapability(EquipmentProvider.EQUIPMENT_HANDLER_CAPABILITY).ifPresent(
                handler -> {
                    for (int i = 0; i < handler.getSlotSize(); i++) {
                        equipmentWidgets.get(i).setEquipment(handler.getEquipment(i));
                    }
                }
        );
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void addRenderableWidget(AbstractWidget widget) {
        super.addRenderableWidget(widget);
    }

}
