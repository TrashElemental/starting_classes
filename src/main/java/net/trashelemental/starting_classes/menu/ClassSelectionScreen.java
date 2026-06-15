package net.trashelemental.starting_classes.menu;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.trashelemental.starting_classes.StartingClasses;
import net.trashelemental.starting_classes.client.SelectClassPacket;
import net.trashelemental.starting_classes.client.StartingClassesNetworking;
import net.trashelemental.starting_classes.menu.class_system.*;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassSelectionScreen extends Screen {

    private static final ResourceLocation CLASS_PANEL = StartingClasses.prefix("textures/gui/class_selection_panel.png");
    private static final ResourceLocation BOOK_PANEL = StartingClasses.prefix("textures/gui/book_panel.png");
    private static final ResourceLocation ICON_BACKGROUND = StartingClasses.prefix("textures/gui/icon_background.png");

    private static final int CLASS_TEXTURE_WIDTH = 334;
    private static final int CLASS_TEXTURE_HEIGHT = 334;

    private static final int CLASS_PANEL_WIDTH = 176;
    private static final int CLASS_PANEL_HEIGHT = 334;

    private static final int BOOK_TEXTURE_WIDTH = 334;
    private static final int BOOK_TEXTURE_HEIGHT = 334;

    private static final float GUI_SCALE = 0.6F;

    private static final int BOOK_CONTENT_LEFT = 30;
    private static final int BOOK_CONTENT_WIDTH = 160;

    private static final int ICON_Y = 20;
    private static final int TITLE_Y = 74;

    private static final int DESCRIPTION_Y = 115;
    private static final int DESCRIPTION_WIDTH = 180;

    private int classPanelX;
    private int classPanelY;

    private int bookPanelX;
    private int bookPanelY;

    private static final int BOOK_TEXT_COLOR = 0x3E3449;

    private static ItemStack hoveredEquipmentStack = ItemStack.EMPTY;

    private int bookScaledY(int y) {
        return getBookScreenY() + (int)(y * GUI_SCALE);
    }

    private StartingClassData selectedClass;
    private int currentPage = 0;
    private static final Map<ChoiceItemEntry, Integer> selectedChoices = new HashMap<>();
    private ClassListWidget classListWidget;
    private final List<Button> choiceButtons = new ArrayList<>();
    private Button nextPageButton;
    private Button previousPageButton;

    public ClassSelectionScreen() {
        super(Component.literal("Starting Classes"));

        StartingClassManager.onClassesLoaded(classes -> {
            if (!classes.isEmpty()) {
                this.selectedClass = classes.get(0);
            }
        });
    }

    @Override
    protected void init() {
        float scale = GUI_SCALE;

        classPanelX = (int)((this.width / scale) / 2 - CLASS_PANEL_WIDTH);
        classPanelY = (int)((this.height / scale - CLASS_PANEL_HEIGHT) / 2);

        bookPanelX = classPanelX + CLASS_PANEL_WIDTH;
        bookPanelY = classPanelY;

        List<StartingClassData> classes = StartingClassManager.getClasses();

        if (classes.isEmpty()) {
            this.addRenderableWidget(new Button.Builder(Component.literal("No classes available"), (button) -> {})
                    .bounds(this.width / 2 - 100, this.height / 2, 200, 20)
                    .build());
            return;
        }

        classListWidget = new ClassListWidget(
                (int)(classPanelX * GUI_SCALE + 8),
                (int)(classPanelY * GUI_SCALE + 8),
                150,
                188
        );

        classListWidget.setClasses(
                StartingClassManager.getClasses(),
                selectedClass,
                selected -> {
                    selectedClass = selected;
                    currentPage = 0;
                    syncChoiceDefaults(selected);
                    rebuildUI();
                }
        );

        this.addRenderableWidget(classListWidget);

        int bookWidth = this.width - 140 - 20;
        int buttonWidth = 120;
        int buttonHeight = 20;
        int buttonX = 140 + (bookWidth / 2) - (buttonWidth / 2);
        int buttonY = this.height - 60;

        this.addRenderableWidget(
                Button.builder(Component.literal("Start With This Class"),
                                button -> {
                                    ClassSelectionData data = buildSelectionData();
                                    StartingClassesNetworking.CHANNEL.sendToServer(new SelectClassPacket(data.classId(), data.choiceIndices()));

                                    Minecraft.getInstance().setScreen(null);
                                })
                        .bounds(buttonX, buttonY, buttonWidth, buttonHeight)
                        .build()
        );

        previousPageButton = Button.builder(
                        Component.literal("←"),
                        button -> {
                            currentPage = 0;
                            playPageTurnSound();
                            rebuildUI();
                        }
                )
                .bounds(140 + 218, this.height - 140, 20, 20)
                .build();

        this.addRenderableWidget(previousPageButton);

        nextPageButton = Button.builder(
                        Component.literal("→"),
                        button -> {
                            currentPage = 1;
                            playPageTurnSound();
                            rebuildUI();
                        }
                )
                .bounds(140 + 218, this.height - 140, 20, 20)
                .build();

        this.addRenderableWidget(nextPageButton);

        rebuildUI();
        updatePageButtons();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        this.renderBackground(guiGraphics);

        if (selectedClass == null) {
                guiGraphics.drawCenteredString(font, "No classes loaded", this.width / 2, this.height / 2, 0xFFFFFF);
            return;
        }

        renderPanels(guiGraphics);

        if (currentPage == 0) {
            renderDescriptionPage(guiGraphics);
        } else {
            renderEquipmentPage(this, guiGraphics, selectedClass, this.font, mouseX, mouseY);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        if (!hoveredEquipmentStack.isEmpty()) {
            guiGraphics.renderTooltip(font, hoveredEquipmentStack, mouseX, mouseY);
        }

    }

    private void renderPanels(GuiGraphics guiGraphics) {
        float scale = GUI_SCALE;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scale, scale, 1.0F);

        guiGraphics.blit(
                CLASS_PANEL,
                classPanelX,
                classPanelY,
                0,
                0,
                CLASS_TEXTURE_WIDTH,
                CLASS_TEXTURE_HEIGHT,
                CLASS_TEXTURE_WIDTH,
                CLASS_TEXTURE_HEIGHT
        );

        guiGraphics.blit(
                BOOK_PANEL,
                bookPanelX,
                bookPanelY,
                0,
                0,
                BOOK_TEXTURE_WIDTH,
                BOOK_TEXTURE_HEIGHT,
                BOOK_TEXTURE_WIDTH,
                BOOK_TEXTURE_HEIGHT
        );

        guiGraphics.pose().popPose();
    }

    private int getBookScreenX() {
        return (int)(bookPanelX * GUI_SCALE);
    }

    private int getBookScreenY() {
        return (int)(bookPanelY * GUI_SCALE);
    }

    private void renderDescriptionPage(GuiGraphics guiGraphics) {

        int bookScreenX = getBookScreenX() - 5;
        int bookScreenY = getBookScreenY();

        int pageCenterX =
                bookScreenX +
                        (int)((BOOK_CONTENT_LEFT + (float) BOOK_CONTENT_WIDTH / 2) * GUI_SCALE);

        // Icon box

        int iconBoxX = pageCenterX - (24 / 2);
        int iconBoxY = bookScreenY + (int)(ICON_Y * GUI_SCALE);

        guiGraphics.blit(
                ICON_BACKGROUND,
                iconBoxX,
                iconBoxY,
                0,
                0,
                24,
                24,
                24,
                24
        );

        // Icon

        int itemX = iconBoxX + 4;
        int itemY = iconBoxY + 4;

        ClassIconRenderer.render(
                guiGraphics,
                selectedClass.icon(),
                itemX,
                itemY
        );

        //Title
        String title = selectedClass.name().getString();

        int maxWidth = (int)(BOOK_CONTENT_WIDTH * GUI_SCALE);
        int titleWidth = font.width(title);

        boolean wrapTitle = false;
        float titleScale = 1.0F;

        if (titleWidth > maxWidth) {

            float neededScale = (float) maxWidth / titleWidth;

            if (neededScale >= 0.75F) {
                titleScale = neededScale;
            } else {
                wrapTitle = true;
            }
        }

        int titleY = bookScaledY(TITLE_Y);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(titleScale, titleScale, 1.0F);

        if (!wrapTitle) {

            int titleX = (int)(
                    (pageCenterX / titleScale)
                            - (titleWidth / 2.0F)
            );

            guiGraphics.drawString(
                    font,
                    title,
                    titleX,
                    (int)(titleY / titleScale),
                    BOOK_TEXT_COLOR,
                    false
            );

        } else {

            List<FormattedCharSequence> titleLines = font.split(
                    Component.literal(title),
                    maxWidth
            );

            int lineY = (int)(titleY / titleScale);

            if (titleLines.size() > 1) {
                lineY -= (font.lineHeight + 2) / 2;
            }

            for (FormattedCharSequence line : titleLines) {

                int lineWidth = font.width(line);

                int lineX = (int)(
                        (pageCenterX / titleScale)
                                - (lineWidth / 2.0F)
                );

                guiGraphics.drawString(
                        font,
                        line,
                        lineX,
                        lineY,
                        BOOK_TEXT_COLOR,
                        false
                );

                lineY += font.lineHeight + 2;
            }
        }

        guiGraphics.pose().popPose();

        //Description

        String description = selectedClass.description().getString();

        int descriptionX = getBookScreenX() + (int)(BOOK_CONTENT_LEFT * GUI_SCALE);
        int descriptionY = bookScaledY(DESCRIPTION_Y);

        List<FormattedCharSequence> lines =
                font.split(
                        Component.literal(description),
                        (int)(DESCRIPTION_WIDTH * GUI_SCALE)
                );

        float textScale = 0.8F;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(textScale, textScale, 1.0F);

        int scaledX = (int)(descriptionX / textScale);
        int scaledY = (int)(descriptionY / textScale);

        for (FormattedCharSequence line : lines) {
            guiGraphics.drawString(
                    font,
                    line,
                    scaledX,
                    scaledY,
                    BOOK_TEXT_COLOR,
                    false
            );

            scaledY += (int)((font.lineHeight + 2) / textScale);
        }

        guiGraphics.pose().popPose();

    }

    private static void renderEquipmentPage(Screen screen, GuiGraphics guiGraphics, StartingClassData selectedClass, Font font, int mouseX, int mouseY) {

        hoveredEquipmentStack = ItemStack.EMPTY;

        int bookX = 140;
        int startX = bookX + 120;
        int startY = 80;

        int slotSpacing = 20;

        guiGraphics.drawString(font, "Starting Equipment", startX - 5, 50, 0x000000, false);

        List<EquipmentEntry> items = selectedClass.equipment();

        for (int i = 0; i < items.size(); i++) {
            int row = i / 4;
            int column = i % 4;
            int x = startX + (column * slotSpacing);
            int y = startY + (row * slotSpacing);

            EquipmentEntry entry = items.get(i);
            ItemStack stack = getDisplayStack(entry);

            guiGraphics.renderItem(stack, x, y);
            guiGraphics.renderItemDecorations(font, stack, x, y);

            if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
                hoveredEquipmentStack = stack;
            }
        }
    }

    private static ItemStack getDisplayStack(EquipmentEntry entry) {

        if (entry instanceof FixedItemEntry fixed) {
            return fixed.item().createStack();
        }

        if (entry instanceof ChoiceItemEntry choice) {

            if (choice.options().isEmpty()) {
                return ItemStack.EMPTY;
            }

            int index = selectedChoices.getOrDefault(choice, 0);
            index = Mth.clamp(index, 0, choice.options().size() - 1);

            return choice.options().get(index).createStack();
        }

        return ItemStack.EMPTY;
    }

    private void syncChoiceDefaults(StartingClassData clazz) {
        selectedChoices.clear();

        for (EquipmentEntry entry : clazz.equipment()) {
            if (entry instanceof ChoiceItemEntry choice) {
                selectedChoices.put(choice, 0);
            }
        }
    }

    private void cycleChoice(ChoiceItemEntry choice) {
        int current = selectedChoices.getOrDefault(choice, 0);

        int next = current + 1;
        if (next >= choice.options().size()) {
            next = 0;
        }

        selectedChoices.put(choice, next);
    }

    private void buildChoiceButtons() {
        if (selectedClass == null) return;

        List<EquipmentEntry> items = selectedClass.equipment();

        int bookX = 140;
        int startX = bookX + 110;
        int startY = 80;
        int slotSpacing = 20;

        for (int i = 0; i < items.size(); i++) {

            EquipmentEntry entry = items.get(i);
            if (!(entry instanceof ChoiceItemEntry choice)) continue;

            int row = i / 4;
            int column = i % 4;

            int x = startX + (column * slotSpacing);
            int y = startY + (row * slotSpacing);

            int btnX = x + 18;
            int btnY = y + 6;

            Button btn = Button.builder(Component.literal("⇆").withStyle(ChatFormatting.BOLD),
                    b -> {cycleChoice(choice);}).bounds(btnX, btnY, 10, 10).build();

            this.addRenderableWidget(btn);
            choiceButtons.add(btn);
        }
    }

    private void rebuildUI() {
        for (Button button : choiceButtons) {
            this.removeWidget(button);
        }

        choiceButtons.clear();

        buildChoiceButtons();

        updateChoiceButtonVisibility();

        updatePageButtons();

        if (!this.children().contains(classListWidget)) {
            this.addRenderableWidget(classListWidget);
        }
    }

    private void updateChoiceButtonVisibility() {

        boolean equipmentPage = currentPage == 1;

        for (Button button : choiceButtons) {
            button.visible = equipmentPage;
            button.active = equipmentPage;
        }
    }

    private void playPageTurnSound() {
        Minecraft.getInstance().getSoundManager().play(
                SimpleSoundInstance.forUI(
                        SoundEvents.BOOK_PAGE_TURN,
                        1.0F
                )
        );
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {

        if (classListWidget != null &&
                classListWidget.mouseScrolled(mouseX, mouseY, delta)) {
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    private void updatePageButtons() {

        nextPageButton.visible = currentPage == 0;
        nextPageButton.active = currentPage == 0;

        previousPageButton.visible = currentPage == 1;
        previousPageButton.active = currentPage == 1;
    }

    private ClassSelectionData buildSelectionData() {

        List<Integer> choices = new ArrayList<>();

        for (EquipmentEntry entry : selectedClass.equipment()) {

            if (entry instanceof ChoiceItemEntry choice) {
                choices.add(selectedChoices.getOrDefault(choice, 0));
            }
        }

        return new ClassSelectionData(
                selectedClass.id(),
                choices
        );
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
