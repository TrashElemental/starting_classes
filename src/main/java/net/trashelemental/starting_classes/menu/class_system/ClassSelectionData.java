package net.trashelemental.starting_classes.menu.class_system;

import java.util.List;

public record ClassSelectionData(
        String classId,
        List<Integer> choiceIndices
) {}
