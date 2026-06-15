package net.trashelemental.starting_classes.capability;

public class PlayerClassData {

    private String selectedClass = "";

    public String getSelectedClass() {
        return selectedClass;
    }

    public void setSelectedClass(String selectedClass) {
        this.selectedClass = selectedClass;
    }

    public boolean hasSelectedClass() {
        return !selectedClass.isEmpty();
    }
}
