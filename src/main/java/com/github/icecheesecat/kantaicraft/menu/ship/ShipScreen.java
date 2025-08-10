package com.github.icecheesecat.kantaicraft.menu.ship;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.capability.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.capability.EquipmentHandlerCapability;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicCannonShip;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.entity.ship.CannonFireMode;
import com.github.icecheesecat.kantaicraft.network.Cache.Cache;
import com.github.icecheesecat.kantaicraft.registries.ModAttribute;
import com.github.icecheesecat.kantaicraft.network.packet.SyncType;
import com.github.icecheesecat.kantaicraft.equipment.Equipments;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.GridLayout;
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

//    private static final ResourceLocation BACKGROUND_1 = new ResourceLocation(KantaiCraft.MODID, "textures/gui/ship_menu_background_1.png");
    private static final ResourceLocation FIREPOWER_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/firepower_icon.png");
    private static final ResourceLocation TORPEDO_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/torpedo_icon.png");
    private static final ResourceLocation ANTIAIR_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/antiair_icon.png");
    private static final ResourceLocation ASW_ICON = new ResourceLocation(KantaiCraft.MODID, "textures/gui/asw_icon.png");
    private static final ResourceLocation TEST_256x256_0 = new ResourceLocation(KantaiCraft.MODID, "textures/gui/test_256x256_0.png");
    private static final ResourceLocation TEST_256x256_1 = new ResourceLocation(KantaiCraft.MODID, "textures/gui/test_256x256_1.png");
    private static final ResourceLocation CAN_MELEE_FALSE = new ResourceLocation(KantaiCraft.MODID, "textures/gui/can_melee_false.png");
    private static final ResourceLocation CAN_MELEE_TRUE = new ResourceLocation(KantaiCraft.MODID, "textures/gui/can_melee_true.png");
    private static final ResourceLocation ROUND_ROBIN = new ResourceLocation(KantaiCraft.MODID, "textures/gui/round_robin.png");
    private static final ResourceLocation VOLLEY = new ResourceLocation(KantaiCraft.MODID, "textures/gui/volley.png");

    private static final int ENTITY_MODEL_BACKGROUND = FastColor.ARGB32.color(200, 255, 255, 255);

    private int SECTION_X;
    private int SECTION_Y;
    private int SECTION_WIDTH;
    private int SECTION_HEIGHT;
    private int ENTITY_MODEL_X;
    private int ENTITY_MODEL_Y;
    private int ENTITY_MODEL_CENTER_X;
    private int ENTITY_MODEL_CENTER_Y;
    private int ENTITY_MODEL_WIDTH;
    private int ENTITY_MODEL_HEIGHT;

    private static final int BACKGROUND = FastColor.ARGB32.color(102, 0, 0, 0);

    private final BasicEntityShip ship;

    private ScreenSection TOGGLE_FEATURE_SECTION;
    private ScreenSection TOGGLE_EQUIPMENT_SECTION;
    private ScreenSection TOGGLE_STATS_SECTION;
    private ScreenSection TOGGLE_FACTION_SECTION;

    private SectionSelector featureSS;
    private SectionSelector equipmentSS;
    private SectionSelector statsSS;
    private SectionSelector factionSS;
    private final SectionManager sectionManager = new SectionManager();
    GridLayout gridLayout;

    EquipmentHandler equipmentHandler;
    private List<SelectionWidget> tempSelectionWidgets = new ArrayList<>();
    boolean isSelectDirty = false;

    public ShipScreen(ShipMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.imageWidth = 320;
        this.imageHeight = 180;
        this.ship = this.getMenu().getEntityShip();
        this.ship.getCapability(EquipmentHandlerCapability.TOKEN).ifPresent(
            handler -> {
                this.equipmentHandler = handler;
            }
        );
    }

    @Override
    protected void init() {
        super.init();
        initVar();

        gridLayout = new GridLayout(SECTION_X, SECTION_Y - 10);
        gridLayout.defaultCellSetting().padding(4);

        TOGGLE_FEATURE_SECTION = new ScreenSection(Component.translatable("toggle_feature_section_screen_section"), SECTION_X, SECTION_Y, SECTION_WIDTH, SECTION_HEIGHT);
            TOGGLE_FEATURE_SECTION.addWidget(new SyncedWidget<>(50, 100, 32, 32, this.ship, BasicEntityShip.DATA_IS_GUARDING,
                    ImmutableMap.of(false, TEST_256x256_0, true, TEST_256x256_1), SyncType.GUARD, (b) -> !b));
            TOGGLE_FEATURE_SECTION.addWidget(new SyncedWidget<>(82, 100, 32, 32, this.ship, BasicEntityShip.DATA_CAN_MELEE,
                    ImmutableMap.of(false, CAN_MELEE_FALSE, true, CAN_MELEE_TRUE), SyncType.MELEE, (b) -> !b));
            if (this.ship instanceof BasicCannonShip cannonShip) {
                TOGGLE_FEATURE_SECTION.addWidget(new SyncedWidget<>(114, 100, 32, 32, this.ship, BasicCannonShip.CANNON_FIRE_MODE,
                        ImmutableMap.of(CannonFireMode.ROUND_ROBIN, ROUND_ROBIN, CannonFireMode.VOLLEY, VOLLEY), SyncType.CANNON_FIRE_MODE, CannonFireMode::getNext));
            }
            TOGGLE_FEATURE_SECTION.widgets.forEach(widget -> this.addRenderableWidget(widget));

        TOGGLE_EQUIPMENT_SECTION = new ScreenSection(Component.translatable("toggle_feature_section_screen_section"), SECTION_X, SECTION_Y, SECTION_WIDTH, SECTION_HEIGHT);
            TOGGLE_EQUIPMENT_SECTION.addWidget(new EquipmentWidget(SECTION_X + 10, SECTION_Y + 10, this.ship, 0));
            TOGGLE_EQUIPMENT_SECTION.addWidget(new EquipmentWidget(SECTION_X + 10, SECTION_Y + 10 + 42 , this.ship, 1));
            TOGGLE_EQUIPMENT_SECTION.addWidget(new EquipmentWidget(SECTION_X + 10, SECTION_Y + 10 + 42 + 42, this.ship, 2));
            TOGGLE_EQUIPMENT_SECTION.addWidget(new EquipmentWidget(SECTION_X + 10, SECTION_Y + 10 + 42 + 42 + 42 , this.ship, 3));
            TOGGLE_EQUIPMENT_SECTION.widgets.forEach(this::addRenderableWidget);

        TOGGLE_STATS_SECTION = new ScreenSection(Component.translatable("toggle_stats_section_screen_section"), SECTION_X, SECTION_Y, SECTION_WIDTH, SECTION_HEIGHT);
            TextGridLayout statsTextGridLayout = createStatsTextGrid();
                statsTextGridLayout.arrangeElements();
            TOGGLE_STATS_SECTION.setTextGridLayout(statsTextGridLayout);

        TOGGLE_FACTION_SECTION = new FactionSection(Component.translatable("toggle_faction_section_screen_section"), SECTION_X, SECTION_Y, SECTION_WIDTH, SECTION_HEIGHT, this.ship, FactionResourcesHelper.create(this.ship.getFactionId()));


        featureSS = new SectionSelector(0, 0, 20, 10, Component.translatable("featuress"), TOGGLE_FEATURE_SECTION);
        equipmentSS = new SectionSelector(0, 0, 20, 10, Component.translatable("equipmentss"), TOGGLE_EQUIPMENT_SECTION);
        statsSS = new SectionSelector(0, 0, 20, 10, Component.translatable("statsSS"), TOGGLE_STATS_SECTION);
        factionSS = new SectionSelector(0, 0, 20, 10, Component.translatable("factionSS"), TOGGLE_FACTION_SECTION);


        setupSectionSelector(0, featureSS, TOGGLE_FEATURE_SECTION, false);
        setupSectionSelector(1, equipmentSS, TOGGLE_EQUIPMENT_SECTION, false);
        setupSectionSelector(2, statsSS, TOGGLE_STATS_SECTION, true);
        setupSectionSelector(3, factionSS, TOGGLE_FACTION_SECTION, false);
        gridLayout.arrangeElements();
        gridLayout.visitWidgets(this::addRenderableWidget);

    }

    private void initVar() {
        SECTION_X = 10;
        SECTION_Y = this.height - (int) (this.height * 0.85) - 10;
        SECTION_WIDTH = (int) (this.width * 0.6d);
        SECTION_HEIGHT = (int) (this.height * 0.85);
        ENTITY_MODEL_X = SECTION_X + SECTION_WIDTH;
        ENTITY_MODEL_Y = SECTION_Y;
        ENTITY_MODEL_WIDTH = (int) (this.width * 0.3d);
        ENTITY_MODEL_HEIGHT = (int) (this.height * 0.85);
        ENTITY_MODEL_CENTER_X = ENTITY_MODEL_X + ENTITY_MODEL_WIDTH / 2;
        ENTITY_MODEL_CENTER_Y = ENTITY_MODEL_Y + ENTITY_MODEL_HEIGHT / 2;
    }

    private void setupSectionSelector(int num, SectionSelector ss, ScreenSection screenSection, boolean isSelected) {
        gridLayout.addChild(ss, 0, num);
        if (isSelected) {
            this.sectionManager.addSectionAndSetSelected(ss, screenSection);
        }
        else {
            this.sectionManager.addSection(ss, screenSection);
        }
    }

    private @NotNull TextGridLayout createStatsTextGrid() {
        TextGridLayout textGridLayout = new TextGridLayout(SECTION_X, SECTION_Y, 10, 40);
        textGridLayout.addChild(() -> String.valueOf(this.ship.getAttributeValue(ModAttribute.FIREPOWER.get())), 0, 0);
        textGridLayout.addChild(() -> String.valueOf(this.ship.getAttributeValue(ModAttribute.TORPEDO.get())), 0, 1);
        textGridLayout.addChild(() -> String.valueOf(this.ship.getAttributeValue(ModAttribute.ANTIAIR.get())), 0, 2);
        textGridLayout.addChild(() -> String.valueOf(this.ship.getAttributeValue(ModAttribute.ASW.get())), 0, 3);
        return textGridLayout;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        sectionManager.controlSections(pMouseX, pMouseY, pButton);

        tempSelectionWidgets.forEach((w) -> w.mouseClicked(pMouseX, pMouseY, pButton));
        tempSelectionWidgets.forEach(this::removeWidget);
        tempSelectionWidgets.clear();

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
//        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.fill(0, 0, this.width, this.height, -1000, BACKGROUND);
        this.renderEntityWithBg(guiGraphics, mouseX, mouseY);
        this.sectionManager.renderAll(guiGraphics, mouseX, mouseY, partialTick);

        for (var r: renderables) {
            r.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        updateWidget();
    }

    private void renderEntityWithBg(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, ENTITY_MODEL_CENTER_X, ENTITY_MODEL_CENTER_Y + 30, 40, ENTITY_MODEL_CENTER_X - mouseX, ENTITY_MODEL_CENTER_Y - mouseY, this.ship);
        guiGraphics.fill(ENTITY_MODEL_X, ENTITY_MODEL_Y, ENTITY_MODEL_X + ENTITY_MODEL_WIDTH, ENTITY_MODEL_Y + ENTITY_MODEL_HEIGHT, -999, ENTITY_MODEL_BACKGROUND);
    }

    private void updateWidget() {
        if (isSelectDirty) {
            if (Cache.selectionEntityId == this.ship.getId()) {
                this.createSelectionWidget();
            }

            isSelectDirty = false;
        }
    }

    private void createSelectionWidget() {
        EquipmentWidget widget = (EquipmentWidget) TOGGLE_EQUIPMENT_SECTION.widgets.get(Cache.selectionIndexCache);
        List<SelectionWidget> widgets = new ArrayList<>();
        for (int i = 0; i < Cache.selectionCache.size(); i++) {
            widgets.add(new SelectionWidget(widget.getX() + 220, widget.getY() + 32 * i, Equipments.getEquipmentInstanceById(Cache.selectionCache.get(i), 0), this.ship, Cache.selectionIndexCache));
        }

        for (var w: widgets) {
            this.addRenderableWidget(w);
        }
        this.tempSelectionWidgets.addAll(widgets);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {

    }

    public boolean isSelectDirty() {
        return isSelectDirty;
    }

    public void setSelectDirty(boolean selectDirty) {
        isSelectDirty = selectDirty;
    }

    public ScreenSection getEquipmentSection() {
        return TOGGLE_EQUIPMENT_SECTION;
    }

    public ScreenSection getFeatureSection() {
        return TOGGLE_FEATURE_SECTION;
    }
}
