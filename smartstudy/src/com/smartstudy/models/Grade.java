package com.smartstudy.models;

public class Grade {
    private String subject;
    private double score;
    private double maxScore;
    private String semester;

    public Grade() {
        this.maxScore = 100.0;
    }

    public Grade(String subject, double score, double maxScore, String semester) {
        this();
        this.subject = subject;
        this.score = score;
        this.maxScore = maxScore;
        this.semester = semester;
    }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public double getMaxScore() { return maxScore; }
    public void setMaxScore(double maxScore) { this.maxScore = maxScore; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public double getPercentage() {
        return (score / maxScore) * 100;
    }

    public String toFileString() {
        return subject + "," + score + "," + maxScore + "," + semester;
    }

    public static Grade fromFileString(String line) {
        try {
            String[] parts = line.split(",");
            return new Grade(parts[0], Double.parseDouble(parts[1]),
                    Double.parseDouble(parts[2]), parts[3]);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("%s: %.2f/%.2f (%s)", subject, score, maxScore, semester);
    }
}
