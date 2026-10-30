package com.github.icecheesecat.kantaicraft.menu.expedition;

import com.github.icecheesecat.kantaicraft.expedition.ExpeditionDefinition;
import com.github.icecheesecat.kantaicraft.expedition.ExpeditionDefinitionManager;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import com.github.icecheesecat.kantaicraft.registries.ModMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;
import java.util.List;

public class ExpeditionMenu extends AbstractContainerMenu {

    private ContainerLevelAccess m_access;
    private List<ExpeditionDefinition> m_expeditions = List.of();

    public ExpeditionMenu(int p_containerId, Inventory p_inventory, ContainerLevelAccess p_access) {
        super(ModMenu.EXPEDITION_MENU.get(), p_containerId);
        m_access = p_access;
        if (!p_inventory.player.level().isClientSide) {
            setExpeditions(getSortedExpeditions());
        }
    }

    public ExpeditionMenu(int p_containerId, Inventory p_inventory, FriendlyByteBuf p_extraData) {
        this(p_containerId, p_inventory,
                ContainerLevelAccess.create(p_inventory.player.level(), p_extraData.readBlockPos()));
        setExpeditions(p_extraData.readList(ExpeditionMenu::readExpedition));
    }

    public ContainerLevelAccess getAccess() {
        return m_access;
    }

    public List<ExpeditionDefinition> getExpeditions() {
        return m_expeditions;
    }

    public void setExpeditions(List<ExpeditionDefinition> p_expeditions) {
        m_expeditions = List.copyOf(p_expeditions);
    }

    public static void writeOpeningData(FriendlyByteBuf p_buffer, BlockPos p_pos) {
        p_buffer.writeBlockPos(p_pos);
        p_buffer.writeCollection(getSortedExpeditions(), ExpeditionMenu::writeExpedition);
    }

    private static List<ExpeditionDefinition> getSortedExpeditions() {
        return ExpeditionDefinitionManager.getInstance().getAllExpeditions().stream()
                .sorted(Comparator.comparing(p_expedition -> p_expedition.getId().toString()))
                .toList();
    }

    private static void writeExpedition(FriendlyByteBuf p_buffer, ExpeditionDefinition p_expedition) {
        p_buffer.writeResourceLocation(p_expedition.getId());
        p_buffer.writeUtf(p_expedition.getNameKey());
        p_buffer.writeUtf(p_expedition.getDescriptionKey());
        p_buffer.writeLong(p_expedition.getDurationTicks());
        p_buffer.writeMap(p_expedition.getRewards(), FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeVarInt);
    }

    private static ExpeditionDefinition readExpedition(FriendlyByteBuf p_buffer) {
        ExpeditionDefinition expedition = new ExpeditionDefinition(
                p_buffer.readResourceLocation(), p_buffer.readUtf(), p_buffer.readUtf());

        expedition.setDurationTicks(p_buffer.readLong());
        expedition.setRewards(p_buffer.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readVarInt));
        return expedition;
    }

    @Override
    public ItemStack quickMoveStack(Player p_player, int p_index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player p_player) {
        return stillValid(getAccess(), p_player, ModBlock.EXPEDITION.get());
    }
}
