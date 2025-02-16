package com.github.icecheesecat.kantaicraft.menu.shipyard;

import com.github.icecheesecat.kantaicraft.block.ShipyardBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;

public class ShipyardScreen extends AbstractContainerScreen<ShipyardMenu> {

    private static final int BACKGROUND = FastColor.ARGB32.color(102, 0, 0, 0);

    ShipyardBlockEntity shipyardBlockEntity;

    public ShipyardScreen(ShipyardMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.shipyardBlockEntity = pMenu.shipyardBlockEntity;
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
                pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, "" + this.shipyardBlockEntity.getRemainTime(i), 100, 20 * i, WHITE);
            }
        }

        var builtData = this.shipyardBlockEntity.getBuiltData();
        for (int i = 0; i < builtData.size(); i++) {
            pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, builtData.get(i).getName(), 300, 20 * i + 20, WHITE);
        }
    }
}
