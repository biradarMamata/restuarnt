package com.restaurant.main;

import com.restaurant.dao.DatabaseManager;
import com.restaurant.ui.LoginFrame;

import javax.swing.*;

/**
 * Entry point for the Restaurant Management System.
 */
public class MainApplication {
    public static void main(String[] args) {
        // Ensure UI updates are handled on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Set System L&F
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    System.err.println("Could not set System Look and Feel.");
                }

                // Initialize Database and UI
                DatabaseManager dbManager = new DatabaseManager();
                LoginFrame loginFrame = new LoginFrame(dbManager);
                loginFrame.setVisible(true);
            }
        });
    }
}
