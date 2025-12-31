package com.drugstore.dao;

import com.drugstore.model.User;
import com.drugstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private static final String INSERT_USER = "INSERT INTO users (full_name, email, password_hash, phone, address) VALUES (?,?,?,?,?)";
    private static final String FIND_BY_EMAIL = "SELECT * FROM users WHERE email = ?";

    public boolean registerUser(User user) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_USER)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getAddress());
            return ps.executeUpdate() > 0;
        }
    }

    public User findByEmail(String email) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(FIND_BY_EMAIL)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                return user;
            }
            return null;
        }
    }
}
