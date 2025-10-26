package com.kiranastore.dao;

import com.kiranastore.database.DatabaseConnection;
import com.kiranastore.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private static final Logger logger = LoggerFactory.getLogger(ProductDAO.class);

    public boolean addProduct(Product product) {
        String sql = "INSERT INTO products (name, sku, category, buying_price, selling_price, quantity_in_stock, reorder_level) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getSku());
            pstmt.setString(3, product.getCategory());
            pstmt.setDouble(4, product.getBuyingPrice());
            pstmt.setDouble(5, product.getSellingPrice());
            pstmt.setInt(6, product.getQuantityInStock());
            pstmt.setInt(7, product.getReorderLevel());

            pstmt.executeUpdate();
            logger.info("Product added: {}", product.getName());
            return true;
        } catch (SQLException e) {
            logger.error("Error adding product", e);
            return false;
        }
    }

    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, sku = ?, category = ?, buying_price = ?, " +
                     "selling_price = ?, quantity_in_stock = ?, reorder_level = ?, updated_at = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getSku());
            pstmt.setString(3, product.getCategory());
            pstmt.setDouble(4, product.getBuyingPrice());
            pstmt.setDouble(5, product.getSellingPrice());
            pstmt.setInt(6, product.getQuantityInStock());
            pstmt.setInt(7, product.getReorderLevel());
            pstmt.setInt(8, product.getId());

            pstmt.executeUpdate();
            logger.info("Product updated: {}", product.getName());
            return true;
        } catch (SQLException e) {
            logger.error("Error updating product", e);
            return false;
        }
    }

    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
            logger.info("Product deleted with ID: {}", productId);
            return true;
        } catch (SQLException e) {
            logger.error("Error deleting product", e);
            return false;
        }
    }

    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching product by ID", e);
        }
        return null;
    }

    public Product getProductBySku(String sku) {
        String sql = "SELECT * FROM products WHERE sku = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sku);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching product by SKU", e);
        }
        return null;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY name ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            logger.error("Error fetching all products", e);
        }
        return products;
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category = ? ORDER BY name ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching products by category", e);
        }
        return products;
    }

    public List<Product> getLowStockProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE quantity_in_stock <= reorder_level ORDER BY quantity_in_stock ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            logger.error("Error fetching low stock products", e);
        }
        return products;
    }

    public boolean updateStock(int productId, int newQuantity) {
        String sql = "UPDATE products SET quantity_in_stock = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error("Error updating stock", e);
            return false;
        }
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        return Product.builder()
            .id(rs.getInt("id"))
            .name(rs.getString("name"))
            .sku(rs.getString("sku"))
            .category(rs.getString("category"))
            .buyingPrice(rs.getDouble("buying_price"))
            .sellingPrice(rs.getDouble("selling_price"))
            .quantityInStock(rs.getInt("quantity_in_stock"))
            .reorderLevel(rs.getInt("reorder_level"))
            .build();
    }
}
