package com.kiranastore.controller;

import com.kiranastore.model.InvoiceItem;
import com.kiranastore.model.Product;
import com.kiranastore.service.InvoiceService;
import com.kiranastore.service.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BillingController {
    private static final Logger logger = LoggerFactory.getLogger(BillingController.class);

    @FXML
    private ComboBox<Product> productCombo;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField customerNameField;

    @FXML
    private TextField customerPhoneField;

    @FXML
    private ComboBox<String> paymentMethodCombo;

    @FXML
    private TableView<InvoiceItem> invoiceItemTable;

    @FXML
    private TableColumn<InvoiceItem, String> productNameColumn;

    @FXML
    private TableColumn<InvoiceItem, Integer> quantityColumn;

    @FXML
    private TableColumn<InvoiceItem, Double> unitPriceColumn;

    @FXML
    private TableColumn<InvoiceItem, Double> lineTotalColumn;

    @FXML
    private Label subtotalLabel;

    @FXML
    private Label taxLabel;

    @FXML
    private Label totalLabel;

    @FXML
    private Button addItemBtn;

    @FXML
    private Button removeItemBtn;

    @FXML
    private Button generateInvoiceBtn;

    @FXML
    private Button clearBillBtn;

    @FXML
    private Button viewInvoicesBtn;

    private ProductService productService;
    private InvoiceService invoiceService;
    private ObservableList<InvoiceItem> cartItems;

    @FXML
    public void initialize() {
        logger.info("Initializing BillingController");
        productService = new ProductService();
        invoiceService = new InvoiceService();

        setupComboBoxes();
        setupTableColumns();
        setupButtonHandlers();
        cartItems = FXCollections.observableArrayList();
        invoiceItemTable.setItems(cartItems);
    }

    private void setupComboBoxes() {
        ObservableList<Product> products = FXCollections.observableArrayList(productService.getAllProducts());
        productCombo.setItems(products);

        ObservableList<String> paymentMethods = FXCollections.observableArrayList(
            "Cash",
            "Card",
            "Cheque",
            "Online Transfer"
        );
        paymentMethodCombo.setItems(paymentMethods);
        paymentMethodCombo.setValue("Cash");
    }

    private void setupTableColumns() {
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        lineTotalColumn.setCellValueFactory(new PropertyValueFactory<>("lineTotal"));

        unitPriceColumn.setCellFactory(tc -> new TableCell<InvoiceItem, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty ? "" : String.format("Rs. %.2f", price));
            }
        });

        lineTotalColumn.setCellFactory(tc -> new TableCell<InvoiceItem, Double>() {
            @Override
            protected void updateItem(Double total, boolean empty) {
                super.updateItem(total, empty);
                setText(empty ? "" : String.format("Rs. %.2f", total));
            }
        });
    }

    private void setupButtonHandlers() {
        addItemBtn.setOnAction(e -> addItemToCart());
        removeItemBtn.setOnAction(e -> removeItemFromCart());
        generateInvoiceBtn.setOnAction(e -> generateInvoice());
        clearBillBtn.setOnAction(e -> clearBill());
        viewInvoicesBtn.setOnAction(e -> viewInvoices());
    }

    private void addItemToCart() {
        Product selected = productCombo.getValue();
        if (selected == null) {
            showWarning("Please select a product");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                showError("Quantity must be greater than 0");
                return;
            }

            if (quantity > selected.getQuantityInStock()) {
                showError("Insufficient stock. Available: " + selected.getQuantityInStock());
                return;
            }

            InvoiceItem item = InvoiceItem.create(selected, quantity);
            cartItems.add(item);
            quantityField.clear();

            updateTotals();
            logger.info("Item added to cart: {}", selected.getName());

        } catch (NumberFormatException e) {
            showError("Please enter valid quantity");
        }
    }

    private void removeItemFromCart() {
        InvoiceItem selected = invoiceItemTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Please select an item to remove");
            return;
        }

        cartItems.remove(selected);
        updateTotals();
    }

    private void updateTotals() {
        double subtotal = cartItems.stream().mapToDouble(InvoiceItem::getLineTotal).sum();
        double tax = subtotal * 0.05;  // 5% tax
        double total = subtotal + tax;

        subtotalLabel.setText(String.format("Rs. %.2f", subtotal));
        taxLabel.setText(String.format("Rs. %.2f", tax));
        totalLabel.setText(String.format("Rs. %.2f", total));
    }

    private void generateInvoice() {
        if (cartItems.isEmpty()) {
            showWarning("Please add items before generating invoice");
            return;
        }

        String customerName = customerNameField.getText().trim();
        String customerPhone = customerPhoneField.getText().trim();
        String paymentMethod = paymentMethodCombo.getValue();

        int invoiceId = invoiceService.createInvoice(
            customerName.isEmpty() ? "Walk-in Customer" : customerName,
            customerPhone,
            new ArrayList<>(cartItems),
            paymentMethod
        );

        if (invoiceId != -1) {
            showInfo("Invoice #" + invoiceId + " generated successfully!");
            clearBill();
        } else {
            showError("Failed to generate invoice");
        }
    }

    private void clearBill() {
        cartItems.clear();
        customerNameField.clear();
        customerPhoneField.clear();
        paymentMethodCombo.setValue("Cash");
        updateTotals();
    }

    private void viewInvoices() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Recent Invoices");
        dialog.setWidth(800);
        dialog.setHeight(500);

        TableView<com.kiranastore.model.Invoice> invoiceTable = new TableView<>();
        TableColumn<com.kiranastore.model.Invoice, String> invCol = new TableColumn<>("Invoice #");
        invCol.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));

        TableColumn<com.kiranastore.model.Invoice, String> custCol = new TableColumn<>("Customer");
        custCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        TableColumn<com.kiranastore.model.Invoice, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        invoiceTable.getColumns().addAll(invCol, custCol, amountCol);
        invoiceTable.setItems(FXCollections.observableArrayList(invoiceService.getAllInvoices()));

        dialog.getDialogPane().setContent(invoiceTable);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
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
