package com.github.icecheesecat.kantaicraft.menu.ship;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.exception.KantaiCraftException;
import com.github.icecheesecat.kantaicraft.network.packet.entityship.SyncType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class SyncedBooleanWidget extends SyncedWidget<Boolean> {
    public SyncedBooleanWidget(int pX, int pY, int pWidth, int pHeight, EntityShip entityShip, EntityDataAccessor<Boolean> accessor, ResourceLocation iconFalse, ResourceLocation overlayIcon, SyncType syncType) {
        super(pX, pY, pWidth, pHeight, entityShip, accessor, Map.of(false, iconFalse, true, overlayIcon), syncType, (b) -> !b);
        if (syncType.getDataType() != SyncType.DataType.BOOLEAN) {
            throw new KantaiCraftException(this.getClass(), "not a boolean SyncType");
        }
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        boolean t = this.getSynced();
        pGuiGraphics.blit(resources.get(false), this.getX(), this.getY(), 0, 0, 32, 32, 32, 32);
        if (t) {
            pGuiGraphics.blit(resources.get(true), this.getX(), this.getY(), 0, 0, 32, 32, 32, 32);
        }
    }
}
