package com.smartstudy.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtils {
    public static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

    public static String formatDate(LocalDate date) {
        return date.format(DISPLAY_FORMATTER);
    }

    public static String formatTime(LocalDateTime dateTime) {
        return dateTime.format(TIME_FORMATTER);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATETIME_FORMATTER);
    }

    public static LocalDate getToday() {
        return LocalDate.now();
    }

    public static LocalDate getWeekStart() {
        return LocalDate.now().minusDays(6);
    }

    public static LocalDate getMonthStart() {
        return LocalDate.now().minusDays(29);
    }

    public static boolean isToday(LocalDateTime dateTime) {
        return dateTime.toLocalDate().equals(getToday());
    }

    public static boolean isWithinLastDays(LocalDateTime dateTime, int days) {
        LocalDate startDate = LocalDate.now().minusDays(days - 1);
        return !dateTime.toLocalDate().isBefore(startDate) && !dateTime.toLocalDate().isAfter(getToday());
    }

    public static String formatDuration(int totalMinutes) {
        if (totalMinutes < 60) {
            return totalMinutes + " min";
        }
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        if (minutes == 0) {
            return hours + " hr";
        }
        return hours + " hr " + minutes + " min";
    }

    public static String formatTimerDisplay(int remainingSeconds) {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static int suggestBreakDuration(int studyMinutes) {
        if (studyMinutes < 25) {
            return 5;
        } else if (studyMinutes < 45) {
            return 10;
        } else if (studyMinutes < 60) {
            return 15;
        } else {
            return 20;
        }
    }
}
