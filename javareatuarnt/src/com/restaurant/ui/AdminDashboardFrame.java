package com.restaurant.ui;

import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.Admin;
import com.restaurant.model.MenuItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Dashboard for Admin users to manage the menu.
 */
public class AdminDashboardFrame extends JFrame {
    private DatabaseManager dbManager;
    private Admin admin;
    private JTable menuTable;
    private DefaultTableModel tableModel;

    public AdminDashboardFrame(DatabaseManager dbManager, Admin admin) {
        this.dbManager = dbManager;
        this.admin = admin;
        setTitle("Admin Dashboard - " + admin.getUsername());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        loadMenuData();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());

        // Table setup
        String[] columns = {"ID", "Item Name", "Price"};
        tableModel = new DefaultTableModel(columns, 0);
        menuTable = new JTable(tableModel);
        panel.add(new JScrollPane(menuTable), BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Item");
        JButton deleteButton = new JButton("Delete Item");
        JButton logoutButton = new JButton("Logout");

        controlPanel.add(addButton);
        controlPanel.add(deleteButton);
        controlPanel.add(logoutButton);

        panel.add(controlPanel, BorderLayout.SOUTH);
        add(panel);

        // Actions
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItemDialog();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedItem();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginFrame(dbManager).setVisible(true);
            }
        });
    }

    private void loadMenuData() {
        tableModel.setRowCount(0);
        List<MenuItem> items = dbManager.getAllMenuItems();
        for (MenuItem item : items) {
            tableModel.addRow(new Object[]{item.getId(), item.getName(), item.getPrice()});
        }
    }

    private void addItemDialog() {
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        Object[] message = {
            "Item Name:", nameField,
            "Price:", priceField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Menu Item", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());

                if (name.isEmpty() || price < 0) {
                    JOptionPane.showMessageDialog(this, "Invalid input values.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (dbManager.addMenuItem(name, price)) {
                    JOptionPane.showMessageDialog(this, "Item added successfully.");
                    loadMenuData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add item.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Price must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedItem() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this item?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (dbManager.deleteMenuItem(id)) {
                    JOptionPane.showMessageDialog(this, "Item deleted.");
                    loadMenuData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete item.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
