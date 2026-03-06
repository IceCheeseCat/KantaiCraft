package com.github.icecheesecat.kantaicraft.menu.ship;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.capability.equipment.EquipmentHandlerCapability;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.menu.IconWithTextElement;
import com.github.icecheesecat.kantaicraft.menu.pagescreen.Page;
import com.github.icecheesecat.kantaicraft.menu.pagescreen.PageScreen;
import com.github.icecheesecat.kantaicraft.network.packet.entityship.SyncType;
import com.github.icecheesecat.kantaicraft.registries.ModAttribute;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShipScreen extends PageScreen<ShipMenu> {

    private static final ResourceLocation SHIP_SCREEN_BACKGROUND = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_screen/ship_screen_background.png");
    private static final ResourceLocation HEART_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_screen/heart_icon.png");
    private static final ResourceLocation FIREPOWER_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_screen/firepower_icon.png");
    private static final ResourceLocation TORPEDO_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_screen/torpedo_icon.png");
    private static final ResourceLocation ANTIAIR_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_screen/antiair_icon.png");
    private static final ResourceLocation ASW_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_screen/asw_icon.png");
    private static final ResourceLocation GUARD_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_screen/guard_icon.png");
    private static final ResourceLocation MELEE_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_screen/melee_icon.png");
    private static final ResourceLocation WONDER_AROUND_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_screen/wonder_around_icon.png");
    private static final ResourceLocation SELECTED = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_screen/selected.png");
    private static final ResourceLocation NEXT_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_screen/next_icon.png");
    private static final ResourceLocation NEXT_ICON_HOVERED = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_screen/next_icon_hovered.png");
    private static final ResourceLocation PREV_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_screen/prev_icon.png");
    private static final ResourceLocation PREV_ICON_HOVERED = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_screen/prev_icon_hovered.png");
    private static final ResourceLocation INVENTORY_SLOTS = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_screen/ship_screen_inventory.png");

    private static final int EQUIPMENT_SLOT_SIZE = 4;

    private static final int ENTITY_MODEL_BACKGROUND = FastColor.ARGB32.color(200, 255, 255, 255);
    private static final int BACKGROUND_COLOR = FastColor.ARGB32.color(102, 0, 0, 0);
    private final EntityShip entityShip;
    private int SECTION_X;
    private int SECTION_Y;
    private int STATS_Y;
    private int SECTION_WIDTH;
    private int SECTION_HEIGHT;
    private int BUTTON_X;
    private int BUTTON_Y;
    private int MODEL_X;
    private int MODEL_Y;
    private int MODEL_SCALE;
    private int EQUIPMENT_SLOT_1_X;
    private int EQUIPMENT_SLOT_1_Y;
    private int EQUIPMENT_SLOT_2_X;
    private int EQUIPMENT_SLOT_2_Y;
    private int EQUIPMENT_SLOT_3_X;
    private int EQUIPMENT_SLOT_3_Y;
    private int EQUIPMENT_SLOT_4_X;
    private int EQUIPMENT_SLOT_4_Y;
    private PageTitleDisplayer pageTitleDisplayer;
    private AbstractButton nextPageButton;
    private AbstractButton prevPageButton;
    private GridLayout statsLayout;

    public ShipScreen(ShipMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.entityShip = this.getMenu().getEntityShip();
        this.addPage(this::createMainPage);
        this.addPage(this::createEquipmentPage);
    }

    private void initVar() {
        this.imageWidth = 256;
        this.imageHeight = 144;
        this.width = Minecraft.getInstance().screen.width;
        this.height = Minecraft.getInstance().screen.height;
        this.leftPos = 0;
        this.topPos = 0;

        SECTION_X = 0;
        SECTION_Y = (int) (this.height * 0.3125f);
        STATS_Y = (int) (this.height * 0.225f);
        BUTTON_X = this.leftPos + 11;
        BUTTON_Y = this.topPos + 13;
        SECTION_WIDTH = (int) (this.width * 1.0f);
        SECTION_HEIGHT = (int) (this.imageHeight * 0.9f);
        MODEL_X = this.width / 2;
        MODEL_Y = this.height + 40;
        MODEL_SCALE = 90;

        // init equipment slot positions
        EQUIPMENT_SLOT_1_X = (int) (this.width * 0.25f);
        EQUIPMENT_SLOT_2_X = (int) (this.width * 0.25f);
        EQUIPMENT_SLOT_3_X = (int) (this.width * 0.75f);
        EQUIPMENT_SLOT_4_X = (int) (this.width * 0.75f);

        EQUIPMENT_SLOT_1_Y = EQUIPMENT_SLOT_3_Y = (int) (this.height * 0.45f);
        EQUIPMENT_SLOT_2_Y = EQUIPMENT_SLOT_4_Y = EQUIPMENT_SLOT_1_Y + 65;

    }

    @Override
    protected void init() {
        initVar();
        super.init();

        if (this.statsLayout != null) {
            this.statsLayout.visitChildren(this.renderables::remove);
        }
        this.statsLayout = createStatLayout();

        // Next section button
        this.removeWidget(this.nextPageButton);
        this.nextPageButton = new CustomTextureButton((int) (this.width * 0.75f), SECTION_Y / 2, 16, 16, Component.empty(), NEXT_ICON, NEXT_ICON_HOVERED) {
            @Override
            public void onPress() {
                gotoNextPage();
            }
        };
        this.addRenderableWidget(this.nextPageButton);

        // Prev section button
        this.removeWidget(this.prevPageButton);
        this.prevPageButton = new CustomTextureButton((int) (this.width * 0.25f), SECTION_Y / 2, 16, 16, Component.empty(), PREV_ICON, PREV_ICON_HOVERED) {
            @Override
            public void onPress() {
                gotoPrevPage();
            }
        };
        this.addRenderableWidget(this.prevPageButton);


        this.renderables.remove(this.pageTitleDisplayer);
        this.pageTitleDisplayer = new PageTitleDisplayer(this.width/2, SECTION_Y/2 -3, 0, 0, 70) {
            @Override
            protected Component getCurrentPageTitle() {
                return ShipScreen.this.getCurrentPageTitle();
            }

            @Override
            protected Component getNextPageTitle() {
                return ShipScreen.this.getNextPageTitle();
            }

            @Override
            protected Component getPrevPageTitle() {
                return ShipScreen.this.getPrevPageTitle();
            }
        };
        this.addRenderableOnly(this.pageTitleDisplayer);

    }

    private Page createMainPage() {
        GridPage gridPage = new GridPage(Component.translatable("ship_screen_main_section"), SECTION_X, SECTION_Y, SECTION_WIDTH, SECTION_HEIGHT);
        gridPage.appendGridlayout(createControlLayout());
        return gridPage;
    }

    private Page createEquipmentPage() {
        Page page = new Page(Component.translatable("ship_screen_equipment_section"), SECTION_X, SECTION_Y, SECTION_WIDTH, SECTION_HEIGHT);
        page.addAllWidget(createEquipmentWidgets());

        return page;
    }

    private Page createInventoryPage() {
        InventoryPage screenSection = new InventoryPage(Component.translatable("ship_screen_inventory_section"), SECTION_X, SECTION_Y, SECTION_WIDTH, SECTION_HEIGHT, this.menu.slots);
        int playerX = this.leftPos - 154  - 20;
        int playerY = this.topPos + 3;
        int shipX = this.leftPos + 18;
        int shipY = this.topPos + 3;
        int blitOffset = 0;
        int white = FastColor.ARGB32.color(255, 255, 255, 255);

        Page.ImageDisplay playerInventoryDisplay = new Page.ImageDisplay(playerX, playerY, blitOffset, 154, 73, INVENTORY_SLOTS, 0.8f);
        Page.ImageDisplay shipInventoryDisplay = new Page.ImageDisplay(shipX, shipY, blitOffset,154, 73, INVENTORY_SLOTS, 0.8f);
        screenSection.addImageDisplay(playerInventoryDisplay);
        screenSection.addTextInstance(new Page.TextInstance(playerX, playerY-10, "Player Inventory", white));
        screenSection.addImageDisplay(shipInventoryDisplay);
        screenSection.addTextInstance(new Page.TextInstance(shipX, shipY-10, "Ship Inventory", white));

        return screenSection;
    }

    private GridLayout createControlLayout() {

        int x = SECTION_X + 10;
        int y = SECTION_Y + 100;
        GridLayout gridLayout = new GridLayout(x, y);
        gridLayout.defaultCellSetting().padding(4);
        int indexOfCell = 0;
        gridLayout.addChild(new SyncedBooleanWidget(0, 0, 32, 32, this.entityShip, EntityShip.DATA_IS_GUARDING,
                        GUARD_ICON, SELECTED, SyncType.GUARD),
                0, indexOfCell++);
        gridLayout.addChild(new SyncedBooleanWidget(0, 0, 32, 32, this.entityShip, EntityShip.DATA_FORCE_MELEE,
                        MELEE_ICON, SELECTED, SyncType.MELEE),
                0, indexOfCell++);
        gridLayout.addChild(new SyncedBooleanWidget(0, 0, 32, 32, this.entityShip, EntityShip.DATA_WONDER_AROUND,
                        WONDER_AROUND_ICON, SELECTED, SyncType.WONDER_AROUND),
                0, indexOfCell++);
        gridLayout.arrangeElements();

        return gridLayout;
    }

    private List<AbstractWidget> createEquipmentWidgets() {
        List<AbstractWidget> widgets = new ArrayList<>();

        this.entityShip.getCapability(EquipmentHandlerCapability.TOKEN).ifPresent(equipmentHandler -> {

            widgets.add(0, new ArmedEquipmentWidget(EQUIPMENT_SLOT_1_X - 30, EQUIPMENT_SLOT_1_Y - 30, 60, 60, equipmentHandler.getArmedEquipment(0)));
            widgets.add(1, new ArmedEquipmentWidget(EQUIPMENT_SLOT_2_X - 30, EQUIPMENT_SLOT_2_Y - 30, 60, 60, equipmentHandler.getArmedEquipment(1)));
            widgets.add(2, new ArmedEquipmentWidget(EQUIPMENT_SLOT_3_X - 30, EQUIPMENT_SLOT_3_Y - 30, 60, 60, equipmentHandler.getArmedEquipment(2)));
            widgets.add(3, new ArmedEquipmentWidget(EQUIPMENT_SLOT_4_X - 30, EQUIPMENT_SLOT_4_Y - 30, 60, 60, equipmentHandler.getArmedEquipment(3)));

        });
        return widgets;
    }



    private GridLayout createStatLayout() {
        GridLayout gridLayout = new GridLayout(0, 0);
        gridLayout.defaultCellSetting().padding(0);

        int index = 0;
        gridLayout.addChild(new IconWithTextElement(0, 0, HEART_ICON, 4, String.valueOf(this.entityShip.getHealth())), 0, index++);
        gridLayout.addChild(new IconWithTextElement(0, 0, FIREPOWER_ICON, 4, String.valueOf(this.entityShip.getAttributeValue(ModAttribute.FIREPOWER.get()))), 0, index++);
        gridLayout.addChild(new IconWithTextElement(0, 0, TORPEDO_ICON, 4, String.valueOf(this.entityShip.getAttributeValue(ModAttribute.TORPEDO.get()))), 0, index++);
        gridLayout.addChild(new IconWithTextElement(0, 0, ANTIAIR_ICON, 4, String.valueOf(this.entityShip.getAttributeValue(ModAttribute.ANTIAIR.get()))), 0, index++);
        gridLayout.addChild(new IconWithTextElement(0, 0, ASW_ICON, 4, String.valueOf(this.entityShip.getAttributeValue(ModAttribute.ASW.get()))), 0, index++);
        gridLayout.arrangeElements();

        int gridWidth = gridLayout.getWidth();
        int gridHeight = gridLayout.getHeight();
        gridLayout.setX(this.width/2 - gridWidth/2);
        gridLayout.setY(STATS_Y);
        gridLayout.visitChildren(layoutElement -> this.addRenderableOnly((Renderable) layoutElement));

        return gridLayout;

    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float partialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, partialTick);
        this.renderEntityModel(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.blit(SHIP_SCREEN_BACKGROUND, 0, 0, -100, 0, 0, Minecraft.getInstance().screen.width, Minecraft.getInstance().screen.height, Minecraft.getInstance().screen.width, Minecraft.getInstance().screen.height);
    }

    private void renderEntityModel(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int eyeY = (int) (MODEL_Y - MODEL_SCALE );
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, -50, -500);
        InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, MODEL_X, MODEL_Y, MODEL_SCALE, MODEL_X - mouseX, eyeY - mouseY, this.entityShip);
        guiGraphics.pose().popPose();
    }

}
