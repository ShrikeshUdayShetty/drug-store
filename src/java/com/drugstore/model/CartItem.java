package com.drugstore.model;

import java.sql.Timestamp;

public class CartItem {
    private int id;
    private int userId;
    private Medicine medicine;
    private int quantity;
    private Timestamp createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public double getSubtotal() {
        if (medicine == null) {
            return 0;
        }
        double discountedPrice = medicine.getPricePerUnit() * (1 - medicine.getDiscountPercentage() / 100);
        return discountedPrice * quantity;
    }
}
