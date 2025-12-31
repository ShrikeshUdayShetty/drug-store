package com.drugstore.dao;

import com.drugstore.model.Vendor;
import com.drugstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VendorDao {

    private static final String INSERT_VENDOR = "INSERT INTO vendors (store_name, contact_person, email, password_hash, phone) VALUES (?,?,?,?,?)";
    private static final String FIND_BY_EMAIL = "SELECT * FROM vendors WHERE email = ?";

    public boolean registerVendor(Vendor vendor) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_VENDOR)) {
            ps.setString(1, vendor.getStoreName());
            ps.setString(2, vendor.getContactPerson());
            ps.setString(3, vendor.getEmail());
            ps.setString(4, vendor.getPasswordHash());
            ps.setString(5, vendor.getPhone());
            return ps.executeUpdate() > 0;
        }
    }

    public Vendor findByEmail(String email) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(FIND_BY_EMAIL)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Vendor vendor = new Vendor();
                vendor.setId(rs.getInt("id"));
                vendor.setStoreName(rs.getString("store_name"));
                vendor.setContactPerson(rs.getString("contact_person"));
                vendor.setEmail(rs.getString("email"));
                vendor.setPasswordHash(rs.getString("password_hash"));
                vendor.setPhone(rs.getString("phone"));
                vendor.setCreatedAt(rs.getTimestamp("created_at"));
                return vendor;
            }
            return null;
        }
    }
}
