package com.restaurant.ui;

import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.Admin;
import com.restaurant.model.Customer;
import com.restaurant.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Provides the UI for User Login and Registration.
 */
public class LoginFrame extends JFrame {
    private DatabaseManager dbManager;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        setTitle("Restaurant Management System - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register as Customer");

        panel.add(loginButton);
        panel.add(registerButton);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegistration();
            }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = dbManager.authenticateUser(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login Successful!");
            this.dispose();
            if (user instanceof Admin) {
                new AdminDashboardFrame(dbManager, (Admin) user).setVisible(true);
            } else if (user instanceof Customer) {
                new CustomerDashboardFrame(dbManager, (Customer) user).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegistration() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = dbManager.registerCustomer(username, password);
        if (success) {
            JOptionPane.showMessageDialog(this, "Registration Successful! Please login.");
        } else {
            JOptionPane.showMessageDialog(this, "Registration Failed. Username may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
