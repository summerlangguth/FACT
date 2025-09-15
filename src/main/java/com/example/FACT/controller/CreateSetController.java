package com.example.FACT.controller;

import com.example.FACT.HelloApplication;
import com.example.FACT.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CreateSetController {
    @FXML private ComboBox<String> ApplicationComboBox; // NEW
    @FXML private TextField CategoryTextField;
    @FXML private ComboBox<String> DifficultyComboBox;
    @FXML private TextField DescriptionTextField;
    @FXML private Label CreateMessageLabel;
    @FXML private Button CancelButton;
    @FXML private Button SaveButton;
    @FXML private Button EditSetsButton;
    @FXML private TextField KeyBindTextField;

    // from previous step
    private final javafx.beans.property.ObjectProperty<KeyCombination> capturedCombo =
            new javafx.beans.property.SimpleObjectProperty<>();

    public ICreateSetDAO model = new SqliteCreateSetDAO();

    private static final String ADD_APP_SENTINEL = "➕ Add application…";
    private final ObservableList<String> appItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupKeyCaptureField();

        loadApplications();
        ApplicationComboBox.setItems(appItems);
        ApplicationComboBox.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select Application");
                } else {
                    setText(item);
                }
                setStyle("-fx-font-size: 25px;");
            }
        });
        ApplicationComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-font-size: 25px;");
            }
        });
        ApplicationComboBox.setPromptText("Select application");

        DifficultyComboBox.getItems().setAll("Beginner", "Intermediate", "Advanced");
        DifficultyComboBox.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select Difficulty");
                } else {
                    setText(item);
                }
                setStyle("-fx-font-size: 25px;");
            }
        });
        DifficultyComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-font-size: 25px;");
            }
        });

        ApplicationComboBox.getSelectionModel().clearSelection();
        DifficultyComboBox.getSelectionModel().clearSelection();

        ApplicationComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (ADD_APP_SENTINEL.equals(item)) {
                        setStyle("-fx-font-style: italic;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        ApplicationComboBox.setOnAction(e -> {
            String value = ApplicationComboBox.getValue();
            if (ADD_APP_SENTINEL.equals(value)) {
                handleAddApplication();
            }
        });
    }

    @FXML
    private void ClearForm(ActionEvent e) {
        clearForm(false);
        CreateMessageLabel.setText("");
    }

    @FXML
    private void EditSetAction() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("EditSetView.fxml"));
            Stage CreateSetStage = new Stage();
            Stage currentStage = (Stage) EditSetsButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            CreateSetStage.initStyle(StageStyle.UNDECORATED);
            CreateSetStage.setTitle("Edit Sets");
            CreateSetStage.setScene(scene);
            CreateSetStage.show();
            currentStage.hide();
        }
        catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    private void loadApplications() {
        try {
            List<String> apps = model.listApplications();
            if (apps == null) apps = new ArrayList<>();
            Collections.sort(apps, String.CASE_INSENSITIVE_ORDER);

            appItems.setAll(apps);
            // Append sentinel at the end
            appItems.add(ADD_APP_SENTINEL);
        } catch (Exception ex) {
            ex.printStackTrace();
            appItems.setAll(ADD_APP_SENTINEL);
        }
    }

    private void handleAddApplication() {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Add Application");
        dlg.setHeaderText("Add a new application");
        dlg.setContentText("Application name:");

        Optional<String> result = dlg.showAndWait();
        if (result.isEmpty()) {
            ApplicationComboBox.getSelectionModel().clearSelection();
            return;
        }

        String name = result.get().trim();
        if (name.isEmpty()) {
            showError("Application name cannot be empty.");
            ApplicationComboBox.getSelectionModel().clearSelection();
            return;
        }
        for (String s : appItems) {
            if (s.equalsIgnoreCase(name)) {
                if (!ADD_APP_SENTINEL.equals(s)) {
                    ApplicationComboBox.getSelectionModel().select(s);
                } else {
                    ApplicationComboBox.getSelectionModel().clearSelection();
                }
                return;
            }
        }

        try {
            if (model.addApplication(name)) {
                appItems.remove(ADD_APP_SENTINEL);
                appItems.add(name);
                Collections.sort(appItems, String.CASE_INSENSITIVE_ORDER);
                appItems.add(ADD_APP_SENTINEL);

                ApplicationComboBox.getSelectionModel().select(name);
            } else {
                showError("Could not add application. Please try a different name.");
                ApplicationComboBox.getSelectionModel().clearSelection();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("An error occurred while adding the application.");
            ApplicationComboBox.getSelectionModel().clearSelection();
        }
    }

    private void clearForm(boolean keepApplication) {
        if (!keepApplication) {
            ApplicationComboBox.getSelectionModel().clearSelection();
        }
        CategoryTextField.clear();
        DescriptionTextField.clear();

        // Show "Select Difficulty"
        DifficultyComboBox.getSelectionModel().clearSelection();

        // Reset keybind capture
        capturedCombo.set(null);
        KeyBindTextField.clear();
        KeyBindTextField.setPromptText("Click here and press a shortcut (e.g., Ctrl+C)");
        KeyBindTextField.requestFocus();

        // Clear any message
        CreateMessageLabel.setText("");
    }



    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

    @FXML
    public void CreateSetAction(ActionEvent event) {
        CreateKeySet();
    }

    public void CreateKeySet() {
        String application = ApplicationComboBox.getValue();
        if (application == null || application.isBlank() || ADD_APP_SENTINEL.equals(application)) {
            CreateMessageLabel.setText("Please select a valid application.");
            return;
        }

        String category = CategoryTextField.getText();
        String difficulty = DifficultyComboBox.getValue();
        if (difficulty == null || difficulty.isBlank()) {
            CreateMessageLabel.setText("Please select a difficulty.");
            return;
        }

        String description = DescriptionTextField.getText();
        String keyBind = (capturedCombo.get() != null)
                ? capturedCombo.get().getDisplayText()
                : KeyBindTextField.getText();

        KeySets keySets = new KeySets(application, category, difficulty, description, keyBind);

        try {
            if (model.addKeySet(keySets)) {
                CreateMessageLabel.setText("Created Set successfully");
                clearForm(true);
            } else {
                CreateMessageLabel.setText("Please ensure all details are valid");
            }
        } catch (Exception e) {
            e.printStackTrace();
            CreateMessageLabel.setText("Error creating set. See logs.");
        }

    }

    private void setupKeyCaptureField() {
        KeyBindTextField.setPromptText("Click here and press a shortcut (e.g., Ctrl+C)");
        KeyBindTextField.addEventFilter(KeyEvent.KEY_TYPED, e -> e.consume());
        KeyBindTextField.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                capturedCombo.set(null);
                KeyBindTextField.clear();
                e.consume();
                return;
            }
            boolean ctrl = e.isControlDown() || e.getCode() == KeyCode.CONTROL;
            boolean alt = e.isAltDown() || e.getCode() == KeyCode.ALT;
            boolean shift = e.isShiftDown() || e.getCode() == KeyCode.SHIFT;
            boolean meta = e.isMetaDown() || e.getCode() == KeyCode.META;

            KeyCode code = e.getCode();
            if (code.isModifierKey()) {
                KeyBindTextField.setText(formatPartial(ctrl, alt, shift, meta));
                e.consume();
                return;
            }

            List<KeyCombination.Modifier> mods = new ArrayList<>();
            if (ctrl)  mods.add(KeyCombination.CONTROL_DOWN);
            if (alt)   mods.add(KeyCombination.ALT_DOWN);
            if (shift) mods.add(KeyCombination.SHIFT_DOWN);
            if (meta)  mods.add(KeyCombination.META_DOWN);

            KeyCodeCombination combo = new KeyCodeCombination(code, mods.toArray(new KeyCombination.Modifier[0]));
            capturedCombo.set(combo);
            KeyBindTextField.setText(combo.getDisplayText());
            e.consume();
        });
    }

    private String formatPartial(boolean ctrl, boolean alt, boolean shift, boolean meta) {
        List<String> parts = new ArrayList<>();
        if (ctrl)  parts.add("Ctrl");
        if (alt)   parts.add("Alt");
        if (shift) parts.add("Shift");
        if (meta)  parts.add("Meta");
        return String.join("+", parts) + (parts.isEmpty() ? "" : "+ …");
    }
}
