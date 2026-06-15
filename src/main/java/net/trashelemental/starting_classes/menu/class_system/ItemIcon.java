package net.trashelemental.starting_classes.menu.class_system;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record ItemIcon(ResourceLocation itemId) implements ClassIcon {

    public static final Codec<ItemIcon> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("itemId").forGetter(ItemIcon::itemId)
            ).apply(instance, ItemIcon::new));

}