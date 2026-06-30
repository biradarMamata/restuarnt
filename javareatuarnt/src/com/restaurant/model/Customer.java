package com.restaurant.model;

/**
 * Customer class extending User.
 */
public class Customer extends User {
    public Customer(int id, String username, String password) {
        super(id, username, password, "CUSTOMER");
    }

    @Override
    public void displayDetails() {
        System.out.println("Customer Account: " + getUsername());
    }
}
