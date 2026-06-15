package net.trashelemental.starting_classes.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.trashelemental.starting_classes.StartingClasses;
import net.trashelemental.starting_classes.menu.class_system.ClassIconRenderer;
import net.trashelemental.starting_classes.menu.class_system.StartingClassData;

import java.awt.*;
import java.util.function.Consumer;

public class ClassSelectionButton extends AbstractWidget {

    private static final ResourceLocation CLASS_BUTTON = StartingClasses.prefix("textures/gui/class_button.png");
    private static final ResourceLocation HOVERED = StartingClasses.prefix("textures/gui/class_button_hovered.png");
    private static final ResourceLocation SELECTED = StartingClasses.prefix("textures/gui/class_button_selected.png");

    private static final int TEXTURE_WIDTH = 137;
    private static final int TEXTURE_HEIGHT = 40;
    private static final float GUI_SCALE = 0.6F;

    float titleScale = 0.7F;
    float sourceScale = 0.5F;

    private final StartingClassData classData;
    private final Consumer<StartingClassData> onPress;
    private boolean selected = false;

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private final int indexInList;

    public int getIndexInList() {
        return indexInList;
    }

    public ClassSelectionButton(int x, int y, int width, int height, StartingClassData classData, Consumer<StartingClassData> onPress, int indexInList) {
        super(x, y, width, height, classData.name());
        this.classData = classData;
        this.onPress = onPress;
        this.indexInList = indexInList;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(getX(), getY(), 0.0F);
        guiGraphics.pose().scale(GUI_SCALE, GUI_SCALE, 1.0F);

        if (selected) {
            guiGraphics.blit(SELECTED, 0, 0, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        } else if (!isHoveredOrFocused()) {
            guiGraphics.blit(CLASS_BUTTON, 0, 0, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        } else {
            guiGraphics.blit(HOVERED, 0, 0, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        }

        guiGraphics.pose().popPose();

        ClassIconRenderer.render(guiGraphics, classData.icon(), getX() + 3, getY() + 3);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(titleScale, titleScale, 1.0F);

        Font font = minecraft.font;

        String title = classData.name().getString();

        int maxWidth = 70;

        if (font.width(title) > maxWidth) {
            title = font.plainSubstrByWidth(title, maxWidth - font.width("...")) + "...";
        }

        guiGraphics.drawString(
                minecraft.font,
                title,
                (int)((getX() + 22) / titleScale),
                (int)((getY() + 8) / titleScale),
                0xFFFFFF,
                false
        );

        guiGraphics.pose().popPose();

        // Source text

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(sourceScale, sourceScale, 1.0F);

        guiGraphics.drawString(
                minecraft.font,
                classData.source(),
                (int)((getX() + 22) / sourceScale),
                (int)((getY() + 14) / sourceScale),
                0xAAAAAA,
                false
        );

        guiGraphics.pose().popPose();
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        playDownSound(Minecraft.getInstance().getSoundManager());
        onPress.accept(classData);
    }

    public StartingClassData getClassData() {
        return classData;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narration) {
        narration.add(NarratedElementType.TITLE, classData.name());
    }

    @Override
    public void setPosition(int pX, int pY) {
        super.setPosition(pX, pY);
    }
}
