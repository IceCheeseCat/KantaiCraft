package com.github.icecheesecat.kantaicraft.menu.screen;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.capability.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.capability.EquipmentProvider;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicCannonShip;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.equipment.*;
import com.github.icecheesecat.kantaicraft.network.packet.SyncShipPacket;
import com.github.icecheesecat.kantaicraft.registries.ModAttribute;
import com.github.icecheesecat.kantaicraft.menu.ShipMenu;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.SyncType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class ShipScreen extends AbstractContainerScreen<ShipMenu> {
    private static final ResourceLocation BACKGROUND_1 = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_menu_background_1.png");
    private static final ResourceLocation FIREPOWER_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/firepower_icon.png");
    private static final ResourceLocation TORPEDO_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/torpedo_icon.png");
    private static final ResourceLocation ANTIAIR_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/antiair_icon.png");
    private static final ResourceLocation ASW_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/asw_icon.png");

    private final BasicEntityShip ship;

    Button toggleGuarding;
    Button toggleMelee;
    Button toggleCannonFireMode;
    Button equipmentSection;

    EquipmentHandler equipmentHandler;

    public ShipScreen(ShipMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.imageWidth = 320;
        this.imageHeight = 180;

        this.ship = this.getMenu().getEntityShip();

        toggleGuarding = Button.builder(Component.translatable("shipscreen.toggleguarding"), button -> {
            ModPacketHandler.INSTANCE.sendToServer(new SyncShipPacket(SyncType.GUARD, this.ship.getId(), !this.ship.isGuarding()));
        }).pos(100, 100).build();

        toggleMelee = Button.builder(Component.translatable("shipscreen.togglemelee"), button -> {
            ModPacketHandler.INSTANCE.sendToServer(new SyncShipPacket(SyncType.MELEE, this.ship.getId(), !this.ship.canMelee()));
        }).pos(100, 125).build();

        toggleCannonFireMode = Button.builder(Component.translatable("shipscreen.togglecannonfiremode"), button -> {
            if (this.ship instanceof BasicCannonShip cannonShip) {
                ModPacketHandler.INSTANCE.sendToServer(new SyncShipPacket(SyncType.CANNON_FIRE_MODE, cannonShip.getId(), cannonShip.getCannonFireMode().getNext()));
            }
        }).pos(100, 150).build();

        equipmentSection = Button.builder(Component.translatable("shipscreen.equipmentsection"), pButton -> {
            this.minecraft.setScreen(new EquipmentScreen(Component.literal("equipment_screen"), this.ship));
        }).pos(100, 175).build();

        this.addRenderableWidget(toggleGuarding);
        this.addRenderableWidget(toggleMelee);
        this.addRenderableWidget(toggleCannonFireMode);
        this.addRenderableWidget(equipmentSection);

        this.ship.getCapability(EquipmentProvider.EQUIPMENT_HANDLER_CAPABILITY).ifPresent(
            handler -> {
                this.equipmentHandler = handler;
            }
        );
    }



    /*
        blit(
         ResourceLocation,
         leftCornerX, leftCornerY,
         imageStartX, imageStartY,
         bottomRightX, bottomRightY,
         sizeX, sizeY
        )
     */

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
//        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.blit(BACKGROUND_1, 0, 0, 0, 0, this.width, this.height, this.width, this.height);

        toggleGuarding.setMessage(Component.literal("Toggle guarding: " + this.ship.isGuarding()));
        toggleMelee.setMessage(Component.literal("Toggle melee: " + this.ship.canMelee()));
        if (ship instanceof BasicCannonShip cannonShip) {
            toggleCannonFireMode.setMessage(Component.literal("Toggle cannon fire mode: " + cannonShip.getCannonFireMode()));
        }

        //show status
        if (menu.getEntityShip() != null) {
            renderShipAttrs(guiGraphics, menu.getEntityShip(), 24, 5, FastColor.ABGR32.color(255, 255, 255 , 255));
        }

        for (var r: renderables) {
            r.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        for (int i = 0; i < this.equipmentHandler.getSlotSize(); i++) {
            Equipment equipment = this.equipmentHandler.getEquipments().get(i);
            EquipmentType type = this.equipmentHandler.getEquipments().get(i).getType();

            // show equipments
//            if (equipment == this.ship.getShipClass().getDefaultEquipment()) {
//                //draw sth
//            }
//            else {
//
//            }
        }
        //        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

    }

    private void renderShipAttrs(GuiGraphics guiGraphics, BasicEntityShip entity, int x, int y, int color) {

        int offset = 8;
        int xOffset = 12;

//        var attributes = entity.getAttributes();
        guiGraphics.blit(FIREPOWER_ICON, x, y, 0, 0, 64, 64, 64, 64);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModAttribute.FIREPOWER.get())) , x, y, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModAttribute.TORPEDO.get())) , x, y + offset * 1, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModAttribute.ANTIAIR.get())) , x, y + offset * 2, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModAttribute.ASW.get())) , x, y + offset * 3, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModAttribute.LOS.get())) , x, y + offset * 4, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModAttribute.LUCK.get())) , x, y + offset * 5, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(Attributes.MAX_HEALTH)) , x + xOffset, y + offset * 6, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getHealth()) , x - xOffset, y + offset * 6, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModAttribute.ARMOR.get())) , x, y + offset * 7, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModAttribute.EVASION.get())) , x, y + offset * 8, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(Attributes.MOVEMENT_SPEED)) , x, y +  offset * 9, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModAttribute.AIRCRAFT.get())) , x + xOffset, y +  offset * 10, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAircraft()) , x - xOffset, y +  offset * 10, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModAttribute.FUEL.get())) , x + xOffset, y +  offset * 11, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getFuel()) , x - xOffset, y +  offset * 11, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModAttribute.AMMO.get())) , x + xOffset, y +  offset * 12, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAmmo()) , x - xOffset, y +  offset * 12, color);

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
    }

}
