package com.restaurant.model;

import com.restaurant.exception.InvalidInputException;

/**
 * Represents a single item on the restaurant menu.
 */
public class MenuItem {
    private int id;
    private String name;
    private double price;

    public MenuItem(int id, String name, double price) throws InvalidInputException {
        this.id = id;
        setName(name);
        setPrice(price);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidInputException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Menu item name cannot be empty.");
        }
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) throws InvalidInputException {
        if (price < 0) {
            throw new InvalidInputException("Menu item price cannot be negative.");
        }
        this.price = price;
    }
}
