package com.github.icecheesecat.kantaicraft.menu.screen;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entity.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentSlots;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import com.github.icecheesecat.kantaicraft.init.ModShipAttributes;
import com.github.icecheesecat.kantaicraft.menu.ShipMenu;
import com.github.icecheesecat.kantaicraft.stats.ShipStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class ShipScreen extends AbstractContainerScreen<ShipMenu> {
    private static final ResourceLocation BACKGROUND_1 = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_menu_background_1.png");
    private static final ResourceLocation FIREPOWER_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/firepower_icon.png");
    private static final ResourceLocation TORPEDO_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/torpedo_icon.png");
    private static final ResourceLocation ANTIAIR_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/antiair_icon.png");
    private static final ResourceLocation ASW_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/asw_icon.png");

    EquipmentSlots slots;
    public ShipScreen(ShipMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.imageWidth = 320;
        this.imageHeight = 180;
        slots = this.getMenu().getEntityShip().getEquipmentSlots();
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
//        super.render(p_283479_, p_283661_, p_281248_, p_281886_);
        guiGraphics.blit(BACKGROUND_1, 0, 0, 0, 0, this.width, this.height, this.width, this.height);

        //show status
        if (menu.getEntityShip() != null) {
            renderShipAttrs(guiGraphics, menu.getEntityShip(), 24, 5, FastColor.ABGR32.color(255, 255, 255 , 255));
        }

        for (var r: renderables) {
            r.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        for (int i = 0; i < slots.getSlotSize(); i++) {
            Equipment equipment = slots.getEquipments().get(i);
            EquipmentType type = slots.getEquipmentTypes().get(i);

            // show equipments
            if (equipment == Equipment.EMPTY) {
                //draw sth
            }
            else {

            }
        }
        //        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

    }

    private void renderShipAttrs(GuiGraphics guiGraphics, BasicEntityShip entity, int x, int y, int color) {

        int offset = 8;
        int xOffset = 12;

//        var attributes = entity.getAttributes();
        guiGraphics.blit(FIREPOWER_ICON, x, y, 0, 0, 64, 64, 64, 64);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModShipAttributes.FIREPOWER.get())) , x, y, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModShipAttributes.TORPEDO.get())) , x, y + offset * 1, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModShipAttributes.ANTIAIR.get())) , x, y + offset * 2, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModShipAttributes.ASW.get())) , x, y + offset * 3, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModShipAttributes.LOS.get())) , x, y + offset * 4, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModShipAttributes.LUCK.get())) , x, y + offset * 5, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(Attributes.MAX_HEALTH)) , x + xOffset, y + offset * 6, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getHealth()) , x - xOffset, y + offset * 6, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModShipAttributes.ARMOR.get())) , x, y + offset * 7, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModShipAttributes.EVASION.get())) , x, y + offset * 8, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(Attributes.MOVEMENT_SPEED)) , x, y +  offset * 9, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModShipAttributes.AIRCRAFT.get())) , x + xOffset, y +  offset * 10, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAircraft()) , x - xOffset, y +  offset * 10, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModShipAttributes.FUEL.get())) , x + xOffset, y +  offset * 11, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getFuel()) , x - xOffset, y +  offset * 11, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAttributeValue(ModShipAttributes.AMMO.get())) , x + xOffset, y +  offset * 12, color);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(entity.getAmmo()) , x - xOffset, y +  offset * 12, color);

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
    }


}
