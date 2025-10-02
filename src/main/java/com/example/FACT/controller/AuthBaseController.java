package com.example.FACT.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * AuthBaseController
 * Standalone controller (no inheritance) that mirrors the top-bar/window
 * behavior from your BaseController, but without any sidebar/navigation.
 *
 * Expected FXML ids in authbase.fxml:
 *  - StackPane contentArea
 *  - HBox      titleBar
 *  - Button    minimizeButton, maximizeButton, closeButton
 *
 * Usage:
 *   base.setContent("/com/example/FACT/login.fxml");
 */
public class AuthBaseController {

    // ===== FXML wires =====
    @FXML private StackPane contentArea;
    @FXML private HBox titleBar;
    @FXML private Button minimizeButton;
    @FXML private Button maximizeButton;
    @FXML private Button closeButton;

    // ===== Config =====
    /** Wrap injected content in an AnchorPane and anchor all sides to 0 so it stretches. */
    private static final boolean USE_ANCHOR_WRAPPER = true;

    // ===== Window state for drag/maximize =====
    private double dragOffsetX;
    private double dragOffsetY;
    private boolean maximized = false;

    // Saved bounds for restore after maximize
    private double savedX, savedY, savedW, savedH;

    @FXML
    private void initialize() {
        // Attach drag + double-click handlers to the title bar
        if (titleBar != null) {
            titleBar.setOnMousePressed(this::onDragStart);
            titleBar.setOnMouseDragged(this::onDragged);
            titleBar.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                    onMaximizeRestore();
                }
            });
        }

        // (Optional) You could load a default page here after the Scene is ready
        Platform.runLater(() -> {
            // Example:
            // setContent("/com/example/FACT/login.fxml");
        });
    }

    // ===== Public API to swap center content =====
    public void setContent(String fxmlResourcePath) {
        try {
            URL url = getClass().getResource(fxmlResourcePath);
            if (url == null) {
                System.err.println("AuthBaseController.setContent: resource not found -> " + fxmlResourcePath);
                return;
            }
            FXMLLoader loader = new FXMLLoader(url);
            Node view = loader.load();

            if (USE_ANCHOR_WRAPPER) {
                AnchorPane wrapper = new AnchorPane(view);
                AnchorPane.setTopAnchor(view, 0.0);
                AnchorPane.setRightAnchor(view, 0.0);
                AnchorPane.setBottomAnchor(view, 0.0);
                AnchorPane.setLeftAnchor(view, 0.0);
                contentArea.getChildren().setAll(wrapper);
            } else {
                contentArea.getChildren().setAll(view);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Convenience variant that also returns the child controller (if needed).
     */
    public <T> T setContentAndGetController(String fxmlResourcePath) {
        try {
            URL url = getClass().getResource(fxmlResourcePath);
            if (url == null) {
                System.err.println("AuthBaseController.setContentAndGetController: resource not found -> " + fxmlResourcePath);
                return null;
            }
            FXMLLoader loader = new FXMLLoader(url);
            Node view = loader.load();

            if (USE_ANCHOR_WRAPPER) {
                AnchorPane wrapper = new AnchorPane(view);
                AnchorPane.setTopAnchor(view, 0.0);
                AnchorPane.setRightAnchor(view, 0.0);
                AnchorPane.setBottomAnchor(view, 0.0);
                AnchorPane.setLeftAnchor(view, 0.0);
                contentArea.getChildren().setAll(wrapper);
            } else {
                contentArea.getChildren().setAll(view);
            }

            return loader.getController();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // ===== Title bar window controls =====
    @FXML
    private void onClose() {
        Stage stage = getStage();
        if (stage != null) {
            stage.close();
        }
    }

    @FXML
    private void onMinimize() {
        Stage stage = getStage();
        if (stage != null) {
            stage.setIconified(true);
        }
    }

    @FXML
    private void onMaximizeRestore() {
        Stage stage = getStage();
        if (stage == null) return;

        if (!maximized) {
            // Save current bounds
            savedX = stage.getX();
            savedY = stage.getY();
            savedW = stage.getWidth();
            savedH = stage.getHeight();

            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            maximized = true;
        } else {
            // Restore previous bounds
            stage.setX(savedX);
            stage.setY(savedY);
            stage.setWidth(savedW);
            stage.setHeight(savedH);
            maximized = false;
        }
    }

    // ===== Drag to move window =====
    private void onDragStart(MouseEvent e) {
        Stage stage = getStage();
        if (stage == null) return;

        dragOffsetX = e.getScreenX() - stage.getX();
        dragOffsetY = e.getScreenY() - stage.getY();
    }

    private void onDragged(MouseEvent e) {
        Stage stage = getStage();
        if (stage == null || maximized) return; // do not drag while maximized

        stage.setX(e.getScreenX() - dragOffsetX);
        stage.setY(e.getScreenY() - dragOffsetY);
    }

    // ===== Helpers =====
    private Stage getStage() {
        if (contentArea == null) return null;
        Scene scene = contentArea.getScene();
        return (scene != null) ? (Stage) scene.getWindow() : null;
    }
}
