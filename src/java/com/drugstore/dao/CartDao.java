package com.drugstore.dao;

import com.drugstore.model.CartItem;
import com.drugstore.model.Medicine;
import com.drugstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartDao {

    private static final String UPSERT_CART =
            "INSERT INTO cart_items (user_id, medicine_id, quantity) "
            + "VALUES (?,?,?) "
            + "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";

    private static final String SELECT_BY_USER =
            "SELECT c.id as cart_id, c.user_id, c.quantity, c.created_at, "
            + "       m.* "
            + "FROM cart_items c "
            + "JOIN medicines m ON c.medicine_id = m.id "
            + "WHERE c.user_id = ? "
            + "ORDER BY c.created_at DESC";

    private static final String REMOVE_ITEM = "DELETE FROM cart_items WHERE user_id = ? AND medicine_id = ?";
    private static final String CLEAR_CART = "DELETE FROM cart_items WHERE user_id = ?";
    private static final String UPDATE_QUANTITY = "UPDATE cart_items SET quantity = ? WHERE user_id = ? AND medicine_id = ?";

    public void addToCart(int userId, int medicineId, int quantity) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPSERT_CART)) {
            ps.setInt(1, userId);
            ps.setInt(2, medicineId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        }
    }

    public List<CartItem> getCartItems(int userId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_USER)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            List<CartItem> items = new ArrayList<>();
            while (rs.next()) {
                CartItem item = new CartItem();
                item.setId(rs.getInt("cart_id"));
                item.setUserId(rs.getInt("user_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setCreatedAt(rs.getTimestamp("created_at"));

                Medicine med = new Medicine();
                med.setId(rs.getInt("id"));
                med.setVendorId(rs.getInt("vendor_id"));
                med.setName(rs.getString("name"));
                med.setDescription(rs.getString("description"));
                med.setManufacturingDate(rs.getDate("manufacturing_date"));
                med.setExpiryDate(rs.getDate("expiry_date"));
                med.setPricePerUnit(rs.getDouble("price_per_unit"));
                med.setStockQuantity(rs.getInt("stock_quantity"));
                med.setImageUrl(rs.getString("image_url"));
                med.setDiscountPercentage(rs.getDouble("discount_percentage"));
                med.setCreatedAt(rs.getTimestamp("created_at"));
                item.setMedicine(med);

                items.add(item);
            }
            return items;
        }
    }

    public void removeItem(int userId, int medicineId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(REMOVE_ITEM)) {
            ps.setInt(1, userId);
            ps.setInt(2, medicineId);
            ps.executeUpdate();
        }
    }

    public void updateQuantity(int userId, int medicineId, int quantity) throws SQLException {
        if (quantity <= 0) {
            removeItem(userId, medicineId);
            return;
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_QUANTITY)) {
            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, medicineId);
            ps.executeUpdate();
        }
    }

    public void clearCart(int userId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(CLEAR_CART)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }
}
