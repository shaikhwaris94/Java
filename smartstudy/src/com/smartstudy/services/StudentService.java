package com.smartstudy.services;

import com.smartstudy.models.Student;
import com.smartstudy.models.Grade;
import com.smartstudy.utils.FileManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentService {
    private List<Student> students;
    private Map<String, Student> studentMap;

    public StudentService() {
        this.students = new ArrayList<>();
        this.studentMap = new HashMap<>();
        loadStudents();
    }

    private void loadStudents() {
        List<String> lines = FileManager.readFromFile("students.txt");
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                Student student = Student.fromFileString(line);
                if (student != null) {
                    students.add(student);
                    studentMap.put(student.getId(), student);
                }
            }
        }
    }

    public void saveStudents() {
        List<String> lines = students.stream()
                .map(Student::toFileString)
                .collect(Collectors.toList());
        FileManager.writeToFile("students.txt", lines);
    }

    public boolean addStudent(Student student) {
        for (Student s : students) {
            if (s.getRollNumber().equals(student.getRollNumber())) {
                return false;
            }
        }
        students.add(student);
        studentMap.put(student.getId(), student);
        saveStudents();
        return true;
    }

    public boolean updateStudent(String id, Student updatedStudent) {
        Student existing = studentMap.get(id);
        if (existing != null) {
            existing.setName(updatedStudent.getName());
            existing.setEmail(updatedStudent.getEmail());
            existing.setDepartment(updatedStudent.getDepartment());
            saveStudents();
            return true;
        }
        return false;
    }

    public boolean deleteStudent(String id) {
        Student student = studentMap.get(id);
        if (student != null) {
            students.remove(student);
            studentMap.remove(id);
            saveStudents();
            return true;
        }
        return false;
    }

    public Student findById(String id) {
        return studentMap.get(id);
    }

    public Student findByRollNumber(String rollNumber) {
        return students.stream()
                .filter(s -> s.getRollNumber().equals(rollNumber))
                .findFirst()
                .orElse(null);
    }

    public List<Student> findByDepartment(String department) {
        return students.stream()
                .filter(s -> s.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }

    public List<Student> searchByName(String name) {
        return students.stream()
                .filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public int getTotalStudents() {
        return students.size();
    }

    public Map<String, Integer> getDepartmentStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        for (Student student : students) {
            stats.merge(student.getDepartment(), 1, Integer::sum);
        }
        return stats;
    }

    public boolean addGradeToStudent(String studentId, Grade grade) {
        Student student = studentMap.get(studentId);
        if (student != null) {
            student.addGrade(grade);
            saveStudents();
            return true;
        }
        return false;
    }

    public List<Student> getTopPerformers(int limit) {
        return students.stream()
                .sorted((s1, s2) -> Double.compare(s2.calculateAverage(), s1.calculateAverage()))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
