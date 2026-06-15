package net.trashelemental.starting_classes.menu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.trashelemental.starting_classes.StartingClasses;
import net.trashelemental.starting_classes.menu.class_system.StartingClassData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ClassListWidget extends AbstractWidget {

    private static final ResourceLocation SCROLL_THUMB = StartingClasses.prefix("textures/gui/scroll_thumb.png");

    private final List<ClassSelectionButton> buttons = new ArrayList<>();
    private int scrollOffset = 0;
    private static final int BUTTON_HEIGHT = 24;
    private StartingClassData selectedClass;

    public ClassListWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int clipX = getX();
        int clipY = getY();
        int clipWidth = getX() + width;
        int clipHeight = getY() + height;

        guiGraphics.enableScissor(clipX, clipY, clipWidth, clipHeight);

        for (ClassSelectionButton button : buttons) {
            int buttonY = getY() + (button.getIndexInList() * BUTTON_HEIGHT) - scrollOffset;

            if (buttonY + BUTTON_HEIGHT > getY() && buttonY < getY() + height) {
                button.setPosition(getX(), buttonY);
                button.render(guiGraphics, mouseX, mouseY, partialTick);
            }
        }

        guiGraphics.disableScissor();

        renderScrollbar(guiGraphics);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (!isMouseOver(mouseX, mouseY)) {
            return false;
        }

        scrollOffset -= (int)(delta * 12);
        scrollOffset = Mth.clamp(scrollOffset, 0, getMaxScroll());

        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY)) {
            return false;
        }

        for (ClassSelectionButton classButton : buttons) {
            int buttonY = getY() + (classButton.getIndexInList() * BUTTON_HEIGHT) - scrollOffset;
            classButton.setPosition(getX(), buttonY);

            if (classButton.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narration) {}

    public void setClasses(List<StartingClassData> classes, StartingClassData selected, Consumer<StartingClassData> onSelect) {
        this.selectedClass = selected;
        buttons.clear();

        for (int i = 0; i < classes.size(); i++) {
            StartingClassData classData = classes.get(i);
            ClassSelectionButton button = new ClassSelectionButton(0, 0, 82, 24, classData,
                            selectedClass -> {
                                setSelectedClass(selectedClass);
                                onSelect.accept(selectedClass);

                            },
                            i
                    );
            setSelectedClass(selected);
            buttons.add(button);
        }
    }

    private int getMaxScroll() {
        if (buttons.isEmpty()) {
            return 0;
        }

        int contentHeight = buttons.size() * BUTTON_HEIGHT;
        return Math.max(0, contentHeight - height);
    }

    private void renderScrollbar(GuiGraphics guiGraphics) {
        int maxScroll = getMaxScroll();

        if (maxScroll <= 0) {
            return;
        }

        int trackX = getX() + width - 66;
        int thumbHeight = 24;
        int thumbTravel = height - thumbHeight;
        int thumbY = getY() + (int)((scrollOffset / (float)maxScroll) * thumbTravel);

        guiGraphics.blit(SCROLL_THUMB, trackX, thumbY, 0, 0, 6, thumbHeight, 6, thumbHeight);
    }

    public void setSelectedClass(StartingClassData selectedClass) {
        this.selectedClass = selectedClass;

        for (ClassSelectionButton button : buttons) {
            button.setSelected(
                    button.getClassData() == selectedClass
            );
        }
    }
}