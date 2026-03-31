package com.smartstudy.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.smartstudy.models.StudySession;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String DATA_DIR = "data";
    private static final String SESSIONS_FILE = DATA_DIR + "/sessions.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void ensureDataDirectoryExists() {
        Path path = Paths.get(DATA_DIR);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                System.err.println("Error creating data directory: " + e.getMessage());
            }
        }
    }

    public static List<StudySession> loadSessions() {
        ensureDataDirectoryExists();
        File file = new File(SESSIONS_FILE);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<StudySession>>() {}.getType();
            List<StudySession> sessions = gson.fromJson(reader, listType);
            return sessions != null ? sessions : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error loading sessions: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveSessions(List<StudySession> sessions) {
        ensureDataDirectoryExists();
        try (Writer writer = new FileWriter(SESSIONS_FILE)) {
            gson.toJson(sessions, writer);
        } catch (IOException e) {
            System.err.println("Error saving sessions: " + e.getMessage());
        }
    }

    public static void addSession(StudySession session) {
        List<StudySession> sessions = loadSessions();
        sessions.add(session);
        saveSessions(sessions);
    }

    public static void clearAllSessions() {
        saveSessions(new ArrayList<>());
    }
}
