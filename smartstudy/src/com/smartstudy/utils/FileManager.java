package com.smartstudy.utils;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String DATA_DIR = "data";

    static {
        ensureDirectoryExists(DATA_DIR);
    }

    public static void ensureDirectoryExists(String dir) {
        try {
            Files.createDirectories(Paths.get(dir));
        } catch (IOException e) {
            System.err.println("Error creating directory: " + e.getMessage());
        }
    }

    public static List<String> readFromFile(String filename) {
        List<String> lines = new ArrayList<>();
        String fullPath = DATA_DIR + "/" + filename;

        try (BufferedReader reader = new BufferedReader(new FileReader(fullPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found (will be created): " + filename);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("Permission denied: " + e.getMessage());
        }

        return lines;
    }

    public static boolean writeToFile(String filename, List<String> lines) {
        String fullPath = DATA_DIR + "/" + filename;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("Permission denied: " + e.getMessage());
        }

        return false;
    }

    public static boolean appendToFile(String filename, String line) {
        String fullPath = DATA_DIR + "/" + filename;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath, true))) {
            writer.write(line);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error appending to file: " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("Permission denied: " + e.getMessage());
        }

        return false;
    }

    public static boolean fileExists(String filename) {
        String fullPath = DATA_DIR + "/" + filename;
        return Files.exists(Paths.get(fullPath));
    }

    public static boolean deleteFile(String filename) {
        String fullPath = DATA_DIR + "/" + filename;

        try {
            Files.deleteIfExists(Paths.get(fullPath));
            return true;
        } catch (IOException e) {
            System.err.println("Error deleting file: " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("Permission denied: " + e.getMessage());
        }

        return false;
    }

    public static String getFullPath(String filename) {
        return DATA_DIR + "/" + filename;
    }
}
