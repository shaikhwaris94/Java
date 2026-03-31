package com.smartstudy.services;

import com.smartstudy.models.StudySession;
import com.smartstudy.utils.FileManager;

import java.util.List;

public class SessionService {
    private List<StudySession> sessions;

    public SessionService() {
        this.sessions = FileManager.loadSessions();
    }

    public void addSession(StudySession session) {
        session.complete();
        sessions.add(session);
        FileManager.saveSessions(sessions);
    }

    public List<StudySession> getAllSessions() {
        return sessions;
    }

    public List<StudySession> getRecentSessions(int count) {
        if (sessions.size() <= count) {
            return sessions;
        }
        return sessions.subList(sessions.size() - count, sessions.size());
    }

    public void clearAllSessions() {
        sessions.clear();
        FileManager.clearAllSessions();
    }

    public int getTotalSessionCount() {
        return sessions.size();
    }

    public int getTotalMinutesStudied() {
        return sessions.stream()
                .filter(StudySession::isCompleted)
                .mapToInt(StudySession::getDurationMinutes)
                .sum();
    }
}
