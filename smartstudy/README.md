# SmartStudy - Study Session Manager

A JavaFX desktop application that helps students manage study sessions using the Pomodoro technique, track their progress, and get analytics on their study habits.

## Features

- **Pomodoro Timer**: Customizable study timer (5-120 minutes) with start, pause, and reset
- **Subject Management**: Organize study sessions by subject
- **Session Tracking**: Automatically saves completed sessions with notes
- **Statistics Dashboard**: View daily, weekly, and monthly study analytics
- **Break Suggestions**: Get recommended break durations based on study time
- **Data Persistence**: All sessions saved locally in JSON format

## Tech Stack

- **Language**: Java 17
- **GUI Framework**: JavaFX 17
- **Build Tool**: Maven
- **Data Storage**: JSON (using Gson)
- **Architecture**: MVC with Service Layer

## Project Structure

```
smartstudy/
├── src/main/java/com/smartstudy/
│   ├── models/          # Data models (StudySession, Subject)
│   ├── services/        # Business logic (TimerService, AnalyticsService)
│   ├── utils/           # Utilities (FileManager, DateUtils)
│   └── ui/              # JavaFX views and controllers
├── src/main/resources/  # FXML files and styles
├── data/                # JSON data storage
├── docs/                # Documentation
├── pom.xml              # Maven configuration
└── README.md
```

## How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/smartstudy.git
cd smartstudy
```

2. Build the project:
```bash
mvn clean compile
```

3. Run the application:
```bash
mvn javafx:run
```

Or use the JAR file:
```bash
mvn package
java -jar target/smartstudy-1.0.jar
```

## Usage

### Starting a Study Session
1. Select your subject from the dropdown
2. Set the duration (default is 25 minutes)
3. Click "Start" to begin the timer
4. Use "Pause" to temporarily stop, "Reset" to restart

### Viewing Statistics
- Click the "Statistics" tab to see your study analytics
- Toggle between daily, weekly, and monthly views
- See breakdown by subject

### Taking Notes
- Use the Notes section during study sessions
- Notes are automatically saved with session data

## Example Workflow

1. **Morning**: Select "Mathematics", set 25 min timer, start studying
2. **Break**: Application suggests 5-minute break
3. **Review**: Check Statistics tab for morning progress
4. **Afternoon**: Select "Physics", study for 45 minutes

## Future Improvements

- Export reports to PDF/Excel
- Cloud data synchronization
- Spaced repetition scheduling
- Mobile companion app
- AI-based study recommendations

## Author

Created for BYOP college course project.

## License

MIT License
