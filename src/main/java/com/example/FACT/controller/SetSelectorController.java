package com.example.FACT.controller;

import com.example.FACT.HelloApplication;
import com.example.FACT.model.ICreateSetDAO;
import com.example.FACT.model.KeySets;
import com.example.FACT.model.Shortcut;
import com.example.FACT.model.SqliteCreateSetDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SetSelectorController {

    @FXML public Button startBtn;
    @FXML private ComboBox<String> setCombo;
    @FXML private Label errorLabel;

    private final ICreateSetDAO dao = new SqliteCreateSetDAO();

    // As the FXML file is initialised, all current sets in user's database is displayed in the Combo Box.
    @FXML
    private void initialize() {
        var apps = dao.listApplications();
        setCombo.getItems().setAll(apps);
        if (!apps.isEmpty()) setCombo.getSelectionModel().select(0);
        errorLabel.setText("");
    }

    @FXML
    private void onStart() throws IOException {
        String selected = setCombo.getSelectionModel().getSelectedItem();
        if (selected == null || selected.isBlank()) {
            errorLabel.setText("Please select a set to start playing.");
            return;
        }

        // Retrieves all the shortcuts from the list.
        List<KeySets> rows = dao.listKeySetsByApplication(selected);
        if (rows == null || rows.isEmpty()) {
            errorLabel.setText("No shortcuts found for this set.");
            return;
        }

        // Converts all the data values in the database into shortcut objects, which can then be compiled into a list and imported into gameplay for iteration.
        List<Shortcut> shortcuts = rows.stream()
                .map(k -> new Shortcut(k.getDescription(), parseKeyCombo(k.getKeyBind())))
                .collect(Collectors.toList());


        URL baseUrl = getClass().getResource("/com/example/FACT/homebase.fxml");
        FXMLLoader baseLoader = new FXMLLoader(Objects.requireNonNull(baseUrl, "homebase.fxml not found"));
        Parent baseRoot = baseLoader.load();

        com.example.FACT.controller.HomeBaseController baseController = baseLoader.getController();
        GameplayController gameplayController = baseController.setContentAndGetController("/com/example/FACT/gameplay.fxml");
        gameplayController.setShortcutsAndStart(shortcuts, selected);

        Stage stage = (Stage) startBtn.getScene().getWindow();
        stage.setTitle("Gameplay");
        stage.setScene(new Scene(baseRoot));
        stage.show();
    }

    private KeyCombination parseKeyCombo(String keyBind) {
        if (keyBind == null || keyBind.isBlank()) return KeyCombination.NO_MATCH;

        String s = keyBind.trim().toUpperCase();
        boolean shift = s.contains("SHIFT");
        boolean alt   = s.contains("ALT") || s.contains("OPTION");
        boolean ctrl  = s.contains("CTRL") || s.contains("CONTROL");
        boolean meta  = s.contains("CMD")  || s.contains("COMMAND") || s.contains("META");

        String[] parts = s.split("\\+");
        String keyToken = parts[parts.length - 1].trim();

        var mods = new java.util.ArrayList<KeyCombination.Modifier>();
        if (shift) mods.add(KeyCombination.SHIFT_DOWN);
        if (alt)   mods.add(KeyCombination.ALT_DOWN);
        if (ctrl || meta) mods.add(KeyCombination.SHORTCUT_DOWN);

        KeyCode keyCode;
        try {
            keyCode = KeyCode.valueOf(keyToken);
        } catch (IllegalArgumentException ex) {
            switch (keyToken) {
                case "SPACE": keyCode = KeyCode.SPACE; break;
                case "TAB":   keyCode = KeyCode.TAB;   break;
                case "ENTER": keyCode = KeyCode.ENTER; break;
                default:      keyCode = KeyCode.UNDEFINED;
            }
        }
        return new KeyCodeCombination(keyCode, mods.toArray(new KeyCombination.Modifier[0]));
    }
}