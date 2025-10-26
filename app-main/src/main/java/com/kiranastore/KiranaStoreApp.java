package com.kiranastore;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.kiranastore.database.DatabaseInitializer;
import com.kiranastore.controller.MainDashboardController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class KiranaStoreApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(KiranaStoreApp.class);

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            // Initialize database
            DatabaseInitializer.initialize();
            logger.info("Database initialized successfully");

            // Load main dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainDashboard.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 800);

            // Apply CSS styling
            String css = getClass().getResource("/css/styles.css").toExternalForm();
            scene.getStylesheets().add(css);

            primaryStage.setTitle("Kirana Store Manager");
            primaryStage.setScene(scene);
            primaryStage.setWidth(1200);
            primaryStage.setHeight(800);
            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(600);

            // Set icon (optional)
            try {
                Image icon = new Image(getClass().getResourceAsStream("/images/icon.png"));
                primaryStage.getIcons().add(icon);
            } catch (Exception e) {
                logger.warn("Icon not found, using default");
            }

            primaryStage.show();
            logger.info("Application started successfully");

        } catch (Exception e) {
            logger.error("Error starting application", e);
            throw new RuntimeException("Failed to start application", e);
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        logger.info("Application closed");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
