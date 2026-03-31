package com.smartstudy.services;

import com.smartstudy.models.StudySession;
import com.smartstudy.models.StudyStatistics;
import com.smartstudy.models.Subject;
import com.smartstudy.utils.DateUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsService {
    private List<StudySession> sessions;

    public AnalyticsService(List<StudySession> sessions) {
        this.sessions = sessions;
    }

    public StudyStatistics calculateStatistics() {
        StudyStatistics stats = new StudyStatistics();

        stats.setTotalSessions((int) sessions.stream().filter(StudySession::isCompleted).count());
        stats.setTotalMinutesStudied(getTotalMinutesStudied());

        stats.setTodayMinutes(getMinutesForPeriod("today"));
        stats.setWeekMinutes(getMinutesForPeriod("week"));
        stats.setMonthMinutes(getMinutesForPeriod("month"));

        stats.setSubjects(getSubjectBreakdown());
        return stats;
    }

    private int getTotalMinutesStudied() {
        return sessions.stream()
                .filter(StudySession::isCompleted)
                .mapToInt(StudySession::getDurationMinutes)
                .sum();
    }

    private int getMinutesForPeriod(String period) {
        return sessions.stream()
                .filter(StudySession::isCompleted)
                .filter(s -> {
                    LocalDateTime startTime = s.getStartTime();
                    switch (period) {
                        case "today":
                            return DateUtils.isToday(startTime);
                        case "week":
                            return DateUtils.isWithinLastDays(startTime, 7);
                        case "month":
                            return DateUtils.isWithinLastDays(startTime, 30);
                        default:
                            return false;
                    }
                })
                .mapToInt(StudySession::getDurationMinutes)
                .sum();
    }

    private List<Subject> getSubjectBreakdown() {
        Map<String, Integer> subjectMinutes = new HashMap<>();

        sessions.stream()
                .filter(StudySession::isCompleted)
                .forEach(s -> {
                    String subject = s.getSubject();
                    int minutes = s.getDurationMinutes();
                    subjectMinutes.merge(subject, minutes, Integer::sum);
                });

        return subjectMinutes.entrySet().stream()
                .map(e -> {
                    Subject sub = new Subject(e.getKey());
                    sub.setTotalMinutesStudied(e.getValue());
                    return sub;
                })
                .sorted((a, b) -> b.getTotalMinutesStudied() - a.getTotalMinutesStudied())
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getDailyMinutes(int days) {
        Map<String, Integer> dailyData = new LinkedHashMap<>();

        for (int i = days - 1; i >= 0; i--) {
            LocalDateTime date = LocalDateTime.now().minusDays(i);
            String dateKey = date.toLocalDate().toString();
            dailyData.put(dateKey, 0);
        }

        sessions.stream()
                .filter(StudySession::isCompleted)
                .filter(s -> DateUtils.isWithinLastDays(s.getStartTime(), days))
                .forEach(s -> {
                    String dateKey = s.getStartTime().toLocalDate().toString();
                    dailyData.merge(dateKey, s.getDurationMinutes(), Integer::sum);
                });

        return dailyData;
    }

    public List<String> getRecommendations() {
        List<String> recommendations = new ArrayList<>();

        if (sessions.size() < 5) {
            recommendations.add("Start with 2-3 short study sessions daily to build a habit.");
            return recommendations;
        }

        int avgDuration = getTotalMinutesStudied() / Math.min(sessions.size(), 7);
        if (avgDuration < 25) {
            recommendations.add("Your sessions are quite short. Try using the Pomodoro technique: 25 min study, 5 min break.");
        }

        List<Subject> subjects = getSubjectBreakdown();
        if (!subjects.isEmpty()) {
            int total = subjects.stream().mapToInt(Subject::getTotalMinutesStudied).sum();
            for (Subject subject : subjects) {
                double percentage = (double) subject.getTotalMinutesStudied() / total * 100;
                if (percentage > 60) {
                    recommendations.add(String.format("You spend %.0f%% of study time on %s. Consider balancing with other subjects.",
                            percentage, subject.getName()));
                }
            }
        }

        if (recommendations.isEmpty()) {
            recommendations.add("You're doing great! Keep up the consistent study habits.");
        }

        return recommendations;
    }
}
