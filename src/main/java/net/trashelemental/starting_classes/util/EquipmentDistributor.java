package net.trashelemental.starting_classes.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.trashelemental.starting_classes.menu.class_system.*;

import java.util.ArrayList;
import java.util.List;

public class EquipmentDistributor {

    public static void distributeEquipment(Player player, StartingClassData classData) {
        distributeEquipment(player, classData, new ArrayList<>());
    }

    public static void distributeEquipment(Player player, StartingClassData classData, List<Integer> choices) {
        int choiceIndex = 0;

        for (EquipmentEntry entry : classData.equipment()) {
            if (entry instanceof ChoiceItemEntry) {
                int selectedIndex = choiceIndex < choices.size() ? choices.get(choiceIndex) : 0;
                distributeEntry(player, entry, selectedIndex);
                choiceIndex++;
            } else {
                distributeEntry(player, entry, 0);
            }
        }
    }

    /**
     * Distribute a single equipment entry to a player
     */
    private static void distributeEntry(Player player, EquipmentEntry entry, int choiceIndex) {
        if (entry instanceof FixedItemEntry fixed) {
            ItemStack stack = fixed.item().createStack();

            // Skip spawn eggs - they're only for display purposes
            if (!isSpawnEgg(stack)) {
                giveItemToPlayer(player, stack);
            }
        }

        else if (entry instanceof ChoiceItemEntry choice) {
            choiceIndex = Math.min(choiceIndex, choice.options().size() - 1);
            ItemStack stack = choice.options().get(choiceIndex).createStack();

            if (!isSpawnEgg(stack)) {
                giveItemToPlayer(player, stack);
            }
        }
        else if (entry instanceof MobEntry mob) {
            // Spawn the mob with optional custom NBT data
            EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(mob.entityId());
            for (int i = 0; i < mob.count(); i++) {
                TamedMobUtil.spawnAndTameMob(player, entityType, mob.nbtString());
            }
        }
    }

    private static void giveItemToPlayer(Player player, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }

    public static boolean isSpawnEgg(ItemStack stack) {
        return stack.getItem() instanceof SpawnEggItem;
    }

    public static boolean isSpawnEgg(Item item) {
        return item instanceof SpawnEggItem;
    }
}