package net.trashelemental.starting_classes.menu.class_system;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record TextureIcon(ResourceLocation texture) implements ClassIcon {

    public static final Codec<TextureIcon> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("texture").forGetter(TextureIcon::texture)
            ).apply(instance, TextureIcon::new));

}