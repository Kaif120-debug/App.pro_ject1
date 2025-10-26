package com.kiranastore.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Create products table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS products (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL UNIQUE," +
                "sku VARCHAR(100) NOT NULL UNIQUE," +
                "category VARCHAR(100) NOT NULL," +
                "buying_price DECIMAL(10,2) NOT NULL," +
                "selling_price DECIMAL(10,2) NOT NULL," +
                "quantity_in_stock INT NOT NULL DEFAULT 0," +
                "reorder_level INT NOT NULL DEFAULT 10," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                ")"
            );
            logger.info("Products table created/verified");

            // Create invoices table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS invoices (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "invoice_number VARCHAR(50) NOT NULL UNIQUE," +
                "customer_name VARCHAR(255) NOT NULL," +
                "customer_phone VARCHAR(20)," +
                "total_items INT NOT NULL," +
                "subtotal DECIMAL(10,2) NOT NULL," +
                "tax DECIMAL(10,2) DEFAULT 0," +
                "total_amount DECIMAL(10,2) NOT NULL," +
                "payment_method VARCHAR(50) NOT NULL," +
                "invoice_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );
            logger.info("Invoices table created/verified");

            // Create invoice items table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS invoice_items (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "invoice_id INT NOT NULL," +
                "product_id INT NOT NULL," +
                "product_name VARCHAR(255) NOT NULL," +
                "quantity INT NOT NULL," +
                "unit_price DECIMAL(10,2) NOT NULL," +
                "line_total DECIMAL(10,2) NOT NULL," +
                "FOREIGN KEY (invoice_id) REFERENCES invoices(id)," +
                "FOREIGN KEY (product_id) REFERENCES products(id)" +
                ")"
            );
            logger.info("Invoice items table created/verified");

            // Create sales report table (aggregated data)
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS sales_reports (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "sale_date DATE NOT NULL," +
                "total_sales DECIMAL(10,2) NOT NULL," +
                "total_profit DECIMAL(10,2) NOT NULL," +
                "total_transactions INT NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );
            logger.info("Sales reports table created/verified");

            // Create stock movements table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS stock_movements (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "product_id INT NOT NULL," +
                "movement_type VARCHAR(50) NOT NULL," +
                "quantity INT NOT NULL," +
                "reference_id INT," +
                "notes TEXT," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (product_id) REFERENCES products(id)" +
                ")"
            );
            logger.info("Stock movements table created/verified");

            logger.info("Database initialization completed successfully");

        } catch (Exception e) {
            logger.error("Error initializing database", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }
}
