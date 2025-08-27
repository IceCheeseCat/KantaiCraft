package com.github.icecheesecat.kantaicraft.menu.shipyard;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.block.shipyard.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.blueprint.Blueprint;
import com.github.icecheesecat.kantaicraft.registries.ModEntity;
import com.github.icecheesecat.kantaicraft.registries.ModItem;
import com.github.icecheesecat.kantaicraft.tags.EntityTypeTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShipyardScreen extends AbstractContainerScreen<ShipyardMenu> {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(KantaiCraft.MODID, "textures/gui/shipyard/shipyard_background.png");
    private static final ResourceLocation DESTROYER_SILHOUETTE = new ResourceLocation(KantaiCraft.MODID, "textures/gui/shipyard/destroyer_silhouette.png");
    private static final int WHITE = FastColor.ARGB32.color(255, 255, 255, 255);
    private int BUILTSHIP_X;
    private int BUILTSHIP_Y;
    ShipyardBlockEntity shipyardBlockEntity;
    int SILHOUETTE_X, SILHOUETTE_Y, SILHOUETTE_PADDING;
    int SILHOUETTE_WIDTH, SILHOUETTE_HEIGHT;
    List<BuiltShipButton> builtShipButtons = new ArrayList<>();
    private int BUILTSHIP_WIDTH = 104;
    private int BUILTSHIP_HEIGHT = 18;
    final int processSize;

    public ShipyardScreen(ShipyardMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.shipyardBlockEntity = pMenu.getShipyardBlockEntity();
        this.processSize = shipyardBlockEntity.processShipSize;

        for (int i = 0; i < this.shipyardBlockEntity.processShipSize; i++) {
            this.builtShipButtons.add(i, new BuiltShipButton(i, BUILTSHIP_X, BUILTSHIP_Y, BUILTSHIP_WIDTH, BUILTSHIP_HEIGHT, Component.empty(), this.shipyardBlockEntity));
        }
        this.builtShipButtons.forEach(this::addRenderableWidget);
    }

    @Override
    protected void init() {
        super.init();
        this.imageWidth = 176;
        this.imageHeight = 192;
        this.SILHOUETTE_X = this.leftPos + 74;
        this.SILHOUETTE_Y = this.topPos + 11;
        this.SILHOUETTE_PADDING = 23;
        this.SILHOUETTE_WIDTH = 64;
        this.SILHOUETTE_HEIGHT = 18;
        this.BUILTSHIP_X = 52 + this.leftPos;
        this.BUILTSHIP_Y = 11 + this.topPos;
        for (int i = 0; i < this.processSize; i++) {
            this.builtShipButtons.get(i).setPosition(BUILTSHIP_X, BUILTSHIP_Y + SILHOUETTE_PADDING * i);
        }
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
        this.renderBuiltShip(pGuiGraphics);
        this.renderBuiltShipButton();

        // render process time
        for (int i = 0; i < this.shipyardBlockEntity.getProcessShipSize(); i++) {
            if (this.shipyardBlockEntity.hasProcess(i)) {
                pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, processString(i),  this.leftPos+103,   this.topPos + 16 + 22 * i, WHITE);
            }
        }

    }


    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
//        super.renderLabels(pGuiGraphics, pMouseX, pMouseY);
    }

    private Component processString(int i) {
        String remainTimeStr = this.remainTime(this.shipyardBlockEntity.getRemainTime(i));

        ItemStack itemStack = this.getMenu().getItems().get(i + 36);
        String strTemp = "undefined";
        if (itemStack.is(ModItem.SHIP_BLUEPRINT.get())) {
            var blueprint = Blueprint.createFromTag(itemStack.getTag());
            var entityType = ModEntity.getByEntityId(blueprint.getEntityID());
            strTemp = remainTimeStr;
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

    private void renderBuiltShip(GuiGraphics guiGraphics) {
        for (int i = 0; i < shipyardBlockEntity.processShipSize; i++) {
            if (this.shipyardBlockEntity.getItem(i).isEmpty()) continue;
            var data = shipyardBlockEntity.getBlueprintAt(i);
            var entityType = ModEntity.getByEntityId(data.getEntityID());
            if (entityType.is(EntityTypeTags.DESTROYER_TAG)) {
                float percentage = shipyardBlockEntity.getProcessPercentage(i);
                guiGraphics.blit(DESTROYER_SILHOUETTE, SILHOUETTE_X, SILHOUETTE_Y + SILHOUETTE_PADDING * i, 0, 0, SILHOUETTE_WIDTH, (int) (SILHOUETTE_HEIGHT * percentage), SILHOUETTE_WIDTH, SILHOUETTE_HEIGHT);
            }
        }
    }

    private void renderBuiltShipButton() {
        for (int i = 0; i < this.shipyardBlockEntity.processShipSize; i++) {
            var builtShipButton = this.builtShipButtons.get(i);
            if (this.shipyardBlockEntity.hasCompletedBuilding(i)) {
                builtShipButton.active = builtShipButton.visible = true;
            }
            else {
                builtShipButton.active = builtShipButton.visible = false;
            }
        }
    }

}
