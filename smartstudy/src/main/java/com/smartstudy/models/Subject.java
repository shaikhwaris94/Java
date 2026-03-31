package com.smartstudy.models;

public class Subject {
    private String name;
    private String color;
    private int totalMinutesStudied;
    private int sessionCount;

    public Subject() {
    }

    public Subject(String name) {
        this.name = name;
        this.color = "#4CAF50";
        this.totalMinutesStudied = 0;
        this.sessionCount = 0;
    }

    public Subject(String name, String color) {
        this(name);
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getTotalMinutesStudied() {
        return totalMinutesStudied;
    }

    public void setTotalMinutesStudied(int totalMinutesStudied) {
        this.totalMinutesStudied = totalMinutesStudied;
    }

    public int getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
    }

    public void addStudyTime(int minutes) {
        this.totalMinutesStudied += minutes;
        this.sessionCount++;
    }

    public String getFormattedStudyTime() {
        int hours = totalMinutesStudied / 60;
        int minutes = totalMinutesStudied % 60;
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        }
        return minutes + "m";
    }

    @Override
    public String toString() {
        return name + " (" + getFormattedStudyTime() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Subject subject = (Subject) obj;
        return name != null && name.equals(subject.name);
    }
}
