package com.smartstudy.ui.controllers;

import com.smartstudy.models.StudySession;
import com.smartstudy.models.StudyStatistics;
import com.smartstudy.models.Subject;
import com.smartstudy.services.AnalyticsService;
import com.smartstudy.services.SessionService;
import com.smartstudy.utils.DateUtils;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class MainController {
    @FXML
    private Label timerLabel;

    @FXML
    private ComboBox<String> subjectComboBox;

    @FXML
    private Spinner<Integer> durationSpinner;

    @FXML
    private Button startButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Button resetButton;

    @FXML
    private TextArea notesTextArea;

    @FXML
    private Label todayStatsLabel;

    @FXML
    private Label weekStatsLabel;

    @FXML
    private Label totalStatsLabel;

    @FXML
    private TabPane mainTabPane;

    @FXML
    private BarChart<String, Number> statsChart;

    @FXML
    private CategoryAxis chartXAxis;

    @FXML
    private ComboBox<String> periodComboBox;

    @FXML
    private ListView<String> recommendationsList;

    @FXML
    private ListView<String> subjectBreakdownList;

    private SessionService sessionService;
    private AnalyticsService analyticsService;

    private Timeline timer;
    private int remainingSeconds;
    private boolean isRunning;
    private String currentSubject;
    private StudySession currentSession;

    private static final String[] DEFAULT_SUBJECTS = {
            "Mathematics", "Physics", "Chemistry", "Biology",
            "Computer Science", "English", "History", "Economics", "Other"
    };

    @FXML
    private void initialize() {
        sessionService = new SessionService();
        analyticsService = new AnalyticsService(sessionService.getAllSessions());

        ObservableList<String> subjects = FXCollections.observableArrayList(DEFAULT_SUBJECTS);
        subjectComboBox.setItems(subjects);
        subjectComboBox.setValue("Mathematics");

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 120, 25, 5);
        durationSpinner.setValueFactory(valueFactory);

        ObservableList<String> periods = FXCollections.observableArrayList("Today", "This Week", "This Month");
        periodComboBox.setItems(periods);
        periodComboBox.setValue("This Week");

        periodComboBox.setOnAction(e -> updateChart());

        timerLabel.setText("25:00");
        isRunning = false;
        remainingSeconds = 25 * 60;

        setupTimer();
        updateStatistics();
        updateChart();
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
            currentSubject = subjectComboBox.getValue();
            currentSession = new StudySession(currentSubject, durationSpinner.getValue(), notesTextArea.getText());

            isRunning = true;
            timer.play();

            startButton.setDisable(true);
            pauseButton.setDisable(false);
            resetButton.setDisable(false);
        }
    }

    @FXML
    private void pauseTimer() {
        if (isRunning) {
            timer.stop();
            isRunning = false;
            pauseButton.setText("Resume");
        } else {
            isRunning = true;
            timer.play();
            pauseButton.setText("Pause");
        }
    }

    @FXML
    private void resetTimer() {
        timer.stop();
        isRunning = false;
        remainingSeconds = durationSpinner.getValue() * 60;
        timerLabel.setText(DateUtils.formatTimerDisplay(remainingSeconds));

        startButton.setDisable(false);
        pauseButton.setText("Pause");
        pauseButton.setDisable(true);
    }

    private void timerComplete() {
        timer.stop();
        isRunning = false;

        currentSession.setNotes(notesTextArea.getText());
        sessionService.addSession(currentSession);

        int breakMinutes = DateUtils.suggestBreakDuration(currentSession.getDurationMinutes());
        showAlert("Session Complete!",
                "Great job! You studied " + currentSubject + " for " + currentSession.getDurationMinutes() + " minutes.\n\n" +
                        "Suggested break: " + breakMinutes + " minutes");

        resetTimer();
        updateStatistics();

        notesTextArea.clear();
        analyticsService = new AnalyticsService(sessionService.getAllSessions());
        updateRecommendations();
    }

    private void updateStatistics() {
        StudyStatistics stats = analyticsService.calculateStatistics();

        todayStatsLabel.setText("Today: " + DateUtils.formatDuration(stats.getTodayMinutes()));
        weekStatsLabel.setText("This Week: " + DateUtils.formatDuration(stats.getWeekMinutes()));
        totalStatsLabel.setText("Total Sessions: " + stats.getTotalSessions() +
                " (" + DateUtils.formatDuration(stats.getTotalMinutesStudied()) + ")");

        updateSubjectBreakdown();
    }

    private void updateSubjectBreakdown() {
        ObservableList<String> items = FXCollections.observableArrayList();
        List<Subject> subjects = analyticsService.calculateStatistics().getSubjects();

        if (subjects.isEmpty()) {
            items.add("No study sessions yet");
        } else {
            for (Subject subject : subjects) {
                items.add(subject.getName() + ": " + subject.getFormattedStudyTime());
            }
        }
        subjectBreakdownList.setItems(items);
    }

    private void updateChart() {
        String period = periodComboBox.getValue();
        int days = switch (period) {
            case "Today" -> 1;
            case "This Month" -> 30;
            default -> 7;
        };

        statsChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Study Time (minutes)");

        Map<String, Integer> dailyData = analyticsService.getDailyMinutes(days);

        for (Map.Entry<String, Integer> entry : dailyData.entrySet()) {
            String date = entry.getKey().substring(5);
            series.getData().add(new XYChart.Data<>(date, entry.getValue()));
        }

        statsChart.getData().add(series);
    }

    private void updateRecommendations() {
        ObservableList<String> items = FXCollections.observableArrayList();
        List<String> recommendations = analyticsService.getRecommendations();

        for (String rec : recommendations) {
            items.add("• " + rec);
        }
        recommendationsList.setItems(items);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void refreshData() {
        sessionService = new SessionService();
        analyticsService = new AnalyticsService(sessionService.getAllSessions());
        updateStatistics();
        updateChart();
        updateRecommendations();
    }
}
