package com.github.icecheesecat.kantaicraft.menu.screen;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.SyncShipPacket;
import com.github.icecheesecat.kantaicraft.network.packet.SyncType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SyncedWidget<T> extends AbstractWidget {

    Map<T, ResourceLocation> resources;
    BasicEntityShip ship;
    EntityDataAccessor<T> accessor;
    Function<T, T> operation;

    public SyncedWidget(int pX, int pY, int pWidth, int pHeight, BasicEntityShip ship, EntityDataAccessor<T> accessor, Map<T, ResourceLocation> resources, Function<T, T> operation) {
        super(pX, pY, pWidth, pHeight, Component.empty());
        this.ship = ship;
        this.accessor = accessor;
        this.resources = resources;
        this.operation = operation;
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        T t = ship.getEntityData().get(accessor);

        pGuiGraphics.blit(resources.get(t), this.getX(), this.getY(), 0, 0, 32, 32, 32, 32);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        T t = ship.getEntityData().get(accessor);

        ModPacketHandler.INSTANCE.sendToServer(new SyncShipPacket(SyncType.GUARD, ship.getId(), operation.apply(t)));
    }
}
