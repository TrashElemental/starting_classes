package net.trashelemental.starting_classes.menu.class_system;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FixedItemEntry(StartingItemData item) implements EquipmentEntry {

    public static final Codec<FixedItemEntry> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    StartingItemData.CODEC.fieldOf("item").forGetter(FixedItemEntry::item)
            ).apply(instance, FixedItemEntry::new));

}
