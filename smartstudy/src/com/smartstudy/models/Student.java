package com.smartstudy.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Student {
    private String id;
    private String name;
    private String email;
    private String rollNumber;
    private String department;
    private List<Grade> grades;
    private LocalDateTime createdAt;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Student() {
        this.id = generateId();
        this.grades = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    public Student(String name, String email, String rollNumber, String department) {
        this();
        this.name = name;
        this.email = email;
        this.rollNumber = rollNumber;
        this.department = department;
    }

    private String generateId() {
        return "STU" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public List<Grade> getGrades() { return grades; }
    public void setGrades(List<Grade> grades) { this.grades = grades; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public void addGrade(Grade grade) {
        this.grades.add(grade);
    }

    public double calculateAverage() {
        if (grades.isEmpty()) return 0.0;
        double sum = grades.stream().mapToDouble(Grade::getScore).sum();
        return sum / grades.size();
    }

    public String getGradeLevel() {
        double avg = calculateAverage();
        if (avg >= 90) return "A";
        else if (avg >= 80) return "B";
        else if (avg >= 70) return "C";
        else if (avg >= 60) return "D";
        else return "F";
    }

    public String toFileString() {
        return id + "," + name + "," + email + "," + rollNumber + "," + department + "," + createdAt.format(FORMATTER);
    }

    public static Student fromFileString(String line) {
        try {
            String[] parts = line.split(",");
            Student student = new Student();
            student.id = parts[0];
            student.name = parts[1];
            student.email = parts[2];
            student.rollNumber = parts[3];
            student.department = parts[4];
            student.createdAt = LocalDateTime.parse(parts[5], FORMATTER);
            return student;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s | Roll: %s | Dept: %s | Avg: %.2f (%s)",
                id, name, rollNumber, department, calculateAverage(), getGradeLevel());
    }
}
