package com.github.icecheesecat.kantaicraft.menu.commandcenter;

import com.github.icecheesecat.kantaicraft.client.EquipmentRendererCache;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.menu.GuiLerp;
import com.github.icecheesecat.kantaicraft.menu.HoveringPage;
import com.github.icecheesecat.kantaicraft.menu.pagescreen.Page;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import org.joml.Vector3d;
import org.joml.Vector3f;
import software.bernie.geckolib.renderer.GeoRenderer;

public class EquipmentDetailPage extends HoveringPage {

    private static final int BACKGROUND_COLOR = FastColor.ARGB32.color(128, 128, 128, 128);
    private static final int BACKGROUND_OUTLINE_COLOR = FastColor.ARGB32.color(255, 128, 128, 128);

    final GeoRenderer<Equipment> renderer;
    final Equipment equipment;
    int lastMouseX, lastMouseY;
    float lerpAngle;

    public EquipmentDetailPage(Component title, int x, int y, int width, int height, Equipment equipment) {
        super(title, x, y, width, height);
        this.equipment = equipment;
        this.renderer = EquipmentRendererCache.getEquipmentRenderer(equipment.getId());
        this.lastMouseX = x + width/ 2;
        this.lastMouseY = y + height / 2 + 20;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderEquipmentModel(guiGraphics, partialTick, mouseX, mouseY);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 1000);

        guiGraphics.fill(this.x, this.y, this.x+this.width, this.y+this.height, FastColor.ARGB32.color(200, 128, 128, 128));
        guiGraphics.renderOutline(this.x, this.y, this.width, this.height, BACKGROUND_OUTLINE_COLOR);
        renderEquipmentDetail(guiGraphics, partialTick);

        guiGraphics.pose().popPose();
    }

    protected void renderEquipmentModel(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(this.x + this.width/2, this.y + this.height/2 + 20, 1000);
        poseStack.mulPose(Axis.YP.rotation((float) Math.PI));
        poseStack.mulPose(Axis.ZP.rotation((float) Math.PI));
        poseStack.scale(80, 80, 80);
        this.rotateModelTowardCursorWhenFocused(poseStack, mouseX, mouseY, partialTick);
        renderer.defaultRender(poseStack, null, guiGraphics.bufferSource(), null, null, 0, partialTick, 255);
        poseStack.popPose();
    }

    protected void rotateModelTowardCursorWhenFocused(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        if (this.isFocused()) {
            this.lastMouseX = mouseX;
            this.lastMouseY = mouseY;
        }

        int centerX = this.x + this.width / 2;
        int centerY = (this.y + this.height / 2 + 20);
        int iCenterZ = -100;
        Vector3f model = new Vector3f(0, 0, iCenterZ);
        Vector3f modelNorm = new Vector3f(0,0,-iCenterZ);
        Vector3f cursor = new Vector3f(-(lastMouseX - centerX), (lastMouseY - centerY), 0);
        Vector3f modelToCursor = cursor.sub(model);
        Vector3f perp = modelNorm.cross(modelToCursor);
        float v = Math.abs(iCenterZ / modelToCursor.length());
        float angle = (float) Math.acos(v);
        if (angle != 0.0d) {
            lerpAngle = GuiLerp.lerpAngle(lerpAngle, angle,  partialTick * 0.1f);
            poseStack.mulPose(Axis.of(perp).rotation(lerpAngle));
        }
    }

    static final int FONT_COLOR = FastColor.ARGB32.color(255, 255,255, 255);
    protected void renderEquipmentDetail(GuiGraphics guiGraphics, float partialTick) {
        Component name = this.equipment.getName();
        int id = this.equipment.getId();
        int level = this.equipment.getLevel();

        guiGraphics.drawString(Minecraft.getInstance().font, name, this.x, this.y, FONT_COLOR);
        guiGraphics.drawString(Minecraft.getInstance().font, String.valueOf(id), this.x, this.y + 10, FONT_COLOR);
        guiGraphics.drawString(Minecraft.getInstance().font, String.valueOf(level), this.x, this.y + 20, FONT_COLOR);

    }


}
