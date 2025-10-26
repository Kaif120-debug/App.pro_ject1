package com.kiranastore.controller;

import com.kiranastore.model.SalesReport;
import com.kiranastore.service.InvoiceService;
import com.kiranastore.service.ProductService;
import com.kiranastore.service.SalesReportService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class ReportsController {
    private static final Logger logger = LoggerFactory.getLogger(ReportsController.class);

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Button generateReportBtn;

    @FXML
    private Label totalSalesLabel;

    @FXML
    private Label totalProfitLabel;

    @FXML
    private Label transactionCountLabel;

    @FXML
    private Label profitMarginLabel;

    @FXML
    private VBox chartsContainer;

    @FXML
    private TableView<SalesReport> reportsTable;

    @FXML
    private TableColumn<SalesReport, LocalDate> dateColumn;

    @FXML
    private TableColumn<SalesReport, Double> salesColumn;

    @FXML
    private TableColumn<SalesReport, Double> profitColumn;

    @FXML
    private TableColumn<SalesReport, Integer> transactionsColumn;

    private SalesReportService salesReportService;
    private InvoiceService invoiceService;
    private ProductService productService;

    @FXML
    public void initialize() {
        logger.info("Initializing ReportsController");
        salesReportService = new SalesReportService();
        invoiceService = new InvoiceService();
        productService = new ProductService();

        setupDatePickers();
        setupTableColumns();
        setupButtonHandlers();
        generateReport();
    }

    private void setupDatePickers() {
        LocalDate today = LocalDate.now();
        startDatePicker.setValue(today.minusMonths(1));
        endDatePicker.setValue(today);
    }

    private void setupTableColumns() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
        salesColumn.setCellValueFactory(new PropertyValueFactory<>("totalSales"));
        profitColumn.setCellValueFactory(new PropertyValueFactory<>("totalProfit"));
        transactionsColumn.setCellValueFactory(new PropertyValueFactory<>("totalTransactions"));

        salesColumn.setCellFactory(tc -> new TableCell<SalesReport, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty ? "" : String.format("Rs. %.2f", value));
            }
        });

        profitColumn.setCellFactory(tc -> new TableCell<SalesReport, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty ? "" : String.format("Rs. %.2f", value));
            }
        });
    }

    private void setupButtonHandlers() {
        generateReportBtn.setOnAction(e -> generateReport());
    }

    private void generateReport() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            showError("Please select date range");
            return;
        }

        if (startDate.isAfter(endDate)) {
            showError("Start date cannot be after end date");
            return;
        }

        List<SalesReport> reports = salesReportService.getSalesReportsByDateRange(startDate, endDate);

        double totalSales = reports.stream().mapToDouble(SalesReport::getTotalSales).sum();
        double totalProfit = reports.stream().mapToDouble(SalesReport::getTotalProfit).sum();
        int totalTransactions = reports.stream().mapToInt(SalesReport::getTotalTransactions).sum();
        double profitMargin = totalSales > 0 ? (totalProfit / totalSales) * 100 : 0;

        totalSalesLabel.setText(String.format("Rs. %.2f", totalSales));
        totalProfitLabel.setText(String.format("Rs. %.2f", totalProfit));
        transactionCountLabel.setText(String.valueOf(totalTransactions));
        profitMarginLabel.setText(String.format("%.2f%%", profitMargin));

        reportsTable.setItems(FXCollections.observableArrayList(reports));

        updateCharts(reports);
    }

    private void updateCharts(List<SalesReport> reports) {
        chartsContainer.getChildren().clear();

        if (reports.isEmpty()) {
            Label emptyLabel = new Label("No data available for selected period");
            chartsContainer.getChildren().add(emptyLabel);
            return;
        }

        chartsContainer.getChildren().add(createSalesChart(reports));
        chartsContainer.getChildren().add(createProfitChart(reports));
    }

    private javafx.scene.Node createSalesChart(List<SalesReport> reports) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        yAxis.setLabel("Sales (Rs.)");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Daily Sales");
        chart.setPrefHeight(300);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sales");

        for (SalesReport report : reports) {
            series.getData().add(new XYChart.Data<>(report.getSaleDate().toString(), report.getTotalSales()));
        }

        chart.getData().add(series);
        return chart;
    }

    private javafx.scene.Node createProfitChart(List<SalesReport> reports) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        yAxis.setLabel("Profit (Rs.)");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Daily Profit");
        chart.setPrefHeight(300);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Profit");

        for (SalesReport report : reports) {
            series.getData().add(new XYChart.Data<>(report.getSaleDate().toString(), report.getTotalProfit()));
        }

        chart.getData().add(series);
        return chart;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
