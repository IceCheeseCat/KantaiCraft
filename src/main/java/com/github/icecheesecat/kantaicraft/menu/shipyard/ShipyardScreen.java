package com.github.icecheesecat.kantaicraft.menu.shipyard;

import com.github.icecheesecat.kantaicraft.block.shipyard.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.block.shipyard.BuiltData;
import com.github.icecheesecat.kantaicraft.capability.ShipBlueprintCapability;
import com.github.icecheesecat.kantaicraft.registries.ModItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShipyardScreen extends AbstractContainerScreen<ShipyardMenu> {

    private static final int BACKGROUND = FastColor.ARGB32.color(102, 0, 0, 0);

    ShipyardBlockEntity shipyardBlockEntity;
    List<BuiltShipWidget> builtShipWidgets;

    public ShipyardScreen(ShipyardMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.shipyardBlockEntity = pMenu.shipyardBlockEntity;
        this.builtShipWidgets = new ArrayList<>();
        List<BuiltData> builtData = shipyardBlockEntity.getBuiltData();
        for (BuiltData builtDatum : builtData) {
            builtShipWidgets.add(new BuiltShipWidget(0, 0, this.shipyardBlockEntity, builtDatum));
        }

    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.fill(0, 0, this.width, this.height, -1000, BACKGROUND);
    }

    private static final int WHITE = FastColor.ARGB32.color(255, 255, 255, 255);
    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        for (int i = 0; i < this.shipyardBlockEntity.getProcessShipSize(); i++) {
            if (this.shipyardBlockEntity.hasProcess(i)) {
                pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, processString(i), 100, 20 * i, WHITE);
            }
        }

        checkRemovedWidget();
        checkBuiltData();
        timeRender++;
    }

    String strTemp;
    private Component processString(int i) {
        String remainTimeStr = this.remainTime(this.shipyardBlockEntity.getRemainTime(i));

        ItemStack itemStack = this.getMenu().getItems().get(i + 36);
        strTemp = "undefined";
        if (itemStack.is(ModItem.SHIP_BLUEPRINT.get())) {
            itemStack.getCapability(ShipBlueprintCapability.TOKEN).ifPresent(
                    shipBlueprintData -> {
                        strTemp = shipBlueprintData.getName().getString() + " " + remainTimeStr;
                    }
            );
        }

        return Component.literal(strTemp);
    }

    private String remainTime(int tick) {

        int hour = tick / 3600 / 20;
        int minute = tick/ 60 / 20 - hour * 60;
        int second = tick / 20 - hour * 3600 - minute * 60;

        if (hour > 0) {
            return hour + " hour " + minute + " minute " + second + " second";
        }
        else if (minute > 0) {
            return minute + " minute " + second + " second";
        }
        else {
            return second + " second";
        }

    }

    @Override
    protected void init() {
        super.init();
        rearrangeWidgetLayout();
    }

    private void rearrangeWidgetLayout() {
        this.builtShipWidgets.forEach(this::removeWidget);

        GridLayout gridLayout = new GridLayout(200, 20);
        for (int i = 0; i < this.builtShipWidgets.size(); i++) {
            gridLayout.addChild(this.builtShipWidgets.get(i), i, 0);
        }

        gridLayout.arrangeElements();
        gridLayout.visitWidgets(this::addRenderableWidget);
    }

    int timeRender = 0;
    private void checkRemovedWidget() {
        if (timeRender % 60 != 0) return;
        List<BuiltShipWidget> removeWidget = new ArrayList<>();
        for (var widget: this.builtShipWidgets) {
            if (widget.isRemoved()) {
                removeWidget.add(widget);
                this.removeWidget(widget);
            }
        }

        this.builtShipWidgets.removeAll(removeWidget);
        for (var widget: removeWidget) {
            this.shipyardBlockEntity.getBuiltData().remove(widget.getBuiltData());
        }
        if (!removeWidget.isEmpty()) {
            rearrangeWidgetLayout();
        }
    }

    private void checkBuiltData() {
        if (timeRender % 60 != 0) return;
        List<BuiltData> builtData = this.shipyardBlockEntity.getBuiltData();
        // builtData has new instance
        boolean foundChange = false;
        for (BuiltData data : builtData) {
            boolean foundAnyWidget = this.builtShipWidgets.stream().anyMatch(widget -> widget.getUUID().compareTo(data.getUuid()) == 0);
            if (!foundAnyWidget) {
                this.builtShipWidgets.add(new BuiltShipWidget(0, 0, this.shipyardBlockEntity, data));
                foundChange = true;
            }
        }

        if (foundChange) {
            rearrangeWidgetLayout();
        }

    }

}
