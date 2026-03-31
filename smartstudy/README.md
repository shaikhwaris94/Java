# Student Record Management System

A simple console-based Java application for managing student records, demonstrating core Java concepts from your course.

## Course Concepts Covered

| Module | Concepts Used |
|--------|--------------|
| **Module 1: Java Platform Overview** | Basic Java syntax, main class, package structure |
| **Module 2: OOP Features** | Classes, Objects, Encapsulation, Inheritance, Data Hiding |
| **Module 3: Exception Handling & I/O** | Try-catch, FileReader, FileWriter, BufferedReader |
| **Module 4: Collections Framework** | ArrayList, HashMap, List, Map, Stream API |
| **Module 5: JDBC** | Structure ready for database integration |

## Features

- **Add Student**: Register new students with name, email, roll number, department
- **View All Students**: List all registered students with grades
- **Search Students**: Find by ID, roll number, name, or department
- **Update Student**: Modify existing student details
- **Delete Student**: Remove student records
- **Add Grades**: Record subject-wise grades for students
- **Analytics**: View class average, department-wise stats, grade distribution
- **Top Performers**: View highest performing students

## Project Structure

```
smartstudy/
├── src/com/smartstudy/
│   ├── models/
│   │   ├── Student.java          # Student class (OOP: Encapsulation)
│   │   └── Grade.java             # Grade class (OOP: Composition)
│   ├── services/
│   │   ├── StudentService.java    # CRUD operations (Collections: ArrayList, HashMap)
│   │   └── AnalyticsService.java   # Statistics (Stream API, Maps)
│   ├── utils/
│   │   ├── FileManager.java        # File I/O with Exception Handling
│   │   └── InputHandler.java       # User input with validation
│   ├── ui/
│   │   └── MenuSystem.java         # Console menu interface
│   └── Main.java                   # Application entry point
├── data/                           # Data storage (CSV files)
├── docs/                           # Documentation
├── README.md
└── report.md
```

## How to Compile & Run

### Compile
```bash
cd smartstudy/src
javac -d ../out com/smartstudy/models/*.java com/smartstudy/services/*.java com/smartstudy/utils/*.java com/smartstudy/ui/*.java com/smartstudy/Main.java
```

### Run
```bash
cd ../out
java com.smartstudy.Main
```

Or compile and run in one step:
```bash
cd smartstudy/src
javac -d ../out com/smartstudy/models/*.java com/smartstudy/services/*.java com/smartstudy/utils/*.java com/smartstudy/ui/*.java com/smartstudy/Main.java && cd ../out && java com.smartstudy.Main
```

## Usage

1. Select option 1-9 from the main menu
2. Follow prompts to add, search, update, or delete students
3. Add grades to calculate averages
4. View analytics for class statistics

## Example Session

```
============================================================
  STUDENT RECORD MANAGEMENT SYSTEM
============================================================
1. Add New Student
2. View All Students
...
Enter your choice (1-9): 1

Enter Name: John Doe
Enter Email: john@example.com
Enter Roll Number: 2024001
Enter Department: Computer Science

Student added successfully!
```

## Data Storage

Student records are stored in `data/students.txt` as CSV.
Grades are stored in memory and can be extended to file storage.

## Author

**Student:** Shaikh Mohammad Warsi
**Registration No.:** 24BAI10046
**University:** VIT Bhopal
**Course:** Vityarthi (BYOP)

## License

MIT License
