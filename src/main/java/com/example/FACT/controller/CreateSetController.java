package com.example.FACT.controller;

import com.example.FACT.HelloApplication;
import com.example.FACT.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

/**
 * Controller class CreateSetController
 *
 * JavaFx controller for creating sets for the app connects to createSet.fxml.
 */
public class CreateSetController {
    @FXML private ComboBox<String> ApplicationComboBox; // NEW
    @FXML private TextField CategoryTextField;
    @FXML private ComboBox<String> DifficultyComboBox;
    @FXML private TextField DescriptionTextField;
    @FXML private Label CreateMessageLabel;
    @FXML private Button CancelButton;
    @FXML private Button SaveButton;
    @FXML private Button exit;
    @FXML private Button EditSetsButton;
    @FXML private TextField KeyBindTextField;

    private final javafx.beans.property.ObjectProperty<KeyCombination> capturedCombo =
            new javafx.beans.property.SimpleObjectProperty<>();

    public ICreateSetDAO model = new SqliteCreateSetDAO();

    private static final String ADD_APP_SENTINEL = "➕ Add application…";
    private final ObservableList<String> appItems = FXCollections.observableArrayList();
    private static final String COMBO_CELL_CLASS = "combo-md-cell";
    private static final String COMBO_ITALIC_CLASS = "combo-italic";
    /**
     * Initializes the Create Set controller
     */
    @FXML
    public void initialize() {
        setupKeyCaptureField();

        loadApplications();
        ApplicationComboBox.setItems(appItems);
        configureComboBoxWithSentinel(ApplicationComboBox, "Select Application", ADD_APP_SENTINEL);

        ApplicationComboBox.setOnAction(e -> {
            String value = ApplicationComboBox.getValue();
            if (ADD_APP_SENTINEL.equals(value)) {
                handleAddApplication();
            }
        });

        DifficultyComboBox.getItems().setAll("Beginner", "Intermediate", "Advanced");
        configureComboBox(DifficultyComboBox, "Select Difficulty");

        ApplicationComboBox.getSelectionModel().clearSelection();
        DifficultyComboBox.getSelectionModel().clearSelection();
    }

    private void configureComboBox(ComboBox<String> combo, String prompt) {
        combo.setPromptText(prompt);

        combo.setButtonCell(new ListCell<>() {
            { getStyleClass().add(COMBO_CELL_CLASS); }
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? prompt : item);
                getStyleClass().remove(COMBO_ITALIC_CLASS);
            }
        });

        combo.setCellFactory(cb -> new ListCell<>() {
            { getStyleClass().add(COMBO_CELL_CLASS); }
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                getStyleClass().remove(COMBO_ITALIC_CLASS);
            }
        });
    }

    private void configureComboBoxWithSentinel(ComboBox<String> combo, String prompt, String sentinel) {
        combo.setPromptText(prompt);

        combo.setButtonCell(new ListCell<>() {
            { getStyleClass().add(COMBO_CELL_CLASS); }
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(prompt);
                    getStyleClass().remove(COMBO_ITALIC_CLASS);
                } else {
                    setText(item);
                    if (sentinel.equals(item)) {
                        if (!getStyleClass().contains(COMBO_ITALIC_CLASS)) {
                            getStyleClass().add(COMBO_ITALIC_CLASS);
                        }
                    } else {
                        getStyleClass().remove(COMBO_ITALIC_CLASS);
                    }
                }
            }
        });

        combo.setCellFactory(cb -> new ListCell<>() {
            { getStyleClass().add(COMBO_CELL_CLASS); }
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    getStyleClass().remove(COMBO_ITALIC_CLASS);
                } else {
                    setText(item);
                    if (sentinel.equals(item)) {
                        if (!getStyleClass().contains(COMBO_ITALIC_CLASS)) {
                            getStyleClass().add(COMBO_ITALIC_CLASS);
                        }
                    } else {
                        getStyleClass().remove(COMBO_ITALIC_CLASS);
                    }
                }
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

            FXMLLoader baseLoader = new FXMLLoader(HelloApplication.class.getResource("homebase.fxml"));
            Parent root = baseLoader.load();

            HomeBaseController baseController = baseLoader.getController();

            baseController.setContent("/com/example/FACT/EditSetView.fxml");

            Stage newStage = new Stage();
            Stage currentStage = (Stage) EditSetsButton.getScene().getWindow();
            // Set the new scene with the base layout
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.initStyle(StageStyle.UNDECORATED);
            newStage.show();
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

        DifficultyComboBox.getSelectionModel().clearSelection();

        capturedCombo.set(null);
        KeyBindTextField.clear();
        KeyBindTextField.setPromptText("Click here and press a shortcut (e.g., Ctrl+C)");
        KeyBindTextField.requestFocus();

        CreateMessageLabel.setText("");
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

    /**
     * Event handler for create button that will add a new keyset.
     * @param event , Event for when Create button pressed
     */
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

        String Category = CategoryTextField.getText();
        String difficulty = DifficultyComboBox.getValue();
        if (difficulty == null || difficulty.isBlank()) {
            CreateMessageLabel.setText("Please select a difficulty.");
            return;
        }

        String description = DescriptionTextField.getText();
        String keyBind = (capturedCombo.get() != null)
                ? capturedCombo.get().getDisplayText()
                : KeyBindTextField.getText();

        KeySets keySets = new KeySets(application, Category, difficulty, description, keyBind);

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

    /**
     * Set up for the key capture to detect what keys the user in inputting.
     */
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

    /**
     * Used to format the inputted keybinds
     * @param ctrl
     * @param alt
     * @param shift
     * @param meta
     * @return
     */
    private String formatPartial(boolean ctrl, boolean alt, boolean shift, boolean meta) {
        List<String> parts = new ArrayList<>();
        if (ctrl)  parts.add("Ctrl");
        if (alt)   parts.add("Alt");
        if (shift) parts.add("Shift");
        if (meta)  parts.add("Meta");
        return String.join("+", parts) + (parts.isEmpty() ? "" : "+ …");
    }
}
