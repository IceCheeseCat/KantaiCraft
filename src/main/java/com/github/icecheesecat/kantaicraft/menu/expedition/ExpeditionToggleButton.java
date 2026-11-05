package com.github.icecheesecat.kantaicraft.menu.expedition;

import com.lowdragmc.lowdraglib2.gui.ui.elements.Button;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ExpeditionToggleButton extends Button {

    private ExpeditionState m_expeditionState = ExpeditionState.NO_SELECTION;

    public ExpeditionToggleButton() {
        setFocusable(true);
        setOnClick(p_event -> announceState());
        setExpeditionState(ExpeditionState.NO_SELECTION);
    }

    public ExpeditionState getExpeditionState() {
        return m_expeditionState;
    }

    public void setExpeditionState(ExpeditionState p_state) {
        m_expeditionState = p_state;
        setText(Component.translatable(p_state.getTranslationKey()));
        setActive(p_state != ExpeditionState.NO_SELECTION && p_state != ExpeditionState.DATA_UNAVAILABLE);
    }

    public void announceState() {
        Minecraft.getInstance().getNarrator().sayNow(Component.translatable(getExpeditionState().getTranslationKey()));
    }
}
