package com.example.FACT.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Rectangle2D;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.Objects;

import java.io.IOException;

public class BaseController {

    @FXML private StackPane contentArea;
    @FXML private HBox titleBar;

    private double dragOffsetX;
    private double dragOffsetY;
    private boolean maximized = false;
    private Rectangle2D savedBounds;

    @FXML
    private void initialize() {
        // Make the title bar draggable for UNDECORATED stages
        titleBar.setOnMousePressed(this::onDragStart);
        titleBar.setOnMouseDragged(this::onDragged);

        // Optionally load a default/home page at startup
        Platform.runLater(() -> setContent("/com/example/FACT/homepage.fxml"));
    }

    // Public helper: load a page into the center
    public void setContent(String resourcePath) {
        try {
            System.out.println("BaseController.setContent -> " + resourcePath);
            // Use an anchor class that’s always present (your Application class is ideal)
            URL url = com.example.FACT.HelloApplication.class.getResource(resourcePath);
            if (url == null) {
                throw new IllegalArgumentException("FXML not found: " + resourcePath);
            }
            Node view = FXMLLoader.load(url);
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
            contentArea.getChildren().setAll(
                    new Label("Failed to load: " + resourcePath));
        }
    }

    // Optional overload if you pre-load and want the controller
    public void setContent(Node node) {
        contentArea.getChildren().setAll(node);
    }

    // Sidebar handlers
    @FXML private void goHome()   { setContent("/com/example/FACT/homepage.fxml"); }
    @FXML private void goCreate() { setContent("/com/example/FACT/createSet.fxml"); } // adjust filename
    @FXML private void goPlay()   { setContent("/com/example/FACT/gameplay.fxml"); }   // adjust filename
    @FXML private void goSettings(){ setContent("/com/example/FACT/settings.fxml"); }

    // Window controls
    @FXML
    private void onClose() {
        Stage stage = getStage();
        if (stage != null) stage.close();
    }

    @FXML
    private void onMinimize() {
        Stage stage = getStage();
        if (stage != null) stage.setIconified(true);
    }

    @FXML
    private void onMaximizeRestore() {
        Stage stage = getStage();
        if (stage == null) return;

        Screen screen = Screen.getPrimary();
        Rectangle2D visualBounds = screen.getVisualBounds();

        if (!maximized) {
            // Save current bounds
            savedBounds = new Rectangle2D(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
            stage.setX(visualBounds.getMinX());
            stage.setY(visualBounds.getMinY());
            stage.setWidth(visualBounds.getWidth());
            stage.setHeight(visualBounds.getHeight());
        } else {
            if (savedBounds != null) {
                stage.setX(savedBounds.getMinX());
                stage.setY(savedBounds.getMinY());
                stage.setWidth(savedBounds.getWidth());
                stage.setHeight(savedBounds.getHeight());
            }
        }
        maximized = !maximized;
    }

    // Dragging support
    private void onDragStart(MouseEvent e) {
        Stage stage = getStage();
        if (stage == null) return;
        dragOffsetX = e.getScreenX() - stage.getX();
        dragOffsetY = e.getScreenY() - stage.getY();
    }

    private void onDragged(MouseEvent e) {
        Stage stage = getStage();
        if (stage == null || maximized) return; // Don't drag while maximized
        stage.setX(e.getScreenX() - dragOffsetX);
        stage.setY(e.getScreenY() - dragOffsetY);
    }

    private Stage getStage() {
        Scene scene = contentArea.getScene();
        return (scene != null) ? (Stage) scene.getWindow() : null;
    }
}
