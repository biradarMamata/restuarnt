package com.restaurant.model;

/**
 * Admin class extending User.
 */
public class Admin extends User {
    public Admin(int id, String username, String password) {
        super(id, username, password, "ADMIN");
    }

    @Override
    public void displayDetails() {
        System.out.println("Admin Account: " + getUsername());
    }
}
