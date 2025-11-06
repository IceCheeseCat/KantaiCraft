package com.github.icecheesecat.kantaicraft.menu.commandcenter;

import com.eliotlash.mclib.math.functions.limit.Min;
import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.capability.ClientPlayerKantaiDataCacheCapability;
import com.github.icecheesecat.kantaicraft.capability.PlayerKantaiData;
import com.github.icecheesecat.kantaicraft.capability.PlayerKantaiDataCapability;
import com.github.icecheesecat.kantaicraft.client.ClientPlayerKantaiDataCache;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.menu.ShipSelectButton;
import com.github.icecheesecat.kantaicraft.menu.ship.CustomTextureButton;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.CommandCenterRequestSummonPacket;
import com.github.icecheesecat.kantaicraft.network.packet.RequestPlayerKantaiDataPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommandCenterScreen extends CustomScreen<CommandCenterMenu> {

    private static final ResourceLocation COMMAND_CENTER_BACKGROUND = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/command_center_background.png");
    private static final ResourceLocation NEXT_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/next_icon.png");
    private static final ResourceLocation NEXT_HOVERED_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/next_icon_hovered.png");
    private static final ResourceLocation PREV_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/prev_icon.png");
    private static final ResourceLocation PREV_ICON_HOVERED = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/prev_icon_hovered.png");
    List<AbstractWidget> currentWidgets = new ArrayList<>();
    int page = 0;

    public CommandCenterScreen(CommandCenterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 256;
        this.imageHeight = 149;
    }

    @Override
    protected void init() {
        super.init();
        this.setupShipSelectWidgets();
        this.setupPageButton();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBackground(pGuiGraphics);
        int imageWidth = 256;
        int imageHeight = 149;
        this.renderWithOriginalSize(COMMAND_CENTER_BACKGROUND, pGuiGraphics, imageWidth, imageHeight);
    }

    protected void setupShipSelectWidgets() {
        ModPacketHandler.INSTANCE.sendToServer(new RequestPlayerKantaiDataPacket(Minecraft.getInstance().player.getUUID()));
        refreshPageWidget();
    }

    protected void setupPageButton() {
        this.addRenderableWidget(new CustomTextureButton(this.leftPos + 5, this.topPos + this.imageHeight /2, 16, 16, Component.empty(), PREV_ICON, PREV_ICON_HOVERED) {
            @Override
            public void onPress() {
                prevPage();
                refreshPageWidget();
            }
        });

        this.addRenderableWidget(new CustomTextureButton(this.leftPos + this.imageWidth - 5, this.topPos + this.imageHeight / 2, 16, 16, Component.empty(), NEXT_ICON, NEXT_HOVERED_ICON) {
            @Override
            public void onPress() {
                nextPage();
                refreshPageWidget();
            }
        });
    }

    public static List<Component> getPreviewContent(EntityShip entityShip) {

        return List.of(Component.translatable(entityShip.getName().getString()), Component.literal(String.valueOf(entityShip.getShipLevel())));

    }

    protected void nextPage() {
        this.page++;
        if (this.pageReachedEnd()) {
            this.page = 0;
        }
    }

    protected boolean pageReachedEnd() {
        AtomicBoolean reached = new AtomicBoolean();
        Minecraft.getInstance().player.getCapability(ClientPlayerKantaiDataCacheCapability.TOKEN).ifPresent(clientPlayerKantaiDataCache -> {
            reached.set(clientPlayerKantaiDataCache.getListOfShips().size() <= this.page * 4);
        });

        return reached.get();
    }

    protected void prevPage() {
        this.page--;
        if (this.page < 0) {
            Minecraft.getInstance().player.getCapability(ClientPlayerKantaiDataCacheCapability.TOKEN).ifPresent(
                clientPlayerKantaiDataCache -> {
                    int shipCount = clientPlayerKantaiDataCache.getListOfShips().size();
                    this.page = shipCount == 0 ? 0 : (shipCount % 4 == 0) ? shipCount / 4 - 1 : shipCount / 4;
                }
            );
        }
    }

    protected void refreshPageWidget() {
        this.currentWidgets.forEach(this::removeWidget);

        Minecraft.getInstance().player.getCapability(ClientPlayerKantaiDataCacheCapability.TOKEN).ifPresent(
                clientPlayerKantaiDataCache -> {
                    var listOfShips = clientPlayerKantaiDataCache.getListOfShips();
                    var kantaiDataShipUUID = new ArrayList<UUID>();
                    List<EntityShip> toRender = new ArrayList<>();
                    for (int i = this.page * 4; i < listOfShips.size() && i < this.page * 4 + 4; i++) {
                        toRender.add(listOfShips.get(i).getValue());
                        kantaiDataShipUUID.add(listOfShips.get(i).getKey());
                    }
                    int x = this.leftPos + 16;
                    int y = this.topPos + 14;
                    int widgetX = 56;
                    int widgetY = 120;
                    for (int i = 0; i < toRender.size(); i++) {
                        int finalI = i;
                        var widget = new ShipSelectButton(toRender.get(finalI), x + finalI * widgetX, y, widgetX, widgetY, getPreviewContent(toRender.get(finalI))) {
                            @Override
                            public void onPress() {
                                sendSummonPacketToServer(kantaiDataShipUUID.get(finalI), menu.player.getOnPos());
                            }
                        };
                        this.currentWidgets.add(widget);
                        this.addRenderableWidget(widget);
                    }
                }
        );

    }

    public void refresh() {
        this.refreshPageWidget();
    }

    private static void sendSummonPacketToServer(UUID summonUUID, BlockPos blockPos) {
        Minecraft.getInstance().player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(playerKantaiData -> {
            ModPacketHandler.INSTANCE.sendToServer(new CommandCenterRequestSummonPacket(summonUUID, blockPos));
        });
    }

}
