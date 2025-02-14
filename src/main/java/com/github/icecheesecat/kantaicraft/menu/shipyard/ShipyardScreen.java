package com.github.icecheesecat.kantaicraft.menu.shipyard;

import com.github.icecheesecat.kantaicraft.block.shipyardUtil.BlueprintCell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;

public class ShipyardScreen extends AbstractContainerScreen<ShipyardMenu> {

    private static final int BACKGROUND = FastColor.ARGB32.color(102, 0, 0, 0);
    private final NonNullList<BlueprintCell> blueprintCells;

    public ShipyardScreen(ShipyardMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.blueprintCells = pMenu.getBlueprintCells();
    }

    @Override
    protected void containerTick() {
        for (var cell: this.blueprintCells) {
            if (cell.tick()) {
                if (cell.done()) {
                    cell.removeBlueprint(true);
                }
            }
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
        for (int i = 0; i < blueprintCells.size(); i++) {
            BlueprintCell cell = blueprintCells.get(i);
            if (cell.isEmpty()) continue;
            pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, "" + cell.remainTime(), 100, 20 * i, WHITE);
        }
    }
}
