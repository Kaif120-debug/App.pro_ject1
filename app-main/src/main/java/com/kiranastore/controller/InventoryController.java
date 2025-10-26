package com.kiranastore.controller;

import com.kiranastore.model.Product;
import com.kiranastore.service.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class InventoryController {
    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> categoryFilter;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, String> skuColumn;

    @FXML
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private TableColumn<Product, Double> buyingPriceColumn;

    @FXML
    private TableColumn<Product, Double> sellingPriceColumn;

    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    @FXML
    private TableColumn<Product, Integer> reorderLevelColumn;

    @FXML
    private Button addProductBtn;

    @FXML
    private Button editProductBtn;

    @FXML
    private Button deleteProductBtn;

    @FXML
    private Label totalProductsLabel;

    @FXML
    private Label totalStockLabel;

    @FXML
    private Label inventoryValueLabel;

    private ProductService productService;
    private ObservableList<Product> productList;

    @FXML
    public void initialize() {
        logger.info("Initializing InventoryController");
        productService = new ProductService();
        setupTableColumns();
        setupButtonHandlers();
        setupFilters();
        loadProducts();
    }

    private void setupTableColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        skuColumn.setCellValueFactory(new PropertyValueFactory<>("sku"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        buyingPriceColumn.setCellValueFactory(new PropertyValueFactory<>("buyingPrice"));
        sellingPriceColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
        reorderLevelColumn.setCellValueFactory(new PropertyValueFactory<>("reorderLevel"));

        // Format price columns
        buyingPriceColumn.setCellFactory(tc -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty ? "" : String.format("Rs. %.2f", price));
            }
        });

        sellingPriceColumn.setCellFactory(tc -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty ? "" : String.format("Rs. %.2f", price));
            }
        });
    }

    private void setupButtonHandlers() {
        addProductBtn.setOnAction(e -> showAddProductDialog());
        editProductBtn.setOnAction(e -> showEditProductDialog());
        deleteProductBtn.setOnAction(e -> deleteSelectedProduct());
    }

    private void setupFilters() {
        ObservableList<String> categories = FXCollections.observableArrayList(productService.getAllCategories());
        categories.add(0, "All Categories");
        categoryFilter.setItems(categories);
        categoryFilter.setValue("All Categories");

        categoryFilter.setOnAction(e -> filterProducts());
        searchField.setOnKeyReleased(e -> filterProducts());
    }

    private void loadProducts() {
        productList = FXCollections.observableArrayList(productService.getAllProducts());
        productTable.setItems(productList);
        updateStatistics();
    }

    private void filterProducts() {
        String searchText = searchField.getText().toLowerCase();
        String selectedCategory = categoryFilter.getValue();

        ObservableList<Product> filtered = FXCollections.observableArrayList(productService.getAllProducts());

        if (!selectedCategory.equals("All Categories")) {
            filtered = filtered.filtered(p -> p.getCategory().equals(selectedCategory));
        }

        if (!searchText.isEmpty()) {
            filtered = filtered.filtered(p ->
                p.getName().toLowerCase().contains(searchText) ||
                p.getSku().toLowerCase().contains(searchText)
            );
        }

        productTable.setItems(filtered);
    }

    private void showAddProductDialog() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add New Product");
        dialog.setHeaderText("Enter product details");

        GridPane grid = createProductForm(null);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Convert the result to a Product object
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return createProductFromForm(grid);
            }
            return null;
        });

        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(product -> {
            logger.info("Add product dialog result: {}", product);
            if (product != null && productService.addProduct(product)) {
                loadProducts();
                showInfo("Product added successfully!");
            } else {
                showError("Failed to add product. Check if SKU is unique.");
            }
        });
    }

    private void showEditProductDialog() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Please select a product to edit");
            return;
        }

        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Edit Product");
        dialog.setHeaderText("Edit product details");

        GridPane grid = createProductForm(selected);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Convert the result to a Product object
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return createProductFromForm(grid, selected);
            }
            return null;
        });

        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(product -> {
            logger.info("Edit product dialog result: {}", product);
            if (product != null && productService.updateProduct(product)) {
                loadProducts();
                showInfo("Product updated successfully!");
            } else {
                showError("Failed to update product");
            }
        });
    }

    private GridPane createProductForm(Product product) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20));

        TextField nameField = new TextField(product != null ? product.getName() : "");
        nameField.setPromptText("Product name");

        TextField skuField = new TextField(product != null ? product.getSku() : "");
        skuField.setPromptText("SKU");

        TextField categoryField = new TextField(product != null ? product.getCategory() : "");
        categoryField.setPromptText("Category");

        TextField buyingPriceField = new TextField(product != null ? String.valueOf(product.getBuyingPrice()) : "");
        buyingPriceField.setPromptText("Buying price");

        TextField sellingPriceField = new TextField(product != null ? String.valueOf(product.getSellingPrice()) : "");
        sellingPriceField.setPromptText("Selling price");

        TextField quantityField = new TextField(product != null ? String.valueOf(product.getQuantityInStock()) : "");
        quantityField.setPromptText("Quantity");

        TextField reorderField = new TextField(product != null ? String.valueOf(product.getReorderLevel()) : "");
        reorderField.setPromptText("Reorder level");

        grid.add(new Label("Product Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("SKU:"), 0, 1);
        grid.add(skuField, 1, 1);
        grid.add(new Label("Category:"), 0, 2);
        grid.add(categoryField, 1, 2);
        grid.add(new Label("Buying Price (Rs.):"), 0, 3);
        grid.add(buyingPriceField, 1, 3);
        grid.add(new Label("Selling Price (Rs.):"), 0, 4);
        grid.add(sellingPriceField, 1, 4);
        grid.add(new Label("Quantity:"), 0, 5);
        grid.add(quantityField, 1, 5);
        grid.add(new Label("Reorder Level:"), 0, 6);
        grid.add(reorderField, 1, 6);

        grid.setUserData(new Object[]{nameField, skuField, categoryField, buyingPriceField, 
                                      sellingPriceField, quantityField, reorderField, product});

        return grid;
    }

    private Product createProductFromForm(GridPane grid) {
        return createProductFromForm(grid, null);
    }

    private Product createProductFromForm(GridPane grid, Product existingProduct) {
        try {
            Object[] fields = (Object[]) grid.getUserData();
            TextField nameField = (TextField) fields[0];
            TextField skuField = (TextField) fields[1];
            TextField categoryField = (TextField) fields[2];
            TextField buyingPriceField = (TextField) fields[3];
            TextField sellingPriceField = (TextField) fields[4];
            TextField quantityField = (TextField) fields[5];
            TextField reorderField = (TextField) fields[6];

            // Validate required fields
            if (nameField.getText().trim().isEmpty() || skuField.getText().trim().isEmpty() || 
                categoryField.getText().trim().isEmpty()) {
                showError("Please fill in all required fields (Name, SKU, Category)");
                return null;
            }

            // Parse numeric fields
            double buyingPrice = Double.parseDouble(buyingPriceField.getText().trim());
            double sellingPrice = Double.parseDouble(sellingPriceField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());
            int reorderLevel = Integer.parseInt(reorderField.getText().trim());

            // Create Product object
            Product product = new Product();
            if (existingProduct != null) {
                product.setId(existingProduct.getId()); // Keep existing ID for updates
            }
            product.setName(nameField.getText().trim());
            product.setSku(skuField.getText().trim());
            product.setCategory(categoryField.getText().trim());
            product.setBuyingPrice(buyingPrice);
            product.setSellingPrice(sellingPrice);
            product.setQuantityInStock(quantity);
            product.setReorderLevel(reorderLevel);

            logger.info("Created product from form: {}", product);
            return product;
        } catch (NumberFormatException e) {
            showError("Please enter valid numbers for price and quantity fields");
            return null;
        } catch (Exception e) {
            logger.error("Error creating product from form", e);
            showError("Error creating product: " + e.getMessage());
            return null;
        }
    }

    private void deleteSelectedProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Please select a product to delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Product?");
        alert.setContentText("Are you sure you want to delete " + selected.getName() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (productService.deleteProduct(selected.getId())) {
                loadProducts();
                showInfo("Product deleted successfully!");
            } else {
                showError("Failed to delete product");
            }
        }
    }

    private void updateStatistics() {
        totalProductsLabel.setText(String.valueOf(productService.getTotalProductCount()));
        totalStockLabel.setText(String.valueOf(productService.getTotalStockQuantity()));
        inventoryValueLabel.setText(String.format("Rs. %.2f", productService.getTotalInventoryValue()));
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
