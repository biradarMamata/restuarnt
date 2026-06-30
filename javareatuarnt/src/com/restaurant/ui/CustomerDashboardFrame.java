package com.restaurant.ui;

import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.Customer;
import com.restaurant.model.MenuItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Dashboard for Customers to view menu and place orders.
 */
public class CustomerDashboardFrame extends JFrame {
    private DatabaseManager dbManager;
    private Customer customer;
    private JTable menuTable;
    private DefaultTableModel tableModel;

    public CustomerDashboardFrame(DatabaseManager dbManager, Customer customer) {
        this.dbManager = dbManager;
        this.customer = customer;
        setTitle("Customer Dashboard - Welcome " + customer.getUsername());
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
        JButton orderButton = new JButton("Place Order");
        JButton logoutButton = new JButton("Logout");

        controlPanel.add(orderButton);
        controlPanel.add(logoutButton);

        panel.add(controlPanel, BorderLayout.SOUTH);
        add(panel);

        // Actions
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeOrderDialog();
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

    private void placeOrderDialog() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow != -1) {
            int menuItemId = (int) tableModel.getValueAt(selectedRow, 0);
            String itemName = (String) tableModel.getValueAt(selectedRow, 1);

            String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity for " + itemName + ":", "1");
            if (qtyStr != null && !qtyStr.trim().isEmpty()) {
                try {
                    int quantity = Integer.parseInt(qtyStr.trim());
                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(this, "Quantity must be at least 1.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (dbManager.placeOrder(customer.getId(), menuItemId, quantity)) {
                        JOptionPane.showMessageDialog(this, "Order placed successfully!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to place order.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid quantity entered.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item from the menu to order.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
