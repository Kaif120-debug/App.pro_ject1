package com.kiranastore.controller;

import com.kiranastore.service.InvoiceService;
import com.kiranastore.service.ProductService;
import com.kiranastore.service.SalesReportService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class DashboardController {
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @FXML
    private Label totalProductsLabel;

    @FXML
    private Label totalStockLabel;

    @FXML
    private Label inventoryValueLabel;

    @FXML
    private Label todaySalesLabel;

    @FXML
    private Label totalRevenueLabel;

    @FXML
    private Label averageTransactionLabel;

    @FXML
    private Label lowStockCountLabel;

    @FXML
    private VBox chartsContainer;

    private ProductService productService;
    private InvoiceService invoiceService;
    private SalesReportService salesReportService;

    @FXML
    public void initialize() {
        logger.info("Initializing DashboardController");
        productService = new ProductService();
        invoiceService = new InvoiceService();
        salesReportService = new SalesReportService();

        updateDashboard();
    }

    public void updateDashboard() {
        updateStatistics();
        updateCharts();
    }

    private void updateStatistics() {
        // Product statistics
        int totalProducts = productService.getTotalProductCount();
        int totalStock = productService.getTotalStockQuantity();
        double inventoryValue = productService.getTotalInventoryValue();
        int lowStockCount = productService.getLowStockProducts().size();

        totalProductsLabel.setText(String.valueOf(totalProducts));
        totalStockLabel.setText(String.valueOf(totalStock));
        inventoryValueLabel.setText(String.format("Rs. %.2f", inventoryValue));
        lowStockCountLabel.setText(String.valueOf(lowStockCount));

        // Sales statistics
        double todaySales = invoiceService.getDailySales(LocalDate.now());
        double totalRevenue = invoiceService.getTotalRevenue();
        double averageTransaction = invoiceService.getAverageInvoiceValue();

        todaySalesLabel.setText(String.format("Rs. %.2f", todaySales));
        totalRevenueLabel.setText(String.format("Rs. %.2f", totalRevenue));
        averageTransactionLabel.setText(String.format("Rs. %.2f", averageTransaction));
    }

    private void updateCharts() {
        chartsContainer.getChildren().clear();

        // Create sales trend chart (last 7 days)
        chartsContainer.getChildren().add(createSalesTrendChart());

        // Create low stock alert
        if (!productService.getLowStockProducts().isEmpty()) {
            chartsContainer.getChildren().add(createLowStockAlert());
        }
    }

    private javafx.scene.Node createSalesTrendChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        yAxis.setLabel("Sales (Rs.)");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Sales Trend (Last 7 Days)");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Daily Sales");

        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            double sales = invoiceService.getDailySales(date);
            series.getData().add(new XYChart.Data<>(date.toString(), sales));
        }

        chart.getData().add(series);
        chart.setPrefHeight(300);
        return chart;
    }

    private javafx.scene.Node createLowStockAlert() {
        VBox alertBox = new VBox();
        alertBox.setStyle("-fx-border-color: #ff6b6b; -fx-border-radius: 5; -fx-padding: 15;");
        alertBox.setSpacing(10);

        Label titleLabel = new Label("⚠️ Low Stock Alert");
        titleLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #ff6b6b;");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        int row = 0;
        for (var product : productService.getLowStockProducts()) {
            Label productLabel = new Label(product.getName());
            Label stockLabel = new Label("Stock: " + product.getQuantityInStock());
            stockLabel.setStyle("-fx-text-fill: #ff6b6b;");

            gridPane.add(productLabel, 0, row);
            gridPane.add(stockLabel, 1, row);
            row++;

            if (row >= 5) break;
        }

        alertBox.getChildren().addAll(titleLabel, gridPane);
        return alertBox;
    }
}
