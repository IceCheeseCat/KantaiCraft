package com.github.icecheesecat.kantaicraft.menu.screen;


import com.github.icecheesecat.kantaicraft.common.EquipmentManager;
import com.github.icecheesecat.kantaicraft.entity.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.common.Equipments;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ShowEquipmentStatusButton extends Button {

    Equipment equipment;
    protected ShowEquipmentStatusButton(Equipment equipment, int p_259075_, int p_259271_, int p_260232_, int p_260028_, Button.OnPress onPress, Component p_259351_, CreateNarration p_259552_) {
        super(p_259075_, p_259271_, p_260232_, p_260028_, p_259351_, onPress, p_259552_);

        this.equipment = equipment;
        String str = String.format("%s\nLv: %s\n", EquipmentManager.getEquipmentById(equipment.getUid()), equipment.getEquipmentLevel().getLevel());
        setTooltip(Tooltip.create(Component.translatable(str)));
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float p_282542_) {
        super.renderWidget(guiGraphics, mouseX, mouseY, p_282542_);
    }
}
