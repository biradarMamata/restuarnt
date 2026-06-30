package com.restaurant.dao;

import com.restaurant.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.restaurant.exception.InvalidInputException;

/**
 * Handles database connectivity and CRUD operations via JDBC.
 */
public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:restaurant.db";

    public DatabaseManager() {
        initializeDatabase();
    }

    /**
     * Initializes the SQLite database and creates required tables.
     */
    private void initializeDatabase() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "role TEXT NOT NULL)";

        String createMenuTable = "CREATE TABLE IF NOT EXISTS menu (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "price REAL NOT NULL)";

        String createOrdersTable = "CREATE TABLE IF NOT EXISTS orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "customer_id INTEGER NOT NULL," +
                "menu_item_id INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "FOREIGN KEY(customer_id) REFERENCES users(id)," +
                "FOREIGN KEY(menu_item_id) REFERENCES menu(id))";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            
            // Create tables
            stmt.execute(createUsersTable);
            stmt.execute(createMenuTable);
            stmt.execute(createOrdersTable);
            
            // Insert default admin if not exists
            insertDefaultAdmin(conn);
            
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }

    private void insertDefaultAdmin(Connection conn) {
        String checkAdmin = "SELECT count(*) FROM users WHERE username = 'admin'";
        String insertAdmin = "INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'ADMIN')";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkAdmin)) {
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute(insertAdmin);
            }
        } catch (SQLException e) {
            System.err.println("Error inserting default admin: " + e.getMessage());
        }
    }

    /**
     * Authenticates a user and returns their User object.
     */
    public User authenticateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int id = rs.getInt("id");
                String role = rs.getString("role");
                if ("ADMIN".equals(role)) {
                    return new Admin(id, username, password);
                } else {
                    return new Customer(id, username, password);
                }
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Registers a new customer.
     */
    public boolean registerCustomer(String username, String password) {
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, 'CUSTOMER')";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Registration error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all menu items.
     */
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        String query = "SELECT * FROM menu";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                items.add(new MenuItem(rs.getInt("id"), rs.getString("name"), rs.getDouble("price")));
            }
        } catch (SQLException | InvalidInputException e) {
            System.err.println("Error fetching menu: " + e.getMessage());
        }
        return items;
    }

    /**
     * Adds a new menu item.
     */
    public boolean addMenuItem(String name, double price) {
        String query = "INSERT INTO menu (name, price) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding menu item: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a menu item by ID.
     */
    public boolean deleteMenuItem(int id) {
        String query = "DELETE FROM menu WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting menu item: " + e.getMessage());
            return false;
        }
    }

    /**
     * Places a new order.
     */
    public boolean placeOrder(int customerId, int menuItemId, int quantity) {
        String query = "INSERT INTO orders (customer_id, menu_item_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, menuItemId);
            pstmt.setInt(3, quantity);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error placing order: " + e.getMessage());
            return false;
        }
    }
}
