package com.example.FACT.controller;
import com.example.FACT.model.UserManager;
import com.example.FACT.model.*;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SetSelectorController {

    @FXML private Button startBtn;
    @FXML private ComboBox<String> setCombo;
    @FXML private Label errorLabel;
    @FXML private Label setScore;



    // Sets up DataBase connection
    private final ICreateSetDAO dao = new SqliteCreateSetDAO();
    private final SqliteSetStatisticsDAO model = new SqliteSetStatisticsDAO();



    // Retrieves the list of apps in the DataBase, adds to ComboBox.
    @FXML
    private void initialize() {
        var apps = dao.listApplications();
        setCombo.getItems().setAll(apps);
        if (!apps.isEmpty()){
            setCombo.getSelectionModel().select(0);
            updateMaxScoreForSelectedSet();
        }
        errorLabel.setText("");

        //listen for change in the selected set
        setCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldSet, newSet) -> {
            updateMaxScoreForSelectedSet();
        });
    }

    private void updateMaxScoreForSelectedSet() {
        String selectedSet = setCombo.getSelectionModel().getSelectedItem();
        if (selectedSet == null || selectedSet.isBlank()) {
            setScore.setText("");
            return;
        }

        String email = UserManager.getInstance().getLoggedInUser().getEmail();
        Integer maxScore = model.getMaxScore(email, selectedSet);

        if (maxScore >= 0) {
            setScore.setText("Previous Max Score: " + maxScore + " %");
        } else {
            setScore.setText("");
        }
    }
    @FXML
    private void onStart() throws IOException, SQLException {

        // On Start, identifies which app the user has selected.
        String selected = setCombo.getSelectionModel().getSelectedItem();
        if (selected == null || selected.isBlank()) {
            errorLabel.setText("Please select a set to start playing.");
            return;
        }


        // Stores all shortcuts into a list that matches the application that has been 'selected'.
        List<KeySets> rows = dao.listKeySetsByApplication(selected);
        if (rows == null || rows.isEmpty()) {
            errorLabel.setText("No shortcuts found for this set.");
            return;
        }

        // sets the set name as the most recent one the user has played
        UserManager.getInstance().getLoggedInUser().setLastPlayed(selected);

        // Iterates through the list, converts into a Shortcut object.
        List<Shortcut> shortcuts = rows.stream()
                .map(k -> new Shortcut(k.getApplication(), k.getDescription(), parseKeyCombo(k.getKeyBind())))
                .collect(Collectors.toList());


        URL baseUrl = getClass().getResource("/com/example/FACT/homebase.fxml");
        FXMLLoader baseLoader = new FXMLLoader(Objects.requireNonNull(baseUrl, "homebase.fxml not found"));
        Parent baseRoot = baseLoader.load();

        com.example.FACT.controller.BaseController baseController = baseLoader.getController();
        GameplayController gameplayController = baseController.setContentAndGetController("/com/example/FACT/gameplay.fxml");
        gameplayController.setShortcutsAndStart(shortcuts, selected);

        Stage stage = (Stage) startBtn.getScene().getWindow();
        stage.setTitle("Gameplay");
        stage.setScene(new Scene(baseRoot));
        stage.show();
    }




    private KeyCombination parseKeyCombo(String keyBind) {

        // Check is keybind is null.
        if (keyBind == null) return KeyCombination.NO_MATCH;

        // Trim spaces, convert to Upper.
        String s = keyBind.trim();
        if (s.isEmpty()) return KeyCombination.NO_MATCH;
        s = s.replaceAll("\\s*\\+\\s*", "+").replaceAll("\\++", "+").toUpperCase();

        // Check for modifier keys.
        boolean shift = s.contains("SHIFT");
        boolean alt   = s.contains("ALT") || s.contains("OPTION");
        boolean ctrl  = s.contains("CTRL") || s.contains("CONTROL");
        boolean meta  = s.contains("CMD")  || s.contains("COMMAND") || s.contains("META");

        // Grabs the last key of the key combo (usually the key that isn't the modifier e.g. F, Delete, etc.)
        String[] parts = s.split("\\+");
        if (parts.length == 0) return KeyCombination.NO_MATCH;
        String keyToken = parts[parts.length - 1].trim();
        if (keyToken.isEmpty()) return KeyCombination.NO_MATCH;

        // Check KeyCode for null.
        KeyCode keyCode = resolveKeyCode(keyToken);
        if (keyCode == null || keyCode == KeyCode.UNDEFINED) {
            return KeyCombination.NO_MATCH;
        }

        var mods = new java.util.ArrayList<KeyCombination.Modifier>();
        if (shift) mods.add(KeyCombination.SHIFT_DOWN);
        if (alt)   mods.add(KeyCombination.ALT_DOWN);
        if (ctrl || meta) mods.add(KeyCombination.SHORTCUT_DOWN); // cross-platform Cmd/Ctrl

        return new KeyCodeCombination(keyCode, mods.toArray(new KeyCombination.Modifier[0]));
    }



    private KeyCode resolveKeyCode(String token) {

        try {
            return KeyCode.valueOf(token);
        } catch (IllegalArgumentException ignored) { }

        if (token.length() == 1 && Character.isLetter(token.charAt(0))) {
            return KeyCode.valueOf(String.valueOf(token.charAt(0)));
        }

        if (token.length() == 1 && Character.isDigit(token.charAt(0))) {
            return KeyCode.valueOf("DIGIT" + token);
        }

        switch (token) {
            case "ESC": return KeyCode.ESCAPE;
            case "DEL": return KeyCode.DELETE;
            case "BKSP": case "BACK": return KeyCode.BACK_SPACE;
            case "PGUP": return KeyCode.PAGE_UP;
            case "PGDN": return KeyCode.PAGE_DOWN;
        }

        switch (token) {
            case "+": return KeyCode.PLUS;
            case "-": case "–": case "—": return KeyCode.MINUS;
            case "=": return KeyCode.EQUALS;
            case "/": return KeyCode.SLASH;
            case "\\": return KeyCode.BACK_SLASH;
            case ".": return KeyCode.PERIOD;
            case ",": return KeyCode.COMMA;
            case ";": return KeyCode.SEMICOLON;
            case "'": return KeyCode.QUOTE;
            case "[": return KeyCode.OPEN_BRACKET;
            case "]": return KeyCode.CLOSE_BRACKET;
            case "`": return KeyCode.BACK_QUOTE;
            case "SPACE": return KeyCode.SPACE;
            case "TAB": return KeyCode.TAB;
            case "ENTER": case "RETURN": return KeyCode.ENTER;
            case "UP": return KeyCode.UP;
            case "DOWN": return KeyCode.DOWN;
            case "LEFT": return KeyCode.LEFT;
            case "RIGHT": return KeyCode.RIGHT;
        }

        if (token.startsWith("F")) {
            try {
                int f = Integer.parseInt(token.substring(1));
                if (f >= 1 && f <= 24) return KeyCode.valueOf("F" + f);
            } catch (NumberFormatException ignored) { }
        }

        return KeyCode.UNDEFINED;
    }
}