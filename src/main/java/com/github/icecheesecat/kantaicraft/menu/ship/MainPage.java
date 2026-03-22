package com.github.icecheesecat.kantaicraft.menu.ship;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.menu.pagescreen.Page;
import com.github.icecheesecat.kantaicraft.util.RenderingColor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.joml.Matrix4f;

public class MainPage extends Page {

    private static final ResourceLocation RESOURCES_BACKGROUND = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_screen/resources_background.png");
    private final EntityShip entityShip;

    public MainPage(EntityShip entityShip, Component title, int x, int y, int width, int height) {
        super(title, x, y, width, height);
        this.entityShip = entityShip;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int x = (int) (this.x + this.width * 0.7f);
        int y = (int) (this.y + this.height * 0.1f);
        guiGraphics.blit(RESOURCES_BACKGROUND, x, y, 0, 0, 128, 128, 128, 128);

        entityShip.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(tank -> {
            var fluidStack = tank.getFluidInTank(0);
            IClientFluidTypeExtensions props = IClientFluidTypeExtensions.of(fluidStack.getFluid().defaultFluidState());
            if (props.getStillTexture() != null) {
                var fluidSprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(props.getStillTexture());

                int amount = fluidStack.getAmount();
                int max = tank.getTankCapacity(0);

                blitPercentage(guiGraphics,  (float) amount / (float) max, x + 1, y + 1, 29, 126, fluidSprite);
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(x + 1, y+height, 0);
                guiGraphics.pose().scale(0.5f, 0.5f, 0.5f);
                guiGraphics.drawString(Minecraft.getInstance().font, amount + "/" + max, 0, 0, RenderingColor.WHITE);
                guiGraphics.pose().popPose();
            }
        });
    }

    private void blitPercentage(GuiGraphics guiGraphics, float percentage, int x0, int y0, int texWidth, int texHeight, TextureAtlasSprite sprite) {

        int accumulatedHeight = 0;
        int prevHeight;

        int translateHeight = (int) (texHeight * percentage);

        while(accumulatedHeight < translateHeight) {
            prevHeight = accumulatedHeight;
            accumulatedHeight += 32;
            int localHeight = (accumulatedHeight - translateHeight) <= 0 ? 32 : (translateHeight - prevHeight);
            float localPercentage = localHeight / 32.0f;

            int yLower = (y0 + texHeight) - prevHeight;
            int yHigher = yLower - localHeight;

            float v0 = sprite.getV1() - (sprite.getV1() - sprite.getV0()) * localPercentage;
            customBlit(sprite.atlasLocation(), guiGraphics.pose(), x0,x0 + texWidth, yHigher, yLower, 0, sprite.getU0(), sprite.getU0() + (sprite.getU1() - sprite.getU0()) * (texWidth / 32.0f), v0, sprite.getV1());

        }

    }

    private void customBlit(ResourceLocation pAtlasLocation, PoseStack poseStack, int pX1, int pX2, int pY1, int pY2, int pBlitOffset, float pMinU, float pMaxU, float pMinV, float pMaxV) {
        RenderSystem.setShaderTexture(0, pAtlasLocation);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix4f = poseStack.last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f, (float)pX1, (float)pY1, (float)pBlitOffset).uv(pMinU, pMinV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pX1, (float)pY2, (float)pBlitOffset).uv(pMinU, pMaxV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pX2, (float)pY2, (float)pBlitOffset).uv(pMaxU, pMaxV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pX2, (float)pY1, (float)pBlitOffset).uv(pMaxU, pMinV).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }
}
