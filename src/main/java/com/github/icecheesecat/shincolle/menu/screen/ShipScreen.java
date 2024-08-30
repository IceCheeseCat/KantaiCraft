package com.github.icecheesecat.shincolle.menu.screen;

import com.github.icecheesecat.shincolle.Shincolle;
import com.github.icecheesecat.shincolle.menu.ShipMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class ShipScreen extends AbstractContainerScreen<ShipMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Shincolle.MODID, "textures/gui/ship_menu_screen.png");

    public ShipScreen(ShipMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.imageWidth = 100;
        this.imageHeight = 100;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(@NotNull GuiGraphics p_283479_, int p_283661_, int p_281248_, float p_281886_) {
        super.render(p_283479_, p_283661_, p_281248_, p_281886_);
    }
}
