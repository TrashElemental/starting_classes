package net.trashelemental.starting_classes.class_system;

import com.mojang.serialization.Codec;

public sealed interface EquipmentEntry permits FixedItemEntry, ChoiceItemEntry, MobEntry, CommandEntry, AttributeModifierEntry {

    public static final Codec<EquipmentEntry> CODEC =
            Codec.STRING.dispatch(
                    entry -> {
                        if (entry instanceof FixedItemEntry) return "fixed";
                        if (entry instanceof ChoiceItemEntry) return "choice";
                        if (entry instanceof MobEntry) return "mob";
                        if (entry instanceof CommandEntry) return "command";
                        if (entry instanceof AttributeModifierEntry) return "attribute";
                        throw new IllegalStateException("Unknown EquipmentEntry: " + entry.getClass());
                    },
                    type -> switch (type) {
                        case "fixed" -> FixedItemEntry.CODEC;
                        case "choice" -> ChoiceItemEntry.CODEC;
                        case "mob" -> MobEntry.CODEC;
                        case "command" -> CommandEntry.CODEC;
                        case "attribute" -> AttributeModifierEntry.CODEC;
                        default -> throw new IllegalArgumentException("Unknown equipment type: " + type);
                    }
            );

}