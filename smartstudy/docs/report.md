# SmartStudy - Study Session Manager
## Project Report

---

## 1. Introduction

In modern academic life, students often struggle with maintaining consistent study habits and managing their time effectively across multiple subjects. Traditional study methods lack structure and fail to provide feedback on actual study progress. Without a proper system, students may overestimate or underestimate their study time, leading to poor planning and reduced productivity.

**SmartStudy** is a JavaFX-based desktop application designed to help students manage their study sessions using the proven Pomodoro technique. The application provides a customizable study timer, tracks completed sessions, and offers analytics on study habits. By recording and analyzing study patterns, SmartStudy helps students understand how they spend their time and provides personalized recommendations for improvement.

This project was developed as part of the Bring Your Own Project (BYOP) college course requirement. It demonstrates practical application of Java programming, Object-Oriented Programming (OOP) principles, file handling, and Graphical User Interface (GUI) development using JavaFX.

---

## 2. Problem Identification

Through personal experience and observation of fellow students, several key problems were identified in the context of self-directed study:

**2.1 Lack of Structured Study Time**
Many students study without a clear plan or time structure. Without defined study sessions, time can slip away without meaningful progress. The Pomodoro technique addresses this by breaking study time into focused intervals, but implementing it manually is inconvenient.

**2.2 No Progress Visibility**
Students rarely have a clear picture of how much time they actually spend studying each subject. This makes it difficult to allocate time effectively or identify imbalanced study habits.

**2.3 No Accountability System**
Without tracking, students cannot review their past study sessions or measure improvement over time. This lack of data makes it hard to identify problems or celebrate progress.

**2.4 Difficulty Maintaining Breaks**
Even when students do take breaks, they often extend too long. A system that suggests appropriate break durations based on study time can help maintain balance.

---

## 3. Objectives

The primary objectives of SmartStudy are:

**3.1 Core Functionality**
- Develop a functional desktop application with an intuitive user interface
- Implement a customizable Pomodoro-based study timer (5-120 minutes)
- Create a session tracking system that records subject, duration, and notes
- Provide data persistence using local JSON file storage

**3.2 Analytics Features**
- Calculate and display statistics (daily, weekly, monthly study time)
- Show subject-wise time breakdown
- Generate bar charts for visual progress tracking
- Provide personalized study recommendations

**3.3 Technical Objectives**
- Demonstrate understanding of Java OOP concepts
- Implement proper MVC architecture with service layer
- Use JavaFX for GUI development
- Handle file I/O for data persistence

---

## 4. Proposed Solution

**SmartStudy** proposes a desktop application that combines:

1. **Smart Timer**: A fully controllable timer with start, pause, and reset functionality. Users can customize duration (5-120 minutes) and select the subject they're studying.

2. **Session Tracking**: Each completed session is automatically saved with subject, duration, start time, end time, and optional notes.

3. **Statistics Dashboard**: A dedicated statistics tab showing:
   - Today's, this week's, and total study time
   - Subject breakdown chart
   - Daily study time bar chart
   - AI-generated recommendations

4. **Data Persistence**: All session data is stored locally in JSON format, allowing data to persist between application runs without requiring a database server.

---

## 5. System Design

**5.1 Architecture Overview**

SmartStudy follows a layered architecture pattern:

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                        │
│              (JavaFX FXML + Controllers)                    │
├─────────────────────────────────────────────────────────────┤
│                    Service Layer                             │
│        (SessionService, AnalyticsService)                   │
├─────────────────────────────────────────────────────────────┤
│                    Data Access Layer                         │
│                    (FileManager - JSON)                      │
├─────────────────────────────────────────────────────────────┤
│                      Model Layer                             │
│         (StudySession, Subject, StudyStatistics)            │
└─────────────────────────────────────────────────────────────┘
```

**5.2 Module Description**

**Models (`com.smartstudy.models`)**
- `StudySession`: Represents a single study session with subject, duration, notes, timestamps
- `Subject`: Represents a study subject with name, color, and aggregated statistics
- `StudyStatistics`: Container for calculated statistics

**Services (`com.smartstudy.services`)**
- `SessionService`: Handles CRUD operations for study sessions
- `AnalyticsService`: Calculates statistics and generates recommendations

**Utilities (`com.smartstudy.utils`)**
- `FileManager`: Handles JSON file read/write operations
- `DateUtils`: Provides date formatting and calculation utilities

**UI Layer (`com.smartstudy.ui`)**
- `SmartStudyApplication`: JavaFX application entry point
- `MainController`: Handles all UI interactions and updates

**5.3 Data Flow**

1. User selects subject and duration on the Study Session tab
2. User clicks Start, creating a new StudySession object
3. Timer counts down using JavaFX Timeline animation
4. On completion, session is saved via SessionService
5. SessionService persists data to JSON via FileManager
6. Statistics tab queries SessionService, AnalyticsService calculates results
7. Charts and recommendations are displayed

**5.4 Project Structure**

```
smartstudy/
├── src/main/java/com/smartstudy/
│   ├── SmartStudyApplication.java    # Main entry point
│   ├── models/
│   │   ├── StudySession.java         # Session entity
│   │   ├── Subject.java              # Subject entity
│   │   └── StudyStatistics.java      # Statistics container
│   ├── services/
│   │   ├── SessionService.java       # Session CRUD
│   │   └── AnalyticsService.java     # Analytics logic
│   ├── utils/
│   │   ├── FileManager.java          # JSON persistence
│   │   └── DateUtils.java            # Date utilities
│   └── ui/
│       └── controllers/
│           └── MainController.java   # UI controller
├── src/main/resources/
│   ├── fxml/
│   │   └── main.fxml                 # UI layout
│   └── css/
│       └── styles.css                # Styling
├── data/                             # JSON storage
├── docs/                             # Documentation
├── pom.xml                           # Maven config
└── README.md
```

---

## 6. Implementation Details

**6.1 Timer Implementation**

The timer uses JavaFX's `Timeline` class with a `KeyFrame` that fires every second:

```java
timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
    if (remainingSeconds > 0) {
        remainingSeconds--;
        timerLabel.setText(DateUtils.formatTimerDisplay(remainingSeconds));
    } else {
        timerComplete();
    }
}));
timer.setCycleCount(Animation.INDEFINITE);
```

**6.2 Data Persistence**

Sessions are stored as JSON using Gson library:

```java
public static void saveSessions(List<StudySession> sessions) {
    ensureDataDirectoryExists();
    try (Writer writer = new FileWriter(SESSIONS_FILE)) {
        gson.toJson(sessions, writer);
    } catch (IOException e) {
        System.err.println("Error saving sessions: " + e.getMessage());
    }
}
```

**6.3 Statistics Calculation**

The AnalyticsService aggregates session data:

```java
public StudyStatistics calculateStatistics() {
    StudyStatistics stats = new StudyStatistics();
    stats.setTotalSessions(sessions.stream()
            .filter(StudySession::isCompleted)
            .count());
    stats.setTodayMinutes(getMinutesForPeriod("today"));
    // ... additional calculations
    return stats;
}
```

**6.4 Recommendations Engine**

Simple rule-based recommendations:

```java
public List<String> getRecommendations() {
    if (sessions.size() < 5) {
        return List.of("Start with 2-3 short study sessions daily.");
    }
    // Analyze patterns and generate suggestions
}
```

---

## 7. Challenges Faced

**7.1 Technical Challenges**

*JavaFX Setup*: Setting up JavaFX with Maven required correct dependency configuration and module-path arguments. The pom.xml had to be configured properly with the JavaFX Maven plugin.

*Timer Accuracy*: Using `Timeline` for the timer required careful handling to ensure the display updates correctly and the timer completes at the right moment.

*JSON Serialization*: The `LocalDateTime` fields required custom handling since Gson doesn't serialize them by default. The model classes needed proper structure.

**7.2 Design Challenges**

*Balancing Features*: Deciding which features to include while keeping the project achievable within the timeframe required careful prioritization.

*User Experience*: Creating an intuitive interface without overwhelming users with options was challenging. The tab-based layout helps organize functionality.

**7.3 Learning Challenges**

*JavaFX FXML*: Learning the FXML syntax and understanding how it connects to controller classes required careful study of the framework.

*MVC Pattern*: Properly separating concerns between the UI layer and business logic was important but sometimes challenging to get right.

---

## 8. Results

**8.1 Functional Results**

SmartStudy successfully implements all planned features:

- ✅ Customizable Pomodoro timer (5-120 minutes)
- ✅ Subject selection with 9 predefined options
- ✅ Start, pause, and reset timer controls
- ✅ Session notes functionality
- ✅ Automatic session saving on completion
- ✅ JSON-based data persistence
- ✅ Daily, weekly, and monthly statistics
- ✅ Subject breakdown display
- ✅ Bar chart visualization
- ✅ Personalized recommendations

**8.2 Technical Validation**

- Application compiles without errors using Maven
- All MVC layers properly separated
- Data persists correctly between runs
- JavaFX GUI renders as designed
- Charts display data correctly

**8.3 User Experience**

- Clean, intuitive tab-based interface
- Responsive timer controls
- Clear visual feedback during sessions
- Informative statistics dashboard
- Helpful recommendations

---

## 9. Future Scope

While SmartStudy addresses the core needs of study session management, several enhancements could be considered for future versions:

**9.1 Extended Features**
- Export statistics to PDF or Excel
- Add custom subjects with color coding
- Set daily study goals with notifications
- Track assignment deadlines

**9.2 Data Enhancements**
- Cloud synchronization across devices
- Backup and restore functionality
- Data import/export options

**9.3 Advanced Analytics**
- Weekly study pattern analysis
- Performance prediction based on study time
- Spaced repetition scheduling integration

**9.4 Platform Expansion**
- Mobile companion app (Android/iOS)
- Web version with cloud backend
- Browser extension for web-based study resources

---

## 10. Conclusion

SmartStudy represents a practical application of Java programming concepts learned throughout the course. By implementing a real-world tool for study management, the project demonstrates proficiency in several key areas:

1. **Java Programming**: Core language features including collections, streams, and lambda expressions
2. **Object-Oriented Design**: Proper use of classes, encapsulation, and separation of concerns
3. **JavaFX GUI Development**: Building user interfaces with FXML and styling with CSS
4. **File Handling**: JSON-based data persistence using Gson
5. **Software Architecture**: MVC pattern with service layer

The project successfully meets the BYOP course requirements while creating a genuinely useful tool that students can employ in their academic journey. The Pomodoro timer and progress tracking features provide real value, while the analytics and recommendations demonstrate practical application of algorithmic thinking.

---

## References

1. JavaFX Documentation - https://openjfx.io/
2. Gson Library - https://github.com/google/gson
3. Maven Documentation - https://maven.apache.org/
4. The Pomodoro Technique - Francesco Cirillo
5. Java 17 API Documentation - https://docs.oracle.com/en/java/javase/17/

---

*Project submitted as part of BYOP college course requirements*
*GitHub Repository: https://github.com/yourusername/smartstudy*
