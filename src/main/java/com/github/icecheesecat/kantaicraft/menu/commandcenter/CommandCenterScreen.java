package com.github.icecheesecat.kantaicraft.menu.commandcenter;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.capability.kantaidata.PlayerKantaiDataCapability;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.menu.ClientPlayerKantaiDataRefresh;
import com.github.icecheesecat.kantaicraft.menu.FlipPageCounter;
import com.github.icecheesecat.kantaicraft.menu.pagescreen.Page;
import com.github.icecheesecat.kantaicraft.menu.pagescreen.PageScreen;
import com.github.icecheesecat.kantaicraft.menu.ship.CustomTextureButton;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.commandcenter.DispatchEntityShipPacket;
import com.github.icecheesecat.kantaicraft.network.packet.commandcenter.NearbyOwnedShipsPacket;
import com.github.icecheesecat.kantaicraft.network.packet.commandcenter.RetrieveEntityShipPacket;
import com.github.icecheesecat.kantaicraft.util.SerializedLivingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandCenterScreen extends PageScreen<CommandCenterMenu> implements ClientPlayerKantaiDataRefresh {

    private static final ResourceLocation COMMAND_CENTER_SHIP_PAGE = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/command_center_ship_page.png");
//    private static final ResourceLocation NEXT_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/next_icon.png");
//    private static final ResourceLocation NEXT_HOVERED_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/next_icon_hovered.png");
//    private static final ResourceLocation PREV_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/prev_icon.png");
//    private static final ResourceLocation PREV_ICON_HOVERED = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/prev_icon_hovered.png");
    private static final ResourceLocation SHIP_NEXT_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/ship_next_icon.png");
    private static final ResourceLocation SHIP_NEXT_ICON_HOVERED = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/ship_next_icon_hovered.png");
    private static final ResourceLocation SHIP_PREV_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/ship_prev_icon.png");
    private static final ResourceLocation SHIP_PREV_ICON_HOVERED = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/ship_prev_icon_hovered.png");
//    private static final ResourceLocation SHIP_PAGE = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/ship_page.png");
//    private static final ResourceLocation EQUIPMENT_PAGE = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/equipment_page.png");
//    private static final ResourceLocation EQUIPMENT_DISPLAY_BUTTON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/equipment_display_button.png");
//    private static final ResourceLocation EQUIPMENT_DISPLAY_HOVERED = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/equipment_display_button.png");
    private static final int shipPageSize = 6;
    private static final int equipmentPageSize = 28;
    protected FlipPageCounter inDockCounter = new FlipPageCounter(shipPageSize);
    protected FlipPageCounter onDutyCounter = new FlipPageCounter(shipPageSize);
//    protected FlipPageCounter equipmentPageCounter = new FlipPageCounter(equipmentPageSize);
    private int SHIP_SELECT_ON_DUTY_X;
    private int SHIP_SELECT_ON_DUTY_Y;
    private int SHIP_SELECT_IN_DOCK_X;
    private int SHIP_SELECT_IN_DOCK_Y;
    private int SHIP_SELECT_PADDING = 20;
    private int SHIP_SELECT_WIDTH = 110;
    private int SHIP_SELECT_HEIGHT = 20;

    private int ON_DUTY_PAGE_COUNT_X;
    private int ON_DUTY_PAGE_COUNT_Y;
    private int IN_DOCK_PAGE_COUNT_X;
    private int IN_DOCK_PAGE_COUNT_Y;
    private final List<EntityShip> nearbyShips = new ArrayList<>();
    private int ON_DUTY_PAGE_COUNT_Y_DOWN;
    private int IN_DOCK_PAGE_COUNT_Y_DOWN;

    public CommandCenterScreen(CommandCenterMenu pMenu, Inventory pPlayerInventory, Component title) {
        super(pMenu, pPlayerInventory, title);
        this.imageWidth = 256;
        this.imageHeight = 149;
        this.addPage(this::createShipPage);

        syncFromServerNearbyShips();
    }

    @Override
    protected void initVar() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        this.SHIP_SELECT_ON_DUTY_X = this.leftPos + 16;
        this.SHIP_SELECT_ON_DUTY_Y = this.topPos + 14;
        this.SHIP_SELECT_IN_DOCK_X = this.leftPos + 130;
        this.SHIP_SELECT_IN_DOCK_Y = this.topPos + 14;
        this.ON_DUTY_PAGE_COUNT_X = this.leftPos + 71;
        this.ON_DUTY_PAGE_COUNT_Y = this.topPos + 8;
        this.ON_DUTY_PAGE_COUNT_Y_DOWN = ON_DUTY_PAGE_COUNT_Y + 132;
        this.IN_DOCK_PAGE_COUNT_X = this.leftPos + 185;
        this.IN_DOCK_PAGE_COUNT_Y = ON_DUTY_PAGE_COUNT_Y;
        this.IN_DOCK_PAGE_COUNT_Y_DOWN = ON_DUTY_PAGE_COUNT_Y_DOWN;

    }

    @Override
    protected void init() {
        super.init();
    }

    private void syncFromServerNearbyShips() {
        ModPacketHandler.INSTANCE.sendToServer(new NearbyOwnedShipsPacket.Request(menu.dispatchLocation, 32));
    }

    protected Page createShipPage() {
        Page page = new Page(Component.translatable("command_center_screen.ship"), this.leftPos + 16, this.topPos + 14, 224, 120);

        page.addAllWidget(createInDockWidgets());
        page.addAllWidget(createOnDutyWidgets());
        page.addAllWidget(createInDockCountButton());
        page.addAllWidget(createOnDutyCountButton());

//        AtomicInteger totalPageNumber = new AtomicInteger();
//        Minecraft.getInstance().player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
//                playerKantaiData -> {
//                    totalPageNumber.set(inDockCounter.getTotalPageNumber(playerKantaiData.getInDockShips().size()));
//                }
//        );
//        page.addTextInstance(new Page.TextInstance(this.leftPos + this.imageWidth - 30, this.topPos + this.imageHeight - 13, (this.inDockCounter.getPageNumber() + 1) + "/" + (totalPageNumber.get() + 1), FastColor.ARGB32.color(255, 255, 255, 255)));

        return page;
    }

    protected List<AbstractWidget> createInDockWidgets() {
        List<AbstractWidget> widgets = new ArrayList<>();
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(playerKantaiData -> {

                List<SerializedLivingEntity> serializedLivingEntities = this.inDockCounter.evaluatePageElements(playerKantaiData.getInDockShips());

                for (int i = 0; i < serializedLivingEntities.size(); i++) {
                    var shipSelectButton = new ShipSelectButton(this.SHIP_SELECT_IN_DOCK_X,
                            this.SHIP_SELECT_IN_DOCK_Y + i * SHIP_SELECT_PADDING,
                            this.SHIP_SELECT_WIDTH,
                            this.SHIP_SELECT_HEIGHT,
                            serializedLivingEntities.get(i),
                            COMMAND_CENTER_SHIP_PAGE) {

                        @Override
                        public void onPress() {
                            dispatchInDockShip(serializedLivingEntity.getUuid(), menu.dispatchLocation);
                            syncFromServerNearbyShips();
                        }
                    };

                    widgets.add(shipSelectButton);
                }

            });
        }

        return widgets;
    }

    private void dispatchInDockShip(UUID uuid, BlockPos blockPos) {
        ModPacketHandler.INSTANCE.sendToServer(new DispatchEntityShipPacket(uuid, blockPos));
    }

    protected List<AbstractWidget> createOnDutyWidgets() {
        List<AbstractWidget> widgets = new ArrayList<>();
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(playerKantaiData -> {

                List<EntityShip> entityShips = this.onDutyCounter.evaluatePageElements(this.nearbyShips);

                for (int i = 0; i < entityShips.size(); i++) {
                    var shipSelectButton = new ShipSelectButton(this.SHIP_SELECT_ON_DUTY_X,
                            this.SHIP_SELECT_ON_DUTY_Y + i * SHIP_SELECT_PADDING,
                            this.SHIP_SELECT_WIDTH,
                            this.SHIP_SELECT_HEIGHT,
                            new SerializedLivingEntity(entityShips.get(i)),
                            COMMAND_CENTER_SHIP_PAGE) {

                        @Override
                        public void onPress() {
                            retrieveOnDutyShip(menu.player.getId(), serializedLivingEntity.getUuid());
                            syncFromServerNearbyShips();
                        }
                    };

                    widgets.add(shipSelectButton);
                }

            });
        }

        return widgets;
    }

    private void retrieveOnDutyShip(int playerId, UUID uuid) {
        ModPacketHandler.INSTANCE.sendToServer(new RetrieveEntityShipPacket(playerId, uuid));
    }

    protected List<AbstractWidget> createInDockCountButton() {

        List<AbstractWidget> widgets = new ArrayList<>();
        widgets.add( new CustomTextureButton(this.IN_DOCK_PAGE_COUNT_X, this.IN_DOCK_PAGE_COUNT_Y, 16, 16, Component.empty(), SHIP_PREV_ICON, SHIP_PREV_ICON_HOVERED) {
            @Override
            public void onPress() {
                Minecraft.getInstance().player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
                        playerKantaiData -> {
                            inDockCounter.flipToPrevPage(playerKantaiData.getInDockShips().size());
                        }
                );
                refresh();
            }
        });

        widgets.add( new CustomTextureButton(this.IN_DOCK_PAGE_COUNT_X, this.IN_DOCK_PAGE_COUNT_Y_DOWN, 16, 16, Component.empty(), SHIP_NEXT_ICON, SHIP_NEXT_ICON_HOVERED) {
            @Override
            public void onPress() {
                Minecraft.getInstance().player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
                        playerKantaiData -> {
                            inDockCounter.flipToNextPage(playerKantaiData.getInDockShips().size());
                        }
                );
                refresh();
            }
        });

        return widgets;
    }

    protected List<AbstractWidget> createOnDutyCountButton() {

        List<AbstractWidget> widgets = new ArrayList<>();
        widgets.add( new CustomTextureButton(this.ON_DUTY_PAGE_COUNT_X, this.ON_DUTY_PAGE_COUNT_Y, 16, 16, Component.empty(), SHIP_PREV_ICON, SHIP_PREV_ICON_HOVERED) {
            @Override
            public void onPress() {
                onDutyCounter.flipToPrevPage(nearbyShips.size());
                refresh();
            }
        });

        widgets.add( new CustomTextureButton(this.ON_DUTY_PAGE_COUNT_X, this.ON_DUTY_PAGE_COUNT_Y_DOWN, 16, 16, Component.empty(), SHIP_NEXT_ICON, SHIP_NEXT_ICON_HOVERED) {
            @Override
            public void onPress() {
                onDutyCounter.flipToNextPage(nearbyShips.size());
                refresh();
            }
        });

        return widgets;
    }

//    protected Page createEquipmentPage() {
//        Page page = new Page(Component.translatable("command_center_screen.equipment"), this.leftPos + 16, this.topPos + 14, 224, 120);
//
//        page.addAllWidget(createEquipmentWidgets());
//        page.addWidget(createGotoShipPageButton());
//        page.addAllWidget(createFlipEquipmentPageButton());
//        AtomicInteger totalPageNumber = new AtomicInteger();
//        Minecraft.getInstance().player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
//                playerKantaiData -> {
//                    totalPageNumber.set(equipmentPageCounter.getTotalPageNumber(playerKantaiData.getEquipments().size()));
//                }
//        );
//        page.addTextInstance(new Page.TextInstance(this.leftPos + this.imageWidth - 30, this.topPos + this.imageHeight - 13, (this.equipmentPageCounter.getPageNumber() + 1) + "/" + (totalPageNumber.get() + 1), FastColor.ARGB32.color(255, 255, 255, 255)));
//
//
//        return page;
//    }

//    protected List<AbstractWidget> createEquipmentWidgets() {
//        List<AbstractWidget> widgets = new ArrayList<>();
//
//        Minecraft.getInstance().player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(playerKantaiData -> {
//
//            GridLayout gridLayout = new GridLayout(this.leftPos + 16, this.topPos + 14);
//            List<Equipment> toRender = equipmentPageCounter.evaluatePageElements(playerKantaiData.getEquipments());
//
//            boolean flag = true;
//            for (int i = 0; ; i++) {
//                for (int j = 0; j < 7 ; j++) {
//                    int index = i * 7 + j;
//                    if (index >= toRender.size()) {
//                        flag = false;
//                        break;
//                    }
//                    EquipmentDisplayWidget widget = new EquipmentDisplayWidget(0,0, 32,30, EQUIPMENT_DISPLAY_BUTTON, toRender.get(index)) {
//                        @Override
//                        public EquipmentDetailPage createPage() {
//                            return new EquipmentDetailPage(Component.translatable("equipment_detail_page"), leftPos + 16, topPos + 14, imageWidth - 32, imageHeight - 28, this.equipment);
//                        }
//                    };
//                    gridLayout.addChild(widget, i, j);
//                }
//                if (!flag) break;
//            }
//
//            gridLayout.arrangeElements();
//            gridLayout.visitWidgets(widgets::add);
//        });
//
//
//        return widgets;
//    }

//    protected List<AbstractWidget> createFlipEquipmentPageButton() {
//        List<AbstractWidget> widgets = new ArrayList<>();
//        widgets.add( new CustomTextureButton(this.leftPos + 5, this.topPos + this.imageHeight /2, 16, 16, Component.empty(), PREV_ICON, PREV_ICON_HOVERED) {
//            @Override
//            public void onPress() {
//                Minecraft.getInstance().player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
//                        playerKantaiData -> {
//                            equipmentPageCounter.flipToPrevPage(playerKantaiData.getEquipments().size());
//                        }
//                );
//                refresh();
//            }
//        });
//
//        widgets.add( new CustomTextureButton(this.leftPos + this.imageWidth - 5, this.topPos + this.imageHeight / 2, 16, 16, Component.empty(), NEXT_ICON, NEXT_HOVERED_ICON) {
//            @Override
//            public void onPress() {
//                Minecraft.getInstance().player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
//                        playerKantaiData -> {
//                            equipmentPageCounter.flipToNextPage(playerKantaiData.getEquipments().size());
//                        }
//                );
//                refresh();
//            }
//        });
//
//        return widgets;
//    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBackground(pGuiGraphics);
        int imageWidth = 256;
        int imageHeight = 149;
        this.renderWithOriginalSize(COMMAND_CENTER_SHIP_PAGE, pGuiGraphics, imageWidth, imageHeight);
    }

//    protected AbstractButton createGotoEquipmentPageButton() {
//        return new CustomTextureButton(this.leftPos, this.topPos, 32, 32, Component.empty(), EQUIPMENT_PAGE) {
//            @Override
//            public void onPress() {
//                jumpToPage(1);
//            }
//        };
//    }
//
//    protected AbstractButton createGotoShipPageButton() {
//        return new CustomTextureButton(this.leftPos, this.topPos, 32, 32, Component.empty(), SHIP_PAGE) {
//            @Override
//            public void onPress() {
//                jumpToPage(0);
//            }
//        };
//    }

    public void setNearbyShips(List<Integer> entitiesId) {
        this.nearbyShips.clear();
        if (Minecraft.getInstance().level != null) {
            for (var id: entitiesId) {
                if (Minecraft.getInstance().level.getEntity(id) instanceof EntityShip entityShip) {
                    this.nearbyShips.add(entityShip);
                }
            }
        }
    }

}
