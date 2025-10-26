package com.kiranastore.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MainDashboardController {
    private static final Logger logger = LoggerFactory.getLogger(MainDashboardController.class);

    @FXML
    private BorderPane mainContainer;

    @FXML
    private Button dashboardBtn;

    @FXML
    private Button inventoryBtn;

    @FXML
    private Button billingBtn;

    @FXML
    private Button reportsBtn;

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        logger.info("Initializing MainDashboardController");
        loadDashboard();
        setupButtonHandlers();
    }

    private void setupButtonHandlers() {
        dashboardBtn.setOnAction(e -> loadDashboard());
        inventoryBtn.setOnAction(e -> loadInventory());
        billingBtn.setOnAction(e -> loadBilling());
        reportsBtn.setOnAction(e -> loadReports());
    }

    private void loadDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(loader.load());
            logger.info("Dashboard loaded");
        } catch (IOException e) {
            logger.error("Error loading dashboard", e);
        }
    }

    private void loadInventory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Inventory.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(loader.load());
            logger.info("Inventory loaded");
        } catch (IOException e) {
            logger.error("Error loading inventory", e);
        }
    }

    private void loadBilling() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Billing.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(loader.load());
            logger.info("Billing loaded");
        } catch (IOException e) {
            logger.error("Error loading billing", e);
        }
    }

    private void loadReports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Reports.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(loader.load());
            logger.info("Reports loaded");
        } catch (IOException e) {
            logger.error("Error loading reports", e);
        }
    }
}
