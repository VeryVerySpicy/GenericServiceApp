package com.example.myapplication.Models;

import java.util.Set;

public class Task {
    private String taskType;
    private String date;
    private String time;
    private boolean isRepeating;
    private Set<String> selectedDays;

    public Task(String taskType, String date, String time, boolean isRepeating, Set<String> selectedDays) {
        this.taskType = taskType;
        this.date = date;
        this.time = time;
        this.isRepeating = isRepeating;
        this.selectedDays = selectedDays;
    }

    // Getter methods
    public String getTaskType() { return taskType; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public boolean isRepeating() { return isRepeating; }
    public Set<String> getSelectedDays() { return selectedDays; }
    @Override
    public String toString() {
        return taskType + " | " + date + " | " + time + " | " + (isRepeating ? "Repeats on: " + String.join(", ", selectedDays) : "No Repeat");
    }
}
