package com.smartstudy.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StudySession {
    private String id;
    private String subject;
    private int durationMinutes;
    private String notes;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean completed;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public StudySession() {
        this.id = generateId();
        this.startTime = LocalDateTime.now();
        this.completed = false;
    }

    public StudySession(String subject, int durationMinutes) {
        this();
        this.subject = subject;
        this.durationMinutes = durationMinutes;
    }

    public StudySession(String subject, int durationMinutes, String notes) {
        this(subject, durationMinutes);
        this.notes = notes;
    }

    private String generateId() {
        return "session_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void complete() {
        this.endTime = LocalDateTime.now();
        this.completed = true;
    }

    public String getStartTimeFormatted() {
        return startTime.format(FORMATTER);
    }

    public String getEndTimeFormatted() {
        return endTime != null ? endTime.format(FORMATTER) : "";
    }

    @Override
    public String toString() {
        return "StudySession{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", durationMinutes=" + durationMinutes +
                ", startTime=" + startTime +
                ", completed=" + completed +
                '}';
    }
}
