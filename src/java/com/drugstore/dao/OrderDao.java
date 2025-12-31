package com.drugstore.dao;

import com.drugstore.model.CartItem;
import com.drugstore.model.Medicine;
import com.drugstore.model.Order;
import com.drugstore.model.OrderItem;
import com.drugstore.model.User;
import com.drugstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDao {

    private static final String INSERT_ORDER = "INSERT INTO orders (user_id, total_amount, discount_amount, net_amount, status) VALUES (?,?,?,?,?)";

    private static final String INSERT_ORDER_ITEM =
            "INSERT INTO order_items (order_id, medicine_id, vendor_id, quantity, unit_price, discount_percentage) "
            + "VALUES (?,?,?,?,?,?)";

    private static final String SELECT_ORDERS_BY_USER = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";

    private static final String SELECT_ITEMS_BY_ORDER =
            "SELECT oi.*, m.name, m.image_url, v.store_name "
            + "FROM order_items oi "
            + "JOIN medicines m ON oi.medicine_id = m.id "
            + "JOIN vendors v ON oi.vendor_id = v.id "
            + "WHERE oi.order_id = ?";

    private static final String SELECT_ORDERS_FOR_VENDOR =
            "SELECT o.*, u.full_name, u.email "
            + "FROM orders o "
            + "JOIN order_items oi ON o.id = oi.order_id "
            + "JOIN users u ON o.user_id = u.id "
            + "WHERE oi.vendor_id = ? "
            + "ORDER BY o.created_at DESC";

    private static final String SELECT_VENDOR_ITEMS_FOR_ORDER =
            "SELECT oi.*, m.name, m.image_url "
            + "FROM order_items oi "
            + "JOIN medicines m ON oi.medicine_id = m.id "
            + "WHERE oi.order_id = ? AND oi.vendor_id = ?";

    private static final String SELECT_ORDER_STATUS_FOR_VENDOR =
            "SELECT o.status FROM orders o "
            + "JOIN order_items oi ON o.id = oi.order_id "
            + "WHERE o.id = ? AND oi.vendor_id = ?";

    private static final String SELECT_ORDER_FOR_CANCEL =
            "SELECT status, created_at FROM orders WHERE id = ? AND user_id = ?";

    private static final String UPDATE_ORDER_STATUS =
            "UPDATE orders SET status = ? WHERE id = ?";

    private static final String SUM_TOTAL_FOR_USER =
            "SELECT COALESCE(SUM(net_amount), 0) as total FROM orders WHERE user_id = ? AND status <> 'CANCELLED'";

    private static final String COUNT_PENDING_FOR_VENDOR =
            "SELECT COUNT(DISTINCT o.id) AS total "
            + "FROM orders o "
            + "JOIN order_items oi ON o.id = oi.order_id "
            + "WHERE oi.vendor_id = ? AND o.status NOT IN ('CANCELLED', 'DELIVERED')";

    private static final String SUM_TOTAL_FOR_VENDOR =
            "SELECT COALESCE(SUM(oi.quantity * oi.unit_price * (1 - oi.discount_percentage / 100)), 0) AS total "
            + "FROM orders o "
            + "JOIN order_items oi ON o.id = oi.order_id "
            + "WHERE oi.vendor_id = ? AND o.status <> 'CANCELLED'";

    public int createOrder(User user, List<CartItem> cartItems) throws SQLException {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart cannot be empty");
        }

        double total = 0;
        double discount = 0;

        for (CartItem item : cartItems) {
            Medicine med = item.getMedicine();
            double itemTotal = med.getPricePerUnit() * item.getQuantity();
            double itemDiscount = itemTotal * med.getDiscountPercentage() / 100;
            total += itemTotal;
            discount += itemDiscount;
        }
        double net = total - discount;

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement orderStmt = connection.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, user.getId());
                orderStmt.setDouble(2, total);
                orderStmt.setDouble(3, discount);
                orderStmt.setDouble(4, net);
                orderStmt.setString(5, "PLACED");
                orderStmt.executeUpdate();

                ResultSet keys = orderStmt.getGeneratedKeys();
                if (keys.next()) {
                    int orderId = keys.getInt(1);

                    try (PreparedStatement itemStmt = connection.prepareStatement(INSERT_ORDER_ITEM)) {
                        for (CartItem item : cartItems) {
                            Medicine med = item.getMedicine();
                            itemStmt.setInt(1, orderId);
                            itemStmt.setInt(2, med.getId());
                            itemStmt.setInt(3, med.getVendorId());
                            itemStmt.setInt(4, item.getQuantity());
                            itemStmt.setDouble(5, med.getPricePerUnit());
                            itemStmt.setDouble(6, med.getDiscountPercentage());
                            itemStmt.addBatch();
                        }
                        itemStmt.executeBatch();
                    }

                    connection.commit();
                    return orderId;
                } else {
                    connection.rollback();
                    throw new SQLException("Failed to retrieve order id");
                }
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public List<Order> getOrdersByUser(int userId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ORDERS_BY_USER)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order order = mapOrder(rs);
                order.setItems(getOrderItems(order.getId(), connection));
                orders.add(order);
            }
            return orders;
        }
    }

    public List<Order> getOrdersForVendor(int vendorId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ORDERS_FOR_VENDOR)) {
            ps.setInt(1, vendorId);
            ResultSet rs = ps.executeQuery();
            Map<Integer, Order> orderMap = new HashMap<>();
            while (rs.next()) {
                int orderId = rs.getInt("id");
                Order order = orderMap.get(orderId);
                if (order == null) {
                    order = mapOrder(rs);
                    order.setUserName(rsGetStringSafe(rs, "full_name"));
                    order.setUserEmail(rsGetStringSafe(rs, "email"));
                    orderMap.put(orderId, order);
                }
            }

            for (Order order : orderMap.values()) {
                order.setItems(getOrderItemsForVendor(order.getId(), vendorId, connection));
            }

            return new ArrayList<>(orderMap.values());
        }
    }

    private List<OrderItem> getOrderItems(int orderId, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ITEMS_BY_ORDER)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            List<OrderItem> items = new ArrayList<>();
            while (rs.next()) {
                items.add(mapOrderItem(rs));
            }
            return items;
        }
    }

    private List<OrderItem> getOrderItemsForVendor(int orderId, int vendorId, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_VENDOR_ITEMS_FOR_ORDER)) {
            ps.setInt(1, orderId);
            ps.setInt(2, vendorId);
            ResultSet rs = ps.executeQuery();
            List<OrderItem> items = new ArrayList<>();
            while (rs.next()) {
                items.add(mapOrderItem(rs));
            }
            return items;
        }
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("user_id"));
        order.setTotalAmount(rs.getDouble("total_amount"));
        order.setDiscountAmount(rs.getDouble("discount_amount"));
        order.setNetAmount(rs.getDouble("net_amount"));
        order.setStatus(rs.getString("status"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        order.setUpdatedAt(rs.getTimestamp("updated_at"));
        return order;
    }

    private OrderItem mapOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getInt("id"));
        item.setOrderId(rs.getInt("order_id"));

        Medicine med = new Medicine();
        med.setId(rs.getInt("medicine_id"));
        med.setName(rs.getString("name"));
        med.setImageUrl(rs.getString("image_url"));
        item.setMedicine(med);

        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getDouble("unit_price"));
        item.setDiscountPercentage(rs.getDouble("discount_percentage"));
        return item;
    }

    public boolean cancelOrder(int orderId, int userId, int windowMinutes) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement select = connection.prepareStatement(SELECT_ORDER_FOR_CANCEL)) {
            select.setInt(1, orderId);
            select.setInt(2, userId);
            ResultSet rs = select.executeQuery();
            if (rs.next()) {
                String status = rs.getString("status");
                java.sql.Timestamp createdAt = rs.getTimestamp("created_at");
                if (!"PLACED".equalsIgnoreCase(status) || createdAt == null) {
                    return false;
                }
                long minutes = (System.currentTimeMillis() - createdAt.getTime()) / 60000;
                if (minutes > windowMinutes) {
                    return false;
                }
                try (PreparedStatement update = connection.prepareStatement(UPDATE_ORDER_STATUS)) {
                    update.setString(1, "CANCELLED");
                    update.setInt(2, orderId);
                    return update.executeUpdate() > 0;
                }
            }
            return false;
        }
    }

    public boolean markOrderDelivered(int orderId, int vendorId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement select = connection.prepareStatement(SELECT_ORDER_STATUS_FOR_VENDOR)) {
            select.setInt(1, orderId);
            select.setInt(2, vendorId);
            ResultSet rs = select.executeQuery();
            if (!rs.next()) {
                return false;
            }
            String status = rs.getString("status");
            if (status == null || "CANCELLED".equalsIgnoreCase(status)) {
                return false;
            }
            if ("DELIVERED".equalsIgnoreCase(status)) {
                return true;
            }
            try (PreparedStatement update = connection.prepareStatement(UPDATE_ORDER_STATUS)) {
                update.setString(1, "DELIVERED");
                update.setInt(2, orderId);
                return update.executeUpdate() > 0;
            }
        }
    }

    public double getTotalSpent(int userId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SUM_TOTAL_FOR_USER)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
            return 0;
        }
    }

    public int countPendingOrdersForVendor(int vendorId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(COUNT_PENDING_FOR_VENDOR)) {
            ps.setInt(1, vendorId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        }
    }

    public double getTotalSalesForVendor(int vendorId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SUM_TOTAL_FOR_VENDOR)) {
            ps.setInt(1, vendorId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
            return 0;
        }
    }

    private Integer rsGetIntSafe(ResultSet rs, String column) {
        try {
            return rs.getInt(column);
        } catch (SQLException e) {
            return null;
        }
    }

    private Double rsGetDoubleSafe(ResultSet rs, String column) {
        try {
            return rs.getDouble(column);
        } catch (SQLException e) {
            return null;
        }
    }

    private String rsGetStringSafe(ResultSet rs, String column) {
        try {
            return rs.getString(column);
        } catch (SQLException e) {
            return null;
        }
    }

    private java.sql.Timestamp rsGetTimestampSafe(ResultSet rs, String column) {
        try {
            return rs.getTimestamp(column);
        } catch (SQLException e) {
            return null;
        }
    }
}
