package net.trashelemental.starting_classes.menu.class_system;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Optional;

public record StartingItemData(ResourceLocation itemId, int count, @Nullable String nbtString) {

    public ItemStack createStack() {

        Item item = ForgeRegistries.ITEMS.getValue(itemId);

        if (item == null) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = new ItemStack(item, count);

        // Apply NBT data if present
        if (nbtString != null && !nbtString.isEmpty()) {
            try {
                CompoundTag tag = TagParser.parseTag(nbtString);
                stack.setTag(tag);
            } catch (Exception e) {
                LoggerFactory.getLogger(StartingItemData.class)
                        .warn("Failed to parse NBT data for item {}: {}", itemId, e.getMessage());
            }
        }

        return stack;
    }

    public static final Codec<StartingItemData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("item").forGetter(StartingItemData::itemId),
            Codec.INT.optionalFieldOf("count", 1).forGetter(StartingItemData::count),
            Codec.STRING.optionalFieldOf("nbt").forGetter(data -> Optional.ofNullable(data.nbtString()))
    ).apply(instance, (itemId, count, nbtOpt) ->
            new StartingItemData(itemId, count, nbtOpt.orElse(null))
    ));

}