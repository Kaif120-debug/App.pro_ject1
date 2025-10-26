package com.kiranastore.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    
    // Database configuration - you can change these values
    private static final String DB_TYPE = "mysql"; // Change to "sqlite" to use SQLite
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/kiranastore?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String MYSQL_USERNAME = "root";
    private static final String MYSQL_PASSWORD = "kaif123"; // Change this to your MySQL password
    private static final String SQLITE_URL = "jdbc:sqlite:kiranastore.db";
    
    private static Connection connection;

    static {
        try {
            if ("mysql".equals(DB_TYPE)) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                logger.info("MySQL JDBC driver loaded successfully");
            } else {
                Class.forName("org.sqlite.JDBC");
                logger.info("SQLite JDBC driver loaded successfully");
            }
        } catch (ClassNotFoundException e) {
            logger.error("JDBC driver not found", e);
            throw new RuntimeException("Failed to load JDBC driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            if ("mysql".equals(DB_TYPE)) {
                connection = DriverManager.getConnection(MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD);
                logger.info("Connected to MySQL database");
            } else {
                connection = DriverManager.getConnection(SQLITE_URL);
                logger.info("Connected to SQLite database");
            }
            connection.setAutoCommit(true);
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Database connection closed");
            }
        } catch (SQLException e) {
            logger.error("Error closing database connection", e);
        }
    }
}
