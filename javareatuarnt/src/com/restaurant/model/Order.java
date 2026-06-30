package com.restaurant.model;

import com.restaurant.exception.InvalidInputException;

/**
 * Represents a customer's order.
 */
public class Order {
    private int id;
    private int customerId;
    private int menuItemId;
    private int quantity;

    public Order(int id, int customerId, int menuItemId, int quantity) throws InvalidInputException {
        this.id = id;
        this.customerId = customerId;
        this.menuItemId = menuItemId;
        setQuantity(quantity);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws InvalidInputException {
        if (quantity <= 0) {
            throw new InvalidInputException("Order quantity must be at least 1.");
        }
        this.quantity = quantity;
    }
}
