package com.example.FACT;

import com.example.FACT.controller.GameplayController;
import com.example.FACT.model.GameEngine;
import com.example.FACT.model.Shortcut;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.util.List;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/FACT/setGameplay.fxml"));
        Parent root = loader.load();

        GameplayController controller = loader.getController();

        // DEMO SHORTCUTS
        List<Shortcut> demoShortcuts = List.of(
                new Shortcut("Copy",
                        new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN)),
                new Shortcut("Paste",
                        new KeyCodeCombination(KeyCode.V, KeyCombination.SHORTCUT_DOWN))
        );

        GameEngine engine = new GameEngine(demoShortcuts);
        controller.setEngine(engine);
        stage.setScene(new Scene(root, 1200, 800));
        stage.setTitle("Shortcut Trainer");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}