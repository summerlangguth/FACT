package com.example.FACT.controller;

import com.example.FACT.HelloApplication;
import com.example.FACT.model.ICreateSetDAO;
import com.example.FACT.model.KeySets;
import com.example.FACT.model.SqliteCreateSetDAO;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

public class EditSetController {

    @FXML private ComboBox<String> ApplicationComboBox;
    @FXML private TableView<KeySets> table;
    @FXML private TableColumn<KeySets, String> colCategory;
    @FXML private TableColumn<KeySets, String> colDifficulty;
    @FXML private TableColumn<KeySets, String> colDescription;
    @FXML private TableColumn<KeySets, String> colKeyBind;
    @FXML private TextField KeyBindCaptureField;
    @FXML private Label RowCountLabel;
    @FXML private Button CreateSetButton;

    private final ICreateSetDAO dao = new SqliteCreateSetDAO();
    private final ObservableList<KeySets> items = FXCollections.observableArrayList();

    // capture state
    private final ObjectProperty<KeyCombination> capturedCombo = new SimpleObjectProperty<>();

    @FXML
    public void initialize() {
        // Load applications
        ApplicationComboBox.getItems().setAll(dao.listApplications());
        // Placeholder when null
        ApplicationComboBox.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? "Select Application" : item);
            }
        });
        ApplicationComboBox.getSelectionModel().clearSelection();

        ApplicationComboBox.setOnAction(e -> loadFor(ApplicationComboBox.getValue()));

        // Table setup
        table.setItems(items);
        table.setEditable(true);

        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colCategory.setCellFactory(TextFieldTableCell.forTableColumn());
        colCategory.setOnEditCommit(evt -> {
            evt.getRowValue().setCategory(evt.getNewValue());
            table.refresh();
        });

        colDifficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        colDifficulty.setCellFactory(ComboBoxTableCell.forTableColumn("Beginner","Intermediate","Advanced"));
        colDifficulty.setOnEditCommit(evt -> {
            evt.getRowValue().setDifficulty(evt.getNewValue());
            table.refresh();
        });

        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDescription.setCellFactory(TextFieldTableCell.forTableColumn());
        colDescription.setOnEditCommit(evt -> {
            evt.getRowValue().setDescription(evt.getNewValue());
            table.refresh();
        });

        colKeyBind.setCellValueFactory(new PropertyValueFactory<>("keyBind"));

        setupKeyCapture();
        updateRowCount();
    }

    private void loadFor(String application) {
        items.clear();
        if (application == null || application.isBlank()) {
            updateRowCount();
            return;
        }
        items.addAll(dao.listKeySetsByApplication(application));
        updateRowCount();
    }

    private void updateRowCount() {
        RowCountLabel.setText(items.isEmpty() ? "No items" : items.size() + " items");
    }

    // --- Keybind capture for selected row ---
    private void setupKeyCapture() {
        KeyBindCaptureField.addEventFilter(KeyEvent.KEY_TYPED, e -> e.consume());
        KeyBindCaptureField.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                capturedCombo.set(null);
                KeyBindCaptureField.clear();
                e.consume(); return;
            }
            boolean ctrl = e.isControlDown() || e.getCode() == KeyCode.CONTROL;
            boolean alt = e.isAltDown() || e.getCode() == KeyCode.ALT;
            boolean shift = e.isShiftDown() || e.getCode() == KeyCode.SHIFT;
            boolean meta = e.isMetaDown() || e.getCode() == KeyCode.META;

            KeyCode code = e.getCode();
            if (code.isModifierKey()) {
                KeyBindCaptureField.setText(formatPartial(ctrl, alt, shift, meta));
                e.consume(); return;
            }

            List<KeyCombination.Modifier> mods = new ArrayList<>();
            if (ctrl) mods.add(KeyCombination.CONTROL_DOWN);
            if (alt) mods.add(KeyCombination.ALT_DOWN);
            if (shift) mods.add(KeyCombination.SHIFT_DOWN);
            if (meta) mods.add(KeyCombination.META_DOWN);

            KeyCodeCombination combo = new KeyCodeCombination(code, mods.toArray(new KeyCombination.Modifier[0]));
            capturedCombo.set(combo);
            KeyBindCaptureField.setText(combo.getDisplayText());
            e.consume();
        });
        KeyBindCaptureField.setPromptText("Click here and press shortcut (e.g., Ctrl+C)");
    }

    private String formatPartial(boolean ctrl, boolean alt, boolean shift, boolean meta) {
        List<String> parts = new ArrayList<>();
        if (ctrl) parts.add("Ctrl");
        if (alt) parts.add("Alt");
        if (shift) parts.add("Shift");
        if (meta) parts.add("Meta");
        return String.join("+", parts) + (parts.isEmpty() ? "" : "+ …");
    }

    @FXML
    private void onApplyKeyBindToSelected() {
        KeySets sel = table.getSelectionModel().getSelectedItem();
        if (sel == null || capturedCombo.get() == null) return;
        sel.setKeyBind(capturedCombo.get().getDisplayText());
        table.refresh();
    }

    @FXML
    private void onDeleteSelected() {
        KeySets sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        if (dao.deleteKeySet(sel.getId())) {
            items.remove(sel);
            updateRowCount();
        }
    }

    @FXML
    private void onSaveChanges() {
        for (KeySets ks : items) {
            dao.updateKeySet(ks);
        }
    }

    @FXML
    private void onClose() {
        try{
            // Load homebase layout
            FXMLLoader baseLoader = new FXMLLoader(HelloApplication.class.getResource("homebase.fxml"));
            Parent root = baseLoader.load();

            // Get controller of homebase
            HomeBaseController baseController = baseLoader.getController();

            baseController.setContent("/com/example/FACT/createSet.fxml");

            // Get current stage from the login button
            Stage newStage = new Stage();
            Stage currentStage = (Stage) CreateSetButton.getScene().getWindow();
            // Set the new scene with the base layout
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();
            newStage.initStyle(StageStyle.UNDECORATED);
            currentStage.close();

        }
        catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
}
