package net.trashelemental.starting_classes.menu.class_system;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ClassIconRenderer {

    public static void render(GuiGraphics guiGraphics, ClassIcon icon, int x, int y) {

        if (icon instanceof ItemIcon itemIcon) {
            Item item = BuiltInRegistries.ITEM.get(itemIcon.itemId());

            guiGraphics.renderItem(new ItemStack(item), x, y);
        }
        else if (icon instanceof TextureIcon textureIcon) {
            guiGraphics.blit(textureIcon.texture(), x, y, 0, 0, 16, 16, 16, 16);
        }
    }
}
