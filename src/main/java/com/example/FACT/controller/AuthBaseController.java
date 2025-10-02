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

public class AuthBaseController {


    @FXML private StackPane contentArea;
    @FXML private HBox titleBar;
    @FXML private Button minimizeButton;
    @FXML private Button maximizeButton;
    @FXML private Button closeButton;

    private static final boolean USE_ANCHOR_WRAPPER = true;

    private double dragOffsetX;
    private double dragOffsetY;
    private boolean maximized = false;

    private double savedX, savedY, savedW, savedH;

    @FXML
    private void initialize() {
        if (titleBar != null) {
            titleBar.setOnMousePressed(this::onDragStart);
            titleBar.setOnMouseDragged(this::onDragged);
            titleBar.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                    onMaximizeRestore();
                }
            });
        }

        Platform.runLater(() -> {
        });
    }

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
            stage.setX(savedX);
            stage.setY(savedY);
            stage.setWidth(savedW);
            stage.setHeight(savedH);
            maximized = false;
        }
    }

    private void onDragStart(MouseEvent e) {
        Stage stage = getStage();
        if (stage == null) return;

        dragOffsetX = e.getScreenX() - stage.getX();
        dragOffsetY = e.getScreenY() - stage.getY();
    }

    private void onDragged(MouseEvent e) {
        Stage stage = getStage();
        if (stage == null || maximized) return;

        stage.setX(e.getScreenX() - dragOffsetX);
        stage.setY(e.getScreenY() - dragOffsetY);
    }

    private Stage getStage() {
        if (contentArea == null) return null;
        Scene scene = contentArea.getScene();
        return (scene != null) ? (Stage) scene.getWindow() : null;
    }
}
