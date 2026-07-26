package net.trashelemental.starting_classes.class_system;

import java.util.HashMap;
import java.util.Map;

/**
 * Tracks how many times each class has been selected by players.
 * Used to implement the "disable after selection" feature.
 */
public class ClassSelectionTracker {

    private static final Map<String, Integer> CLASS_SELECTION_COUNT = new HashMap<>();

    /**
     * Increments the selection count for a class and returns the updated count.
     *
     * @param classId the class ID to increment
     * @return the new selection count for this class
     */
    public static int incrementSelectionCount(String classId) {
        int currentCount = CLASS_SELECTION_COUNT.getOrDefault(classId, 0);
        int newCount = currentCount + 1;
        CLASS_SELECTION_COUNT.put(classId, newCount);
        return newCount;
    }

    /**
     * Gets the current selection count for a class.
     *
     * @param classId the class ID
     * @return the number of times this class has been selected
     */
    public static int getSelectionCount(String classId) {
        return CLASS_SELECTION_COUNT.getOrDefault(classId, 0);
    }

    /**
     * Resets the selection count for a specific class.
     *
     * @param classId the class ID to reset
     */
    public static void resetSelectionCount(String classId) {
        CLASS_SELECTION_COUNT.remove(classId);
    }

    /**
     * Clears all selection counts.
     */
    public static void clearAll() {
        CLASS_SELECTION_COUNT.clear();
    }
}
