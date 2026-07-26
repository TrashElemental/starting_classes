package net.trashelemental.starting_classes.class_system;

import java.util.List;

public record ClassSelectionData(
        String classId,
        List<Integer> choiceIndices
) {}
