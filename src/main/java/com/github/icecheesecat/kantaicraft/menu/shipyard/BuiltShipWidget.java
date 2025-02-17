package com.github.icecheesecat.kantaicraft.menu.shipyard;

import com.github.icecheesecat.kantaicraft.block.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.block.shipyardUtil.BuiltData;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.ShipyardSpawnBuiltDataPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

public class BuiltShipWidget extends AbstractWidget {

    boolean removed = false;
    ShipyardBlockEntity shipyardBlockEntity;
    BuiltData builtData;

    public BuiltShipWidget(int pX, int pY, ShipyardBlockEntity shipyardBlockEntity, BuiltData builtData) {
        super(pX, pY, 40, 20, Component.empty());
        this.shipyardBlockEntity = shipyardBlockEntity;
        this.builtData = builtData;
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        int stringColor = FastColor.ARGB32.color(255, 0, 0, 0);
        pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, this.builtData.getData().getName(), this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, stringColor);

        int color = FastColor.ARGB32.color(255, 248, 234, 213);
        pGuiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), color);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
        ModPacketHandler.INSTANCE.sendToServer(new ShipyardSpawnBuiltDataPacket(this.shipyardBlockEntity.getBlockPos(), this.builtData.getUuid()));
        this.removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }

    public UUID getUUID() {
        return this.builtData.getUuid();
    }

    public BuiltData getBuiltData() {
        return this.builtData;
    }
}
