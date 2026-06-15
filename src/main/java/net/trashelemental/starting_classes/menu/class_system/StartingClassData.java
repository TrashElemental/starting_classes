package net.trashelemental.starting_classes.menu.class_system;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;

import java.util.List;

public record StartingClassData(
        String id,
        Component name,
        Component description,
        Component source,
        ClassIcon icon,
        List<EquipmentEntry> equipment,
        List<AttributeBonusData> attributes
) {

    public static final Codec<Component> COMPONENT_CODEC =
            Codec.STRING.xmap(
                    Component::literal,
                    Component::getString
            );

    public static final Codec<StartingClassData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("id").forGetter(StartingClassData::id),

                    COMPONENT_CODEC.fieldOf("name").forGetter(StartingClassData::name),
                    COMPONENT_CODEC.fieldOf("description").forGetter(StartingClassData::description),
                    COMPONENT_CODEC.fieldOf("source").forGetter(StartingClassData::source),

                    ClassIcon.CODEC.fieldOf("icon").forGetter(StartingClassData::icon),

                    EquipmentEntry.CODEC.listOf().fieldOf("equipment").forGetter(StartingClassData::equipment),

                    AttributeBonusData.CODEC.listOf()
                            .optionalFieldOf("attributes", List.of())
                            .forGetter(StartingClassData::attributes)

            ).apply(instance, StartingClassData::new));

}

