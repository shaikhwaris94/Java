package com.smartstudy.models;

import java.util.List;

public class StudyStatistics {
    private int totalSessions;
    private int totalMinutesStudied;
    private int todayMinutes;
    private int weekMinutes;
    private int monthMinutes;
    private List<Subject> subjects;

    public StudyStatistics() {
    }

    public int getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(int totalSessions) {
        this.totalSessions = totalSessions;
    }

    public int getTotalMinutesStudied() {
        return totalMinutesStudied;
    }

    public void setTotalMinutesStudied(int totalMinutesStudied) {
        this.totalMinutesStudied = totalMinutesStudied;
    }

    public int getTodayMinutes() {
        return todayMinutes;
    }

    public void setTodayMinutes(int todayMinutes) {
        this.todayMinutes = todayMinutes;
    }

    public int getWeekMinutes() {
        return weekMinutes;
    }

    public void setWeekMinutes(int weekMinutes) {
        this.weekMinutes = weekMinutes;
    }

    public int getMonthMinutes() {
        return monthMinutes;
    }

    public void setMonthMinutes(int monthMinutes) {
        this.monthMinutes = monthMinutes;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public String getFormattedTotalTime() {
        int hours = totalMinutesStudied / 60;
        int minutes = totalMinutesStudied % 60;
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        }
        return minutes + "m";
    }

    public String getFormattedTodayTime() {
        int hours = todayMinutes / 60;
        int minutes = todayMinutes % 60;
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        }
        return minutes + "m";
    }

    public String getFormattedWeekTime() {
        int hours = weekMinutes / 60;
        int minutes = weekMinutes % 60;
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        }
        return minutes + "m";
    }
}
