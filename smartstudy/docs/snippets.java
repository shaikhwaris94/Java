// SmartStudy - Code Samples
// Key snippets demonstrating core functionality

// =============================================================================
// SAMPLE 1: Main Application Entry Point
// =============================================================================

package com.smartstudy;

import com.smartstudy.ui.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SmartStudyApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();

        MainController controller = loader.getController();

        Scene scene = new Scene(root, 900, 700);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        stage.setTitle("SmartStudy - Study Session Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

// =============================================================================
// SAMPLE 2: StudySession Model
// =============================================================================

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

    private String generateId() {
        return "session_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    public void complete() {
        this.endTime = LocalDateTime.now();
        this.completed = true;
    }

    // Getters and setters omitted for brevity
}

// =============================================================================
// SAMPLE 3: SessionService (Business Logic)
// =============================================================================

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

    public int getTotalMinutesStudied() {
        return sessions.stream()
                .filter(StudySession::isCompleted)
                .mapToInt(StudySession::getDurationMinutes)
                .sum();
    }
}

// =============================================================================
// SAMPLE 4: AnalyticsService
// =============================================================================

package com.smartstudy.services;

import com.smartstudy.models.StudySession;
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

    public int getMinutesForPeriod(String period) {
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

    public List<String> getRecommendations() {
        List<String> recommendations = new ArrayList<>();

        if (sessions.size() < 5) {
            recommendations.add("Start with 2-3 short study sessions daily.");
            return recommendations;
        }

        int avgDuration = getTotalMinutes() / Math.min(sessions.size(), 7);
        if (avgDuration < 25) {
            recommendations.add("Try Pomodoro: 25 min study, 5 min break.");
        }

        return recommendations;
    }
}

// =============================================================================
// SAMPLE 5: Timer Implementation in Controller
// =============================================================================

package com.smartstudy.ui.controllers;

import com.smartstudy.models.StudySession;
import com.smartstudy.utils.DateUtils;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

public class MainController {
    @FXML
    private Label timerLabel;

    @FXML
    private Button startButton, pauseButton, resetButton;

    private Timeline timer;
    private int remainingSeconds;
    private boolean isRunning;
    private StudySession currentSession;

    @FXML
    private void initialize() {
        remainingSeconds = 25 * 60;
        isRunning = false;
        setupTimer();
    }

    private void setupTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (remainingSeconds > 0) {
                remainingSeconds--;
                timerLabel.setText(DateUtils.formatTimerDisplay(remainingSeconds));
            } else {
                timerComplete();
            }
        }));
        timer.setCycleCount(Animation.INDEFINITE);
    }

    @FXML
    private void startTimer() {
        if (!isRunning) {
            isRunning = true;
            currentSession = new StudySession(
                subjectComboBox.getValue(),
                durationSpinner.getValue()
            );
            timer.play();
        }
    }

    private void timerComplete() {
        timer.stop();
        isRunning = false;
        currentSession.complete();
        sessionService.addSession(currentSession);
        showAlert("Session Complete!", "Great job!");
    }
}

// =============================================================================
// SAMPLE 6: FileManager for JSON Persistence
// =============================================================================

package com.smartstudy.utils;

import com.google.gson.Gson;
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
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    public static List<StudySession> loadSessions() {
        ensureDataDirectoryExists();
        File file = new File(SESSIONS_FILE);
        if (!file.exists()) return new ArrayList<>();

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<StudySession>>() {}.getType();
            List<StudySession> sessions = gson.fromJson(reader, listType);
            return sessions != null ? sessions : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveSessions(List<StudySession> sessions) {
        ensureDataDirectoryExists();
        try (Writer writer = new FileWriter(SESSIONS_FILE)) {
            gson.toJson(sessions, writer);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

// =============================================================================
// SAMPLE 7: FXML Layout Snippet
// =============================================================================

<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.geometry.Insets?>
<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>
<?import javafx.scene.text.*?>

<BorderPane xmlns="http://javafx.com/javafx"
            fx:controller="com.smartstudy.ui.controllers.MainController">

    <center>
        <TabPane>
            <Tab text="Study Session">
                <VBox spacing="20" alignment="CENTER">
                    <Label fx:id="timerLabel" text="25:00" styleClass="timer-display"/>

                    <HBox spacing="15" alignment="CENTER">
                        <Button fx:id="startButton" text="Start" onAction="#startTimer"/>
                        <Button fx:id="pauseButton" text="Pause" onAction="#pauseTimer" disable="true"/>
                        <Button fx:id="resetButton" text="Reset" onAction="#resetTimer"/>
                    </HBox>

                    <ComboBox fx:id="subjectComboBox" promptText="Select Subject"/>
                    <Spinner fx:id="durationSpinner"/>

                    <TextArea fx:id="notesTextArea" promptText="Notes..."/>
                </VBox>
            </Tab>

            <Tab text="Statistics">
                <HBox>
                    <BarChart fx:id="statsChart"/>
                    <VBox>
                        <ListView fx:id="recommendationsList"/>
                    </VBox>
                </HBox>
            </Tab>
        </TabPane>
    </center>
</BorderPane>
