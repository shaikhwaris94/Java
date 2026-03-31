package com.smartstudy.ui;

import com.smartstudy.models.Grade;
import com.smartstudy.models.Student;
import com.smartstudy.services.AnalyticsService;
import com.smartstudy.services.StudentService;
import com.smartstudy.utils.InputHandler;

import java.util.List;
import java.util.Map;

public class MenuSystem {
    private StudentService studentService;
    private AnalyticsService analyticsService;

    public MenuSystem() {
        this.studentService = new StudentService();
        this.analyticsService = new AnalyticsService(studentService);
    }

    public void showMainMenu() {
        while (true) {
            printHeader("STUDENT RECORD MANAGEMENT SYSTEM");

            System.out.println("1. Add New Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Add Grade to Student");
            System.out.println("7. View Analytics");
            System.out.println("8. Top Performers");
            System.out.println("9. Exit");

            int choice = InputHandler.getInt("Enter your choice (1-9): ");

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewAllStudents();
                case 3 -> searchStudent();
                case 4 -> updateStudent();
                case 5 -> deleteStudent();
                case 6 -> addGrade();
                case 7 -> showAnalytics();
                case 8 -> showTopPerformers();
                case 9 -> {
                    System.out.println("Thank you for using Student Record Management System!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addStudent() {
        printHeader("ADD NEW STUDENT");

        String name = InputHandler.getString("Enter Name: ");
        String email = InputHandler.getEmail("Enter Email: ");
        String rollNumber = InputHandler.getString("Enter Roll Number: ");
        String department = InputHandler.getString("Enter Department: ");

        Student student = new Student(name, email, rollNumber, department);

        if (studentService.addStudent(student)) {
            System.out.println("Student added successfully!");
        } else {
            System.out.println("Error: Roll number already exists!");
        }

        InputHandler.pause();
    }

    private void viewAllStudents() {
        printHeader("ALL STUDENTS");

        List<Student> students = studentService.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.println("Total Students: " + students.size());
            System.out.println("-".repeat(80));
            for (Student student : students) {
                System.out.println(student);
            }
        }

        InputHandler.pause();
    }

    private void searchStudent() {
        printHeader("SEARCH STUDENT");

        System.out.println("1. Search by ID");
        System.out.println("2. Search by Roll Number");
        System.out.println("3. Search by Name");
        System.out.println("4. Search by Department");

        int choice = InputHandler.getInt("Enter choice: ");

        switch (choice) {
            case 1 -> searchById();
            case 2 -> searchByRollNumber();
            case 3 -> searchByName();
            case 4 -> searchByDepartment();
            default -> System.out.println("Invalid choice.");
        }

        InputHandler.pause();
    }

    private void searchById() {
        String id = InputHandler.getString("Enter Student ID: ");
        Student student = studentService.findById(id);
        displayStudentOrNotFound(student);
    }

    private void searchByRollNumber() {
        String roll = InputHandler.getString("Enter Roll Number: ");
        Student student = studentService.findByRollNumber(roll);
        displayStudentOrNotFound(student);
    }

    private void searchByName() {
        String name = InputHandler.getString("Enter Name (partial match allowed): ");
        List<Student> results = studentService.searchByName(name);
        displayStudentList(results);
    }

    private void searchByDepartment() {
        String dept = InputHandler.getString("Enter Department: ");
        List<Student> results = studentService.findByDepartment(dept);
        displayStudentList(results);
    }

    private void displayStudentOrNotFound(Student student) {
        if (student != null) {
            System.out.println("\nStudent Found:");
            System.out.println(student);
        } else {
            System.out.println("Student not found!");
        }
    }

    private void displayStudentList(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students found!");
        } else {
            System.out.println("\nFound " + students.size() + " student(s):");
            for (Student s : students) {
                System.out.println(s);
            }
        }
    }

    private void updateStudent() {
        printHeader("UPDATE STUDENT");

        String id = InputHandler.getString("Enter Student ID: ");
        Student existing = studentService.findById(id);

        if (existing == null) {
            System.out.println("Student not found!");
            InputHandler.pause();
            return;
        }

        System.out.println("Current Details: " + existing);
        System.out.println("\nEnter new details (or press Enter to keep current value):");

        String name = InputHandler.getString("New Name [" + existing.getName() + "]: ");
        String email = InputHandler.getEmail("New Email [" + existing.getEmail() + "]: ");
        String department = InputHandler.getString("New Department [" + existing.getDepartment() + "]: ");

        if (!name.isEmpty()) existing.setName(name);
        if (!email.isEmpty()) existing.setEmail(email);
        if (!department.isEmpty()) existing.setDepartment(department);

        if (studentService.updateStudent(id, existing)) {
            System.out.println("Student updated successfully!");
        } else {
            System.out.println("Error updating student!");
        }

        InputHandler.pause();
    }

    private void deleteStudent() {
        printHeader("DELETE STUDENT");

        String id = InputHandler.getString("Enter Student ID to delete: ");

        System.out.print("Are you sure? (yes/no): ");
        String confirm = InputHandler.getYesNo("");

        if (confirm.equals("yes")) {
            if (studentService.deleteStudent(id)) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student not found!");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }

        InputHandler.pause();
    }

    private void addGrade() {
        printHeader("ADD GRADE");

        String id = InputHandler.getString("Enter Student ID: ");
        Student student = studentService.findById(id);

        if (student == null) {
            System.out.println("Student not found!");
            InputHandler.pause();
            return;
        }

        String subject = InputHandler.getString("Enter Subject: ");
        double score = InputHandler.getDouble("Enter Score: ");
        double maxScore = InputHandler.getDouble("Enter Max Score (default 100): ");
        if (maxScore <= 0) maxScore = 100;

        String semester = InputHandler.getString("Enter Semester (e.g., Fall 2024): ");

        Grade grade = new Grade(subject, score, maxScore, semester);

        if (studentService.addGradeToStudent(id, grade)) {
            System.out.println("Grade added successfully!");
        } else {
            System.out.println("Error adding grade!");
        }

        InputHandler.pause();
    }

    private void showAnalytics() {
        printHeader("ANALYTICS & STATISTICS");

        List<Student> students = studentService.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students to analyze. Add some students first!");
            InputHandler.pause();
            return;
        }

        System.out.println("Total Students: " + studentService.getTotalStudents());
        System.out.println("Class Average: " + String.format("%.2f", analyticsService.getClassAverage()));

        System.out.println("\n--- Department Statistics ---");
        Map<String, Integer> deptStats = studentService.getDepartmentStatistics();
        for (Map.Entry<String, Integer> entry : deptStats.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " students");
        }

        System.out.println("\n--- Department-wise Average ---");
        Map<String, Double> deptAvg = analyticsService.getDepartmentWiseAverage();
        for (Map.Entry<String, Double> entry : deptAvg.entrySet()) {
            System.out.println(entry.getKey() + ": " + String.format("%.2f", entry.getValue()));
        }

        System.out.println("\n--- Grade Distribution ---");
        Map<String, Integer> gradeDist = analyticsService.getGradeDistribution();
        for (Map.Entry<String, Integer> entry : gradeDist.entrySet()) {
            System.out.println("Grade " + entry.getKey() + ": " + entry.getValue() + " students");
        }

        InputHandler.pause();
    }

    private void showTopPerformers() {
        printHeader("TOP PERFORMERS");

        int limit = InputHandler.getInt("How many top students to show? (default 5): ");
        if (limit <= 0) limit = 5;

        List<Student> topStudents = analyticsService.getStudentsAboveThreshold(60);

        if (topStudents.isEmpty()) {
            System.out.println("No students found above threshold.");
        } else {
            int count = Math.min(limit, topStudents.size());
            System.out.println("Top " + count + " Students (Average >= 60%):");
            System.out.println("-".repeat(80));

            for (int i = 0; i < count; i++) {
                Student s = topStudents.get(i);
                System.out.println((i + 1) + ". " + s.getName() + " - " +
                        String.format("%.2f", s.calculateAverage()) + "% (" + s.getGradeLevel() + ")");
            }
        }

        InputHandler.pause();
    }

    private void printHeader(String title) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  " + title);
        System.out.println("=".repeat(60));
    }
}
