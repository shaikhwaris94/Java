package com.smartstudy;

import com.smartstudy.ui.MenuSystem;

public class Main {
    public static void main(String[] args) {
        System.out.println("Initializing Student Record Management System...");

        try {
            MenuSystem menu = new MenuSystem();
            menu.showMainMenu();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Goodbye!");
        }
    }
}
