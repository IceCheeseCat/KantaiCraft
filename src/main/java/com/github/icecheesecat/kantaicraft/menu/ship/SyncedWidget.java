package com.github.icecheesecat.kantaicraft.menu.ship;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.TogglePlayerShipPacket;
import com.github.icecheesecat.kantaicraft.network.packet.SyncType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.function.Function;

public class SyncedWidget<T> extends AbstractWidget {

    Map<T, ResourceLocation> resources;
    ResourceLocation selected;
    EntityShip entityShip;
    EntityDataAccessor<T> accessor;
    Function<T, T> operation;
    SyncType syncType;

    public SyncedWidget(int pX, int pY, int pWidth, int pHeight, EntityShip entityShip, EntityDataAccessor<T> accessor, Map<T, ResourceLocation> resources, SyncType syncType, Function<T, T> operation) {
        super(pX, pY, pWidth, pHeight, Component.empty());
        this.entityShip = entityShip;
        this.accessor = accessor;
        this.resources = resources;
        this.operation = operation;
        this.syncType = syncType;
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        T t = entityShip.getEntityData().get(accessor);
        pGuiGraphics.blit(resources.get(t), this.getX(), this.getY(), 0, 0, 32, 32, 32, 32);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        T t = entityShip.getEntityData().get(accessor);

        ModPacketHandler.INSTANCE.sendToServer(new TogglePlayerShipPacket(syncType, entityShip.getId(), operation.apply(t)));
    }

    public boolean controlOn() {
        if (this.entityShip.getEntityData().get(accessor) instanceof Boolean bool) {
            return bool;
        }
        return false;
    }
}
