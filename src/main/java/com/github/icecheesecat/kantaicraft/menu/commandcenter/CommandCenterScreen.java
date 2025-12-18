package com.github.icecheesecat.kantaicraft.menu.commandcenter;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.capability.ClientPlayerKantaiDataCacheCapability;
import com.github.icecheesecat.kantaicraft.capability.PlayerKantaiDataCapability;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.menu.ClientPlayerKantaiDataRefresh;
import com.github.icecheesecat.kantaicraft.menu.FlipPageCounter;
import com.github.icecheesecat.kantaicraft.menu.pagescreen.Page;
import com.github.icecheesecat.kantaicraft.menu.pagescreen.PageScreen;
import com.github.icecheesecat.kantaicraft.menu.ship.CustomTextureButton;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.commandcenter.CommandCenterRequestSummonPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class CommandCenterScreen extends PageScreen<CommandCenterMenu> implements ClientPlayerKantaiDataRefresh {

    private static final ResourceLocation COMMAND_CENTER_BACKGROUND = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/command_center_background.png");
    private static final ResourceLocation NEXT_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/next_icon.png");
    private static final ResourceLocation NEXT_HOVERED_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/next_icon_hovered.png");
    private static final ResourceLocation PREV_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/prev_icon.png");
    private static final ResourceLocation PREV_ICON_HOVERED = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/prev_icon_hovered.png");
    private static final ResourceLocation SHIP_PAGE = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/ship_page.png");
    private static final ResourceLocation EQUIPMENT_PAGE = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/equipment_page.png");
    private static final ResourceLocation EQUIPMENT_DISPLAY_BUTTON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/equipment_display_button.png");
    private static final ResourceLocation EQUIPMENT_DISPLAY_HOVERED = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/equipment_display_button.png");
    protected FlipPageCounter shipPageCounter = new FlipPageCounter(shipPageSize);
    protected FlipPageCounter equipmentPageCounter = new FlipPageCounter(equipmentPageSize);
    private static final int shipPageSize = 4;
    private static final int equipmentPageSize = 28;

    public CommandCenterScreen(CommandCenterMenu pMenu, Inventory pPlayerInventory, Component title) {
        super(pMenu, pPlayerInventory, title);
        this.imageWidth = 256;
        this.imageHeight = 149;
        this.addPage(this::createShipPage);
        this.addPage(this::createEquipmentPage);
    }

    @Override
    protected void init() {
        super.init();
    }

    protected Page createShipPage() {
        Page page = new Page(Component.translatable("command_center_screen.ship"), this.leftPos + 16, this.topPos + 14, 224, 120);

        page.addAllWidget(createShipWidgets());
        page.addWidget(createGotoEquipmentPageButton());
        page.addAllWidget(createFlipShipPageCountButton());
        AtomicInteger totalPageNumber = new AtomicInteger();
        Minecraft.getInstance().player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
                playerKantaiData -> {
                    totalPageNumber.set(shipPageCounter.getTotalPageNumber(playerKantaiData.getShips().size()));
                }
        );
        page.addTextInstance(new Page.TextInstance(this.leftPos + this.imageWidth - 30, this.topPos + this.imageHeight - 13, (this.shipPageCounter.getPageNumber() + 1) + "/" + (totalPageNumber.get() + 1), FastColor.ARGB32.color(255, 255, 255, 255)));

        return page;
    }

    protected List<AbstractWidget> createShipWidgets() {
        List<AbstractWidget> widgets = new ArrayList<>();

        Minecraft.getInstance().player.getCapability(ClientPlayerKantaiDataCacheCapability.TOKEN).ifPresent(
                clientPlayerKantaiDataCache -> {
                    var listOfShips = clientPlayerKantaiDataCache.getListOfShips();
                    List<UUID> kantaiDataShipUUID = this.shipPageCounter.evaluatePageElements(listOfShips.stream().map(Map.Entry::getKey).toList());
                    List<EntityShip> toRender = this.shipPageCounter.evaluatePageElements(listOfShips.stream().map(Map.Entry::getValue).toList());

                    int x = this.leftPos + 16;
                    int y = this.topPos + 14;
                    int widgetX = 56;
                    int widgetY = 120;
                    for (int i = 0; i < toRender.size(); i++) {
                        int finalI = i;
                        var widget = new ShipSelectButton(toRender.get(finalI), x + finalI * widgetX, y, widgetX, widgetY, getShipPreviewContent(toRender.get(finalI))) {
                            @Override
                            public void onPress() {
                                sendSummonPacketToServer(kantaiDataShipUUID.get(finalI), menu.player.getOnPos());
                                Minecraft.getInstance().player.sendSystemMessage(Component.literal("Pressed ship select"));
                            }
                        };
                        widgets.add(widget);
                    }
                }
        );

        return widgets;
    }

    protected List<AbstractWidget> createFlipShipPageCountButton() {

        List<AbstractWidget> widgets = new ArrayList<>();
        widgets.add( new CustomTextureButton(this.leftPos + 5, this.topPos + this.imageHeight /2, 16, 16, Component.empty(), PREV_ICON, PREV_ICON_HOVERED) {
            @Override
            public void onPress() {
                Minecraft.getInstance().player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
                        playerKantaiData -> {
                            shipPageCounter.flipToPrevPage(playerKantaiData.getShips().size());
                        }
                );
                refresh();
            }
        });

        widgets.add( new CustomTextureButton(this.leftPos + this.imageWidth - 5, this.topPos + this.imageHeight / 2, 16, 16, Component.empty(), NEXT_ICON, NEXT_HOVERED_ICON) {
            @Override
            public void onPress() {
                Minecraft.getInstance().player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
                        playerKantaiData -> {
                            shipPageCounter.flipToNextPage(playerKantaiData.getShips().size());
                        }
                );
                refresh();
            }
        });

        return widgets;
    }

    protected Page createEquipmentPage() {
        Page page = new Page(Component.translatable("command_center_screen.equipment"), this.leftPos + 16, this.topPos + 14, 224, 120);

        page.addAllWidget(createEquipmentWidgets());
        page.addWidget(createGotoShipPageButton());
        page.addAllWidget(createFlipEquipmentPageButton());
        AtomicInteger totalPageNumber = new AtomicInteger();
        Minecraft.getInstance().player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
                playerKantaiData -> {
                    totalPageNumber.set(equipmentPageCounter.getTotalPageNumber(playerKantaiData.getEquipments().size()));
                }
        );
        page.addTextInstance(new Page.TextInstance(this.leftPos + this.imageWidth - 30, this.topPos + this.imageHeight - 13, (this.equipmentPageCounter.getPageNumber() + 1) + "/" + (totalPageNumber.get() + 1), FastColor.ARGB32.color(255, 255, 255, 255)));


        return page;
    }

    protected List<AbstractWidget> createEquipmentWidgets() {
        List<AbstractWidget> widgets = new ArrayList<>();

        Minecraft.getInstance().player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(playerKantaiData -> {

            GridLayout gridLayout = new GridLayout(this.leftPos + 16, this.topPos + 14);
            List<Equipment> toRender = equipmentPageCounter.evaluatePageElements(playerKantaiData.getEquipments());

            boolean flag = true;
            for (int i = 0; ; i++) {
                for (int j = 0; j < 7 ; j++) {
                    int index = i * 7 + j;
                    if (index >= toRender.size()) {
                        flag = false;
                        break;
                    }
                    EquipmentDisplayWidget widget = new EquipmentDisplayWidget(0,0, 32,30, EQUIPMENT_DISPLAY_BUTTON, toRender.get(i)) {
                        @Override
                        public EquipmentDetailPage createPage() {
                            return new EquipmentDetailPage(Component.translatable("equipment_detail_page"), leftPos + 16, topPos + 14, imageWidth - 32, imageHeight - 28, this.equipment);
                        }
                    };
                    gridLayout.addChild(widget, i, j);
                }
                if (!flag) break;
            }

            gridLayout.arrangeElements();
            gridLayout.visitWidgets(widgets::add);
        });


        return widgets;
    }

    protected List<AbstractWidget> createFlipEquipmentPageButton() {
        List<AbstractWidget> widgets = new ArrayList<>();
        widgets.add( new CustomTextureButton(this.leftPos + 5, this.topPos + this.imageHeight /2, 16, 16, Component.empty(), PREV_ICON, PREV_ICON_HOVERED) {
            @Override
            public void onPress() {
                Minecraft.getInstance().player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
                        playerKantaiData -> {
                            equipmentPageCounter.flipToPrevPage(playerKantaiData.getEquipments().size());
                        }
                );
                refresh();
            }
        });

        widgets.add( new CustomTextureButton(this.leftPos + this.imageWidth - 5, this.topPos + this.imageHeight / 2, 16, 16, Component.empty(), NEXT_ICON, NEXT_HOVERED_ICON) {
            @Override
            public void onPress() {
                Minecraft.getInstance().player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
                        playerKantaiData -> {
                            equipmentPageCounter.flipToNextPage(playerKantaiData.getEquipments().size());
                        }
                );
                refresh();
            }
        });

        return widgets;
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

    protected AbstractButton createGotoEquipmentPageButton() {
        return new CustomTextureButton(this.leftPos, this.topPos, 32, 32, Component.empty(), EQUIPMENT_PAGE) {
            @Override
            public void onPress() {
                jumpToPage(1);
            }
        };
    }

    protected AbstractButton createGotoShipPageButton() {
        return new CustomTextureButton(this.leftPos, this.topPos, 32, 32, Component.empty(), SHIP_PAGE) {
            @Override
            public void onPress() {
                jumpToPage(0);
            }
        };
    }



    public static List<Component> getShipPreviewContent(EntityShip entityShip) {

        return List.of(Component.translatable(entityShip.getName().getString()), Component.literal(String.valueOf(entityShip.getShipLevel())));

    }

    private static void sendSummonPacketToServer(UUID summonUUID, BlockPos blockPos) {
        Minecraft.getInstance().player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(playerKantaiData -> {
            ModPacketHandler.INSTANCE.sendToServer(new CommandCenterRequestSummonPacket(summonUUID, blockPos));
        });
    }

}
