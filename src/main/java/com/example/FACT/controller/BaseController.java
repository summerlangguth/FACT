package com.example.FACT.controller;

import com.example.FACT.HelloApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;

public class BaseController {

    @FXML private StackPane contentArea;
    @FXML private HBox titleBar;

    private double dragOffsetX;
    private double dragOffsetY;
    private boolean maximized = false;
    private Rectangle2D savedBounds;

    private static final boolean USE_ANCHOR_WRAPPER = false;

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

    public void setContent(String resourcePath) {
        try {
            System.out.println("BaseController.setContent -> " + resourcePath);
            URL url = HelloApplication.class.getResource(resourcePath);
            if (url == null) {
                throw new IllegalArgumentException("FXML not found on classpath: " + resourcePath);
            }
            Node view = FXMLLoader.load(url);
            setContent(view);
        } catch (Exception e) {
            e.printStackTrace();
            contentArea.getChildren().setAll(new Label("Failed to load: " + resourcePath));
        }
    }

    public void setContent(Node view) {
        if (view == null) {
            contentArea.getChildren().setAll(new Label("No content"));
            return;
        }

        if (view instanceof Region r) {
            r.setMinSize(0, 0);
            r.setPrefSize(-1, -1);
            r.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            // Bind to fill contentArea
            r.prefWidthProperty().bind(contentArea.widthProperty());
            r.prefHeightProperty().bind(contentArea.heightProperty());
        }

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
    }

    @FXML private void goHome()     { setContent("/com/example/FACT/homepage.fxml"); }
    @FXML private void goCreate()   { setContent("/com/example/FACT/createSet.fxml"); }
    @FXML private void goPlay()     { setContent("/com/example/FACT/gameplay.fxml"); }
    @FXML private void goEdit() { setContent("/com/example/FACT/EditSetView.fxml"); }

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

    private void onDragStart(MouseEvent e) {
        Stage stage = getStage();
        if (stage == null) return;
        dragOffsetX = e.getScreenX() - stage.getX();
        dragOffsetY = e.getScreenY() - stage.getY();
    }

    private void onDragged(MouseEvent e) {
        Stage stage = getStage();
        if (stage == null || maximized) return; // don't drag while maximized
        stage.setX(e.getScreenX() - dragOffsetX);
        stage.setY(e.getScreenY() - dragOffsetY);
    }

    private Stage getStage() {
        Scene scene = contentArea != null ? contentArea.getScene() : null;
        return (scene != null) ? (Stage) scene.getWindow() : null;
    }
}
