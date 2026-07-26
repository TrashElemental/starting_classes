package net.trashelemental.starting_classes.class_system;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.trashelemental.starting_classes.StartingClasses;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class StartingClassManager {

    private static final Map<String, StartingClassData> CLASSES = new HashMap<>();
    private static final List<Consumer<List<StartingClassData>>> LOAD_CALLBACKS = new ArrayList<>();

    public static List<StartingClassData> getClasses() {
        return CLASSES.values().stream()
                .filter(clazz -> !ClassBlacklist.isBlacklisted(clazz.id()))
                .collect(java.util.stream.Collectors.toList());
    }

    public static void clear() {
        CLASSES.clear();
    }

    public static void register(StartingClassData clazz) {
        if (CLASSES.containsKey(clazz.id())) {
            StartingClasses.LOGGER.warn(
                    "Duplicate starting class id '{}', overriding previous definition",
                    clazz.id()
            );
        }

        CLASSES.put(clazz.id(), clazz);
    }

    @Nullable
    public static StartingClassData getClassById(String id) {
        StartingClassData clazz = CLASSES.get(id);
        if (clazz != null && ClassBlacklist.isBlacklisted(id)) {
            return null;
        }
        return clazz;
    }

    public static void onClassesLoaded(Consumer<List<StartingClassData>> callback) {
        if (!CLASSES.isEmpty()) {
            List<StartingClassData> filtered = CLASSES.values().stream()
                    .filter(clazz -> !ClassBlacklist.isBlacklisted(clazz.id()))
                    .collect(java.util.stream.Collectors.toList());
            callback.accept(filtered);
        } else {
            LOAD_CALLBACKS.add(callback);
        }
    }

    public static void notifyLoadComplete() {
        List<Consumer<List<StartingClassData>>> callbacksCopy = new ArrayList<>(LOAD_CALLBACKS);
        LOAD_CALLBACKS.clear();

        for (Consumer<List<StartingClassData>> callback : callbacksCopy) {
            try {
                List<StartingClassData> filtered = CLASSES.values().stream()
                        .filter(clazz -> !ClassBlacklist.isBlacklisted(clazz.id()))
                        .collect(java.util.stream.Collectors.toList());
                callback.accept(filtered);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<ItemStack> buildLoadout(StartingClassData clazz, List<Integer> choices) {

        List<ItemStack> equipment = new ArrayList<>();

        int choiceIndex = 0;

        for (EquipmentEntry entry : clazz.equipment()) {

            if (entry instanceof FixedItemEntry fixed) {
                equipment.add(fixed.item().createStack());
            }

            else if (entry instanceof ChoiceItemEntry choice) {

                if (choice.options().isEmpty()) {
                    continue;
                }

                int selected = 0;

                if (choiceIndex < choices.size()) {
                    selected = choices.get(choiceIndex);
                }

                selected = Mth.clamp(
                        selected,
                        0,
                        choice.options().size() - 1
                );

                equipment.add(choice.options().get(selected).createStack());

                choiceIndex++;
            }
        }

        return equipment;
    }
}