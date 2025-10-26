package com.kiranastore.service;

import com.kiranastore.dao.ProductDAO;
import com.kiranastore.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductDAO productDAO = new ProductDAO();

    public boolean addProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            logger.warn("Product name is empty");
            return false;
        }
        if (product.getSku() == null || product.getSku().trim().isEmpty()) {
            logger.warn("Product SKU is empty");
            return false;
        }
        if (product.getSellingPrice() <= 0 || product.getBuyingPrice() <= 0) {
            logger.warn("Invalid prices");
            return false;
        }
        return productDAO.addProduct(product);
    }

    public boolean updateProduct(Product product) {
        return productDAO.updateProduct(product);
    }

    public boolean deleteProduct(int productId) {
        return productDAO.deleteProduct(productId);
    }

    public Product getProductById(int id) {
        return productDAO.getProductById(id);
    }

    public Product getProductBySku(String sku) {
        return productDAO.getProductBySku(sku);
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public List<Product> getProductsByCategory(String category) {
        return productDAO.getProductsByCategory(category);
    }

    public List<Product> getLowStockProducts() {
        return productDAO.getLowStockProducts();
    }

    public List<String> getAllCategories() {
        return getAllProducts().stream()
            .map(Product::getCategory)
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }

    public boolean updateStock(int productId, int newQuantity) {
        if (newQuantity < 0) {
            logger.warn("Invalid quantity: {}", newQuantity);
            return false;
        }
        return productDAO.updateStock(productId, newQuantity);
    }

    public double getTotalInventoryValue() {
        return getAllProducts().stream()
            .mapToDouble(p -> p.getSellingPrice() * p.getQuantityInStock())
            .sum();
    }

    public double getTotalInventoryCost() {
        return getAllProducts().stream()
            .mapToDouble(p -> p.getBuyingPrice() * p.getQuantityInStock())
            .sum();
    }

    public int getTotalProductCount() {
        return getAllProducts().size();
    }

    public int getTotalStockQuantity() {
        return getAllProducts().stream()
            .mapToInt(Product::getQuantityInStock)
            .sum();
    }
}
