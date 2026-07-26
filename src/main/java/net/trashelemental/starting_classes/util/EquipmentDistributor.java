package net.trashelemental.starting_classes.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.trashelemental.starting_classes.StartingClasses;
import net.trashelemental.starting_classes.class_system.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        else if (entry instanceof CommandEntry cmd) {
            //This code replaces instances of PLAYER in the command with the player's name.
            //This is done to allow for commands that require a player's name, such as /effect give PLAYER or /tp PLAYER.
            String expandedCommand = cmd.command().replace("PLAYER", player.getName().getString());

            var commandDispatcher = player.getServer().getCommands().getDispatcher();
            var commandSourceStack = player.getServer().createCommandSourceStack()
                    .withPosition(player.position())
                    .withSuppressedOutput();
            try {
                var parseResults = commandDispatcher.parse(expandedCommand, commandSourceStack);
                player.getServer().getCommands().performCommand(parseResults, expandedCommand);
            } catch (Exception e) {
                System.err.println("Failed to execute command: " + expandedCommand);
                e.printStackTrace();
            }
        }
        else if (entry instanceof AttributeModifierEntry attributeEntry) {
            applyAttributeModifier(player, attributeEntry);
        }

    }

    /**
     * Apply a permanent attribute modifier with a deterministic UUID
     */
    public static void applyAttributeModifier(Player player, AttributeModifierEntry entry) {
        var attribute = BuiltInRegistries.ATTRIBUTE.get(entry.bonus().attribute());
        if (attribute == null) {
            System.err.println("Unknown attribute: " + entry.bonus().attribute());
            return;
        }

        var attributeHolder = BuiltInRegistries.ATTRIBUTE.getHolder(entry.bonus().attribute());
        if (attributeHolder.isEmpty()) {
            System.err.println("Cannot find holder for attribute: " + entry.bonus().attribute());
            return;
        }

        var attributeInstance = player.getAttributes().getInstance(attributeHolder.get());
        if (attributeInstance == null) {
            return;
        }

        String uuidString = entry.getDeterministicUUID().toString();
        var modifierId = StartingClasses.prefix("attribute_" + uuidString);

        var modifier = new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                modifierId,
                entry.bonus().amount(),
                entry.bonus().operation()
        );

        attributeInstance.removeModifier(modifierId);
        attributeInstance.addPermanentModifier(modifier);
    }

    private static void giveItemToPlayer(Player player, ItemStack stack) {

        if (stack.getItem() instanceof ArmorItem armor) {
            EquipmentSlot slot = armor.getEquipmentSlot();
            ItemStack current = player.getItemBySlot(slot);

            if (current.isEmpty()) {
                player.setItemSlot(slot, stack);
                return;
            }
        }

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