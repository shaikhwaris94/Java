package com.smartstudy.services;

import com.smartstudy.models.Student;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsService {
    private StudentService studentService;

    public AnalyticsService(StudentService studentService) {
        this.studentService = studentService;
    }

    public double getClassAverage() {
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) return 0.0;

        double totalAverage = students.stream()
                .mapToDouble(Student::calculateAverage)
                .sum();
        return totalAverage / students.size();
    }

    public Map<String, Double> getDepartmentWiseAverage() {
        List<Student> students = studentService.getAllStudents();
        Map<String, List<Student>> byDepartment = new HashMap<>();

        for (Student student : students) {
            byDepartment.computeIfAbsent(student.getDepartment(), k -> new ArrayList<>())
                    .add(student);
        }

        Map<String, Double> deptAvg = new HashMap<>();
        for (Map.Entry<String, List<Student>> entry : byDepartment.entrySet()) {
            double avg = entry.getValue().stream()
                    .mapToDouble(Student::calculateAverage)
                    .average()
                    .orElse(0.0);
            deptAvg.put(entry.getKey(), avg);
        }
        return deptAvg;
    }

    public List<Student> getStudentsAboveThreshold(double threshold) {
        return studentService.getAllStudents().stream()
                .filter(s -> s.calculateAverage() >= threshold)
                .sorted((s1, s2) -> Double.compare(s2.calculateAverage(), s1.calculateAverage()))
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getGradeDistribution() {
        Map<String, Integer> distribution = new HashMap<>();
        distribution.put("A", 0);
        distribution.put("B", 0);
        distribution.put("C", 0);
        distribution.put("D", 0);
        distribution.put("F", 0);

        for (Student student : studentService.getAllStudents()) {
            String grade = student.getGradeLevel();
            distribution.merge(grade, 1, Integer::sum);
        }
        return distribution;
    }

    public List<Map.Entry<String, Integer>> getTopDepartments() {
        return studentService.getDepartmentStatistics().entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());
    }
}
