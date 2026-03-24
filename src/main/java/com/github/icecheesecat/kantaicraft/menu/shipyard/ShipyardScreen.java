package com.github.icecheesecat.kantaicraft.menu.shipyard;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.block.shipyard.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModItem;
import com.github.icecheesecat.kantaicraft.util.RenderingColor;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipyardScreen extends AbstractContainerScreen<ShipyardMenu> {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(KantaiCraft.MODID, "textures/gui/shipyard/shipyard_background.png");
    private int BUILTSHIP_X;
    private int BUILTSHIP_Y;
    ShipyardBlockEntity shipyardBlockEntity;
    int SILHOUETTE_X, SILHOUETTE_Y;
    List<BuiltShipButton> builtShipButtons = new ArrayList<>();
    private int BUILTSHIP_WIDTH = 16;
    private int BUILTSHIP_HEIGHT = 16;
    final int processSize;
    private int TIME_X;
    private int TIME_Y;
    private List<ItemStack> prevBlueprints = new ArrayList<>();
    private Map<Integer, LivingEntity> dummyEntities = new HashMap<>();
    private float accumTick = 0.0f;

    public ShipyardScreen(ShipyardMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.shipyardBlockEntity = pMenu.getShipyardBlockEntity();
        this.processSize = shipyardBlockEntity.processShipSize;

        for (int i = 0; i < this.shipyardBlockEntity.processShipSize; i++) {
            this.builtShipButtons.add(i, new BuiltShipButton(i, BUILTSHIP_X, BUILTSHIP_Y, BUILTSHIP_WIDTH, BUILTSHIP_HEIGHT, Component.empty(), this.shipyardBlockEntity, BACKGROUND));
        }
        for (int i = 0; i < shipyardBlockEntity.processShipSize; i++) {
           prevBlueprints.add(ItemStack.EMPTY);
        }
    }

    @Override
    protected void init() {
        super.init();
        this.imageWidth = 176;
        this.imageHeight = 192;
        this.SILHOUETTE_X = this.leftPos + 74;
        this.SILHOUETTE_Y = this.topPos + 11;
        this.TIME_X = this.leftPos + 25;
        this.TIME_Y = this.topPos + 72;
        this.BUILTSHIP_X = this.leftPos + 152;
        this.BUILTSHIP_Y = this.topPos + 88;
        for (int i = 0; i < this.processSize; i++) {
            this.builtShipButtons.get(i).setPosition(BUILTSHIP_X, BUILTSHIP_Y);
        }
        this.builtShipButtons.forEach(this::addRenderableWidget);
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
        this.renderBuiltShip(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
        this.renderBuiltShipButton();

        // render process time
        for (int i = 0; i < this.shipyardBlockEntity.getProcessShipSize(); i++) {
            if (this.shipyardBlockEntity.hasProcess(i)) {
                pGuiGraphics.pose().pushPose();
                pGuiGraphics.pose().translate(this.TIME_X, this.TIME_Y + 22 * i, 0);
                pGuiGraphics.pose().scale(0.8f, 0.8f, 0.8f);
                pGuiGraphics.drawString(Minecraft.getInstance().font, processString(i), 0, 0, RenderingColor.WHITE);
                pGuiGraphics.pose().popPose();
            }
        }

    }


    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
//        super.renderLabels(pGuiGraphics, pMouseX, pMouseY);
    }

    private Component processString(int i) {
        int tick = this.shipyardBlockEntity.getRemainTime(i);
        if (tick == 0) {
            return Component.literal("Done!");
        }

        String remainTimeStr = this.remainTime(tick);

        ItemStack itemStack = this.getMenu().getItems().get(i + 36);
        String strTemp = "undefined";
        if (itemStack.is(ModItem.SHIP_BLUEPRINT.get())) {
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

    private void renderBuiltShip(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.pose().pushPose();
        for (int i = 0; i < shipyardBlockEntity.processShipSize; i++) {
            if (this.shipyardBlockEntity.getItem(i).isEmpty() || this.shipyardBlockEntity.getBlueprintAt(i).isEmpty()) continue;
            var data = shipyardBlockEntity.getBlueprintAt(i);
            var shipClass = data.getShipClass();
            float percentage = shipyardBlockEntity.getProcessPercentage(i);
            // TODO
            var dummyEntity = this.dummyEntities.get(i);
            if (dummyEntity != null) {
                var pose =  (new Quaternionf()).rotateZ((float)Math.PI);
                pose.mul(Axis.YN.rotationDegrees(180 + accumTick));
                renderDarkEntity(guiGraphics, this.leftPos + 124, this.topPos + 94, 40, pose, null, dummyEntity);
                accumTick += partialTick;
            }
        }
        guiGraphics.pose().popPose();
    }

    public static void renderDarkEntity(GuiGraphics pGuiGraphics, int pX, int pY, int pScale, Quaternionf pPose, @Nullable Quaternionf pCameraOrientation, LivingEntity pEntity) {
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate((double)pX, (double)pY, 50.0D);
        pGuiGraphics.pose().mulPoseMatrix((new Matrix4f()).scaling((float)pScale, (float)pScale, (float)(-pScale)));
        pGuiGraphics.pose().mulPose(pPose);
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        if (pCameraOrientation != null) {
            pCameraOrientation.conjugate();
            entityrenderdispatcher.overrideCameraOrientation(pCameraOrientation);
        }

        entityrenderdispatcher.setRenderShadow(false);
        RenderSystem.runAsFancy(() -> {
            entityrenderdispatcher.render(pEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, pGuiGraphics.pose(), pGuiGraphics.bufferSource(), -10000);
        });
        pGuiGraphics.flush();
        entityrenderdispatcher.setRenderShadow(true);
        pGuiGraphics.pose().popPose();
        Lighting.setupFor3DItems();
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

    @Override
    protected void containerTick() {
        for (int i = 0; i < prevBlueprints.size(); i++) {
            if (!prevBlueprints.get(i).equals(shipyardBlockEntity.getItem(i), false)) {
                // get new Entity Renderer
                if (shipyardBlockEntity.getBlueprintAt(i).isEmpty()) {
                    this.dummyEntities.remove(i);
                }
                else {
                    var optional = shipyardBlockEntity.getBlueprintAt(i).getEntityType();
                    int finalI = i;
                    optional.ifPresent(entityType -> {
                        var renderer = Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(entityType);
                        if (renderer != null) {
                            EntityShip entityShip =(EntityShip) entityType.create(Minecraft.getInstance().level);
                            entityShip.setNoAnimation();
                            this.dummyEntities.put(finalI, entityShip);
                        }
                    });
                }
            }
        }

        for (int i = 0; i < prevBlueprints.size(); i++) {
            this.prevBlueprints.set(i, shipyardBlockEntity.getItem(i).copy());
        }
    }
}
