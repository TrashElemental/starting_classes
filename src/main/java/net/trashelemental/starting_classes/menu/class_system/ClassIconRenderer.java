package net.trashelemental.starting_classes.menu.class_system;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ClassIconRenderer {

    public static void render(GuiGraphics guiGraphics, ClassIcon icon, int x, int y) {

        if (icon instanceof ItemIcon itemIcon) {
            Item item = ForgeRegistries.ITEMS.getValue(
                    itemIcon.itemId()
            );

            if (item != null) {
                guiGraphics.renderItem(new ItemStack(item), x, y);
            }
        }
        else if (icon instanceof TextureIcon textureIcon) {
            guiGraphics.blit(textureIcon.texture(), x, y, 0, 0, 16, 16, 16, 16);
        }
    }
}
