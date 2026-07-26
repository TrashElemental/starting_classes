package net.trashelemental.starting_classes.class_system;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record AttributeBonusData(ResourceLocation attribute, double amount, AttributeModifier.Operation operation) {

    public static final Codec<AttributeModifier.Operation> OPERATION_CODEC =
            Codec.STRING.xmap(
                    str -> {
                        return switch (str.toLowerCase()) {
                            case "add_value", "addition" -> AttributeModifier.Operation.ADDITION;
                            case "multiply_base" -> AttributeModifier.Operation.MULTIPLY_BASE;
                            case "multiply_total" -> AttributeModifier.Operation.MULTIPLY_TOTAL;
                            default -> throw new IllegalArgumentException("Unknown operation: " + str);
                        };
                    },
                    op -> switch (op) {
                        case ADDITION -> "add_value";
                        case MULTIPLY_BASE -> "multiply_base";
                        case MULTIPLY_TOTAL -> "multiply_total";
                    }
            );

    public static final Codec<AttributeBonusData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("attribute").forGetter(AttributeBonusData::attribute),
                    Codec.DOUBLE.fieldOf("amount").forGetter(AttributeBonusData::amount),
                    OPERATION_CODEC.fieldOf("operation").forGetter(AttributeBonusData::operation)
            ).apply(instance, AttributeBonusData::new));

}
