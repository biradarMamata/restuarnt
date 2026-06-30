package com.restaurant.model;

/**
 * Interface representing common behaviors for any system account.
 */
public interface Account {
    int getId();
    String getUsername();
    String getRole();
    void displayDetails();
}
