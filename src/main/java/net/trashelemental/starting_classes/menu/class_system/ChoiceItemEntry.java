package net.trashelemental.starting_classes.menu.class_system;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record ChoiceItemEntry(List<StartingItemData> options) implements EquipmentEntry {

    public static final MapCodec<ChoiceItemEntry> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    StartingItemData.CODEC.listOf()
                            .fieldOf("options")
                            .forGetter(ChoiceItemEntry::options)
            ).apply(instance, ChoiceItemEntry::new));

}