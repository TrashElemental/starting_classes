package net.trashelemental.starting_classes.menu.class_system;

import com.mojang.serialization.Codec;

public sealed interface ClassIcon permits ItemIcon, TextureIcon {

    public static final Codec<ClassIcon> CODEC =
            Codec.STRING.dispatch(
                    icon -> {
                        if (icon instanceof ItemIcon) return "item";
                        if (icon instanceof TextureIcon) return "texture";
                        throw new IllegalStateException("Unknown ClassIcon: " + icon.getClass());
                    },
                    type -> switch (type) {
                        case "item" -> ItemIcon.CODEC;
                        case "texture" -> TextureIcon.CODEC;
                        default -> throw new IllegalArgumentException("Unknown icon type: " + type);
                    }
            );

}
