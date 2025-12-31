package com.drugstore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = buildJdbcUrl();
    private static final String USER = resolveEnv("DB_USER", "root");
    private static final String PASSWORD = resolveEnv("DB_PASSWORD", "toxFzzVjcVoCYmEsJSdqYFiCKcufEmEF");

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Unable to load MySQL JDBC Driver: " + e.getMessage());
        }
    }

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static String buildJdbcUrl() {
        String host = resolveEnv("DB_HOST", "crossover.proxy.rlwy.net");
        String port = resolveEnv("DB_PORT", "43536");
        String database = resolveEnv("DB_NAME", "railway");
        return String.format("jdbc:mysql://%s:%s/%s?useSSL=true&requireSSL=false", host, port, database);
    }

    private static String resolveEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.isEmpty()) ? defaultValue : value;
    }
}
