package com.github.icecheesecat.kantaicraft.menu.ship;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.capability.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.github.icecheesecat.kantaicraft.menu.IconWithTextElement;
import com.github.icecheesecat.kantaicraft.registries.ModAttribute;
import com.github.icecheesecat.kantaicraft.network.packet.SyncType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShipScreen extends AbstractContainerScreen<ShipMenu> {

    private static final ResourceLocation SHIP_SCREEN_BACKGROUND = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_screen_background.png");
    private static final ResourceLocation HEART_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/heart_icon.png");
    private static final ResourceLocation FIREPOWER_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/firepower_icon.png");
    private static final ResourceLocation TORPEDO_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/torpedo_icon.png");
    private static final ResourceLocation ANTIAIR_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/antiair_icon.png");
    private static final ResourceLocation ASW_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/asw_icon.png");
    private static final ResourceLocation GUARD_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/guard_icon.png");
    private static final ResourceLocation MELEE_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/melee_icon.png");
    private static final ResourceLocation SELECTED = new ResourceLocation(KantaiCraft.MODID, "textures/gui/selected.png");
    private static final ResourceLocation NEXT_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/next_icon.png");
    private static final ResourceLocation NEXT_ICON_HOVERED = new ResourceLocation(KantaiCraft.MODID, "textures/gui/next_icon_hovered.png");
    private static final ResourceLocation PREV_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/prev_icon.png");
    private static final ResourceLocation PREV_ICON_HOVERED = new ResourceLocation(KantaiCraft.MODID, "textures/gui/prev_icon_hovered.png");
    private static final ResourceLocation INVENTORY_SLOTS = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_screen_inventory.png");


    private static final int ENTITY_MODEL_BACKGROUND = FastColor.ARGB32.color(200, 255, 255, 255);

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

    private static final int BACKGROUND_COLOR = FastColor.ARGB32.color(102, 0, 0, 0);

    private final EntityShip entityShip;

    private GridLayout controlLayout;
    private GridLayout statLayout;
    private GridLayout equipmentLayout;
    private final SectionManager sectionManager = new SectionManager();
    private SectionSelectDisplayer sectionSelectDisplayer;
    EquipmentHandler equipmentHandler;
    private List<SelectionWidget> tempSelectionWidgets = new ArrayList<>();

    public ShipScreen(ShipMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.entityShip = this.getMenu().getEntityShip();
//        this.ship.getCapability(EquipmentHandlerCapability.TOKEN).ifPresent(
//            handler -> {
//                this.equipmentHandler = handler;
//            }
//        );

    }

    private void initVar() {
        this.imageWidth = 256;
        this.imageHeight = 144;
        this.width = Minecraft.getInstance().screen.width;
        this.height = Minecraft.getInstance().screen.height;
        this.leftPos = (int) (this.width * 0.5f);
        this.topPos = (int) (this.height * 0.5f);

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

    }

    @Override
    protected void init() {
        initVar();
        this.sectionManager.clear();
        var mainSection = createMainSection();
        var equipmentSection = createEquipmentSection();
        var inventorySection = createInventorySection();
        this.sectionManager.addMainSection(mainSection);
        this.sectionManager.addSection(equipmentSection);
        this.sectionManager.addSection(inventorySection);
        this.sectionManager.check();

        this.statLayout = createStatLayout();

        // Next section button
        this.addRenderableWidget(new SectionButton((int) (this.width * 0.75f), SECTION_Y / 2, 16, 16, Component.empty(), NEXT_ICON, NEXT_ICON_HOVERED, sectionManager) {
            @Override
            public void onPress() {
                this.sectionManager.nextSection();
            }
        });

        // Prev section button
        this.addRenderableWidget(new SectionButton((int) (this.width * 0.25f), SECTION_Y / 2, 16, 16, Component.empty(), PREV_ICON, PREV_ICON_HOVERED, sectionManager) {
            @Override
            public void onPress() {
                this.sectionManager.prevSection();
            }
        });

        this.sectionSelectDisplayer = new SectionSelectDisplayer(sectionManager, this.width/2, SECTION_Y/2 -3, 0, 0, 70);
        this.addRenderableOnly(this.sectionSelectDisplayer);

    }

    private ScreenSection createMainSection() {
        this.controlLayout = createControlLayout();
        GridSection gridSection = new GridSection(Component.translatable("ship_screen_main_section"), SECTION_X, SECTION_Y, SECTION_WIDTH, SECTION_HEIGHT);
        gridSection.appendGridlayout(this.controlLayout);
        return gridSection;
    }

    private ScreenSection createEquipmentSection() {
        this.equipmentLayout = createEquipmentLayout();
        GridSection gridSection = new GridSection(Component.translatable("ship_screen_equipment_section"), SECTION_X, SECTION_Y, SECTION_WIDTH, SECTION_HEIGHT);
        gridSection.appendGridlayout(this.equipmentLayout);

        return gridSection;
    }

    private ScreenSection createInventorySection() {
        InventorySection screenSection = new InventorySection(Component.translatable("ship_screen_inventory_section"), SECTION_X, SECTION_Y, SECTION_WIDTH, SECTION_HEIGHT, this.menu.slots);
        int playerX = this.leftPos - 154  - 20;
        int playerY = this.topPos + 3;
        int shipX = this.leftPos + 18;
        int shipY = this.topPos + 3;
        int blitOffset = 0;
        int white = FastColor.ARGB32.color(255, 255, 255, 255);

        ScreenSection.ImageDisplay playerInventoryDisplay = new ScreenSection.ImageDisplay(playerX, playerY, blitOffset, 154, 73, INVENTORY_SLOTS, 0.8f);
        ScreenSection.ImageDisplay shipInventoryDisplay = new ScreenSection.ImageDisplay(shipX, shipY, blitOffset,154, 73, INVENTORY_SLOTS, 0.8f);
        screenSection.addImageDisplay(playerInventoryDisplay);
        screenSection.addTextInstance(new ScreenSection.TextInstance(playerX, playerY-10, "Player Inventory", white));
        screenSection.addImageDisplay(shipInventoryDisplay);
        screenSection.addTextInstance(new ScreenSection.TextInstance(shipX, shipY-10, "Ship Inventory", white));

        return screenSection;
    }

    private GridLayout createControlLayout() {

        int x = SECTION_X + 10;
        int y = SECTION_Y + 100;
        GridLayout gridLayout = new GridLayout(x, y);
        gridLayout.defaultCellSetting().padding(4);
        int indexOfCell = 0;
        gridLayout.addChild(new SyncedWidget<>(0, 0, 32, 32, this.entityShip, EntityShip.DATA_IS_GUARDING,
                        ImmutableMap.of(false, GUARD_ICON, true, GUARD_ICON), SyncType.GUARD, (b) -> !b),
                0, indexOfCell++);
        gridLayout.addChild(new SyncedWidget<>(0, 0, 32, 32, this.entityShip, EntityShip.DATA_FORCE_MELEE,
                        ImmutableMap.of(false, MELEE_ICON, true, MELEE_ICON), SyncType.MELEE, (b) -> !b),
                0, indexOfCell++);
        gridLayout.arrangeElements();
        gridLayout.visitWidgets(this::addRenderableWidget);

        return gridLayout;
    }

    private GridLayout createEquipmentLayout() {

        int x = SECTION_X;
        int y = SECTION_Y;
        GridLayout gridLayout = new GridLayout(x, y);
        gridLayout.defaultCellSetting().paddingRight(4);
        gridLayout.defaultCellSetting().paddingBottom(4);
        int index = 0;
//        gridLayout.addChild(new EquipmentWidget(SECTION_X, SECTION_Y, this.ship, 0), index++, 0);
//        gridLayout.addChild(new EquipmentWidget(SECTION_X, SECTION_Y, this.ship, 1), index++, 0);
//        gridLayout.addChild(new EquipmentWidget(SECTION_X, SECTION_Y, this.ship, 2), index++, 0);
//        gridLayout.addChild(new EquipmentWidget(SECTION_X, SECTION_Y, this.ship, 3), index++, 0);
        gridLayout.arrangeElements();
        gridLayout.visitWidgets(this::addRenderableWidget);

        return gridLayout;
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
        gridLayout.visitChildren(this::addRenderableLayoutElement);
        gridLayout.visitChildren((le) -> {
            if (le instanceof IconWithTextElement iconWithTextElement) {
                iconWithTextElement.setShow(true);
            }
        });

        int gridWidth = gridLayout.getWidth();
        int gridHeight = gridLayout.getHeight();
        gridLayout.setX(this.width/2 - gridWidth/2);
        gridLayout.setY(STATS_Y);

        return gridLayout;

    }

    public void addRenderableLayoutElement(LayoutElement layoutElement) {
        if (layoutElement instanceof Renderable renderable) {
            this.renderables.add(renderable);
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {

        tempSelectionWidgets.forEach((w) -> w.mouseClicked(pMouseX, pMouseY, pButton));
        tempSelectionWidgets.forEach(this::removeWidget);
        tempSelectionWidgets.clear();

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float partialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, partialTick);
//        this.renderBg(pGuiGraphics, partialTick, pMouseX, pMouseY);
        this.renderEntityModel(pGuiGraphics, pMouseX, pMouseY);
//        this.renderables.forEach(renderable -> renderable.render(pGuiGraphics, pMouseX, pMouseY, partialTick));
        sectionManager.render(pGuiGraphics, pMouseX, pMouseY, partialTick);
        renderOnControl(pGuiGraphics);

    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
//        super.renderLabels(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.blit(SHIP_SCREEN_BACKGROUND, 0, 0, -100, 0, 0, Minecraft.getInstance().screen.width, Minecraft.getInstance().screen.height, Minecraft.getInstance().screen.width, Minecraft.getInstance().screen.height);
    }

    private void renderEntityModel(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int eyeY = (int) (MODEL_Y - MODEL_SCALE );
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, -500);
        InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, MODEL_X, MODEL_Y, MODEL_SCALE, MODEL_X - mouseX, eyeY - mouseY, this.entityShip);
        guiGraphics.pose().popPose();
    }

    private void renderOnControl(GuiGraphics pGuiGraphics) {
        this.controlLayout.visitWidgets(control -> {
            if (!control.visible) return;
            if (!control.active) return;
            if (control instanceof SyncedWidget<?> syncedWidget) {
                if (syncedWidget.controlOn()) {
                    pGuiGraphics.blit(SELECTED, syncedWidget.getX(), syncedWidget.getY(), 0, 0, 32, 32, 32, 32);
                }
            }
        });
    }

    private void updateWidget() {
    }

}
