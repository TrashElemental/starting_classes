package net.trashelemental.starting_classes.junkyard_lib.util;

public class FormatMethods {
    /**
     * Convert a number input into roman numerals. Remember to only use with numbers greater than zero or add one.
     */
    public static String toRoman(int number) {
        if (number <= 0) return "0";

        int[] values =    {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] numerals = {"M", "CM", "D", "CD", "C", "XC", "L", "XL",
                "X", "IX", "V", "IV", "I"};

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            while (number >= values[i]) {
                number -= values[i];
                result.append(numerals[i]);
            }
        }

        return result.toString();
    }

    /**
     * Convert seconds into a time like 0:30.
     */
    public static String formatTimeFromSeconds(int seconds) {
        int m = seconds / 60;
        int s = seconds % 60;

        return String.format("%d:%02d", m, s);
    }

    /**
     * Convert ticks into a time like 0:30.
     */
    public static String formatTimeFromTicks(int ticks) {
        int totalSeconds = ticks / 20;
        int m = totalSeconds / 60;
        int s = totalSeconds % 60;

        return String.format("%d:%02d", m, s);
    }
}
