package net.trashelemental.starting_classes.class_system;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record AttributeModifierEntry(AttributeBonusData bonus) implements EquipmentEntry {

    public static final Codec<AttributeModifierEntry> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    AttributeBonusData.CODEC.fieldOf("bonus").forGetter(AttributeModifierEntry::bonus)
            ).apply(instance, AttributeModifierEntry::new));

}
