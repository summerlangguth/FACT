package com.example.FACT.controller;

import com.example.FACT.HelloApplication;
import com.example.FACT.model.GameEngine;
import com.example.FACT.model.Shortcut;
import com.example.FACT.model.User;
import com.example.FACT.model.UserManager;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
/**
 * The GameplayController manages the FMXL file and links it to the logic ran by the GameEngine.
 */
public class GameplayController {

    @FXML private BorderPane root;
    @FXML private Label appTitleLabel;
    @FXML private Text shortcutDescText;
    @FXML private FlowPane keysPane;
    @FXML private Label statusLabel;
    @FXML private Label progress;
    @FXML private Label streak;
    @FXML private Button exit;
    private Stage stage;
    private Scene scene;
    private GameEngine engine;
    private final String appTitleDefault = "VS Studio Code";

    /**
     * Creates a new instance of GameEngine, which will now be used to calculate the logic for the gameplay.
     * @param engine Passes in the GameEngine instance that will be used for the program.
     */
    public void setEngine(GameEngine engine) {
        this.engine = engine;
        Platform.runLater(this::refreshUI);
    }


    /**
     * Once the root has been loaded, the program begins listening for the next key event (user input).
     * Also sets the top left title, matching the correct application.
     */
    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            root.getScene().addEventFilter((KeyEvent.KEY_PRESSED), this::onKeyPressed);
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

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * When a key is pressed, this method runs.
     * Checks if the input matches the required key combination.
     * Will ignore if there is no GameEngine.
     * If only a modifier key (e.g. shift, command) has been pressed, is ignored.
     * Will display COMPLETE if finished, as signed by the GameEngine.
     * @param e refers to the key event (user input)
     */
    private void onKeyPressed(KeyEvent e) {
        if (engine.isFinished()) {
            showStatus("COMPLETE", "#2e7d32");
            return;
        }

        if ((e.getCode() == KeyCode.CONTROL)||(e.getCode() == KeyCode.SHIFT)||(e.getCode() == KeyCode.ALT)||(e.getCode() == KeyCode.TAB)){
            showStatus(" ", "#c62828");
        }
        else {
            boolean inputStatus = engine.checkAndAdvance(e);
            if (inputStatus) {
                showStatus("CORRECT", "#2e7d32");
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(ev -> {
                    statusLabel.setText(" ");
                    refreshUI();
                });
                pause.play();
            }
            else{
                showStatus("INCORRECT", "ED2A00");
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(ev -> {
                    statusLabel.setText(" ");
                    refreshUI();
                });
                pause.play();
            }
        }
    }


    /**
     * Refreshes the UI to reflect the state of the GameEngine.
     */
    private void refreshUI() {
        Shortcut currentShortcut = engine.current();
        if (currentShortcut == null) {
            shortcutDescText.setText("All shortcuts complete!");
            keysPane.getChildren().clear();
            return;
        }

        appTitleLabel.setText(currentShortcut.getApplication());
        shortcutDescText.setText(currentShortcut.getDescription());
        keysPane.getChildren().setAll(makeKeycaps(currentShortcut.getCombo()));
        progress.setText(engine.progress());
        streak.setText("Streak: poop");
        //streak.setText("Streak: " + Integer.toString(CurrentUser.getStreak()));
    }

    /**
     * Updates the status Label text at the bottom of the screen.
     * @param text text to be inputted.
     * @param colorHex color of the text.
     */
    private void showStatus(String text, String colorHex) {
        statusLabel.setText(text);
        statusLabel.setStyle("-fx-font-size: 23px; -fx-font-weight: bold; -fx-text-fill: " + colorHex + ";");
    }

    /**
     * Updates the streak at the top of the screen
     */


    /**
     * Replaces modifier keys with their associated symbol.
     * @param combo the key event to be replaced.
     * @return
     */
    private List<Label> makeKeycaps(KeyCombination combo) {
        List<Label> out = new ArrayList<>();
        boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");

        if (combo instanceof KeyCodeCombination k) {
            if (k.getShortcut() == KeyCombination.ModifierValue.DOWN)
                out.add(cap(isMac ? "⌘" : "Ctrl"));
            if (k.getShift() == KeyCombination.ModifierValue.DOWN)
                out.add(cap(isMac ? "⇧" : "Shift"));
            if (k.getAlt() == KeyCombination.ModifierValue.DOWN)
                out.add(cap(isMac ? "⌥" : "Alt"));
            if (k.getControl() == KeyCombination.ModifierValue.DOWN && !isMac)
                out.add(cap("Ctrl"));
            if (k.getMeta() == KeyCombination.ModifierValue.DOWN && !isMac)
                out.add(cap("Meta"));

            out.add(cap(k.getCode().getName().toUpperCase()));
        } else {
            // Fallback if a non-KeyCodeCombination is supplied
            out.add(cap(combo.getName()));
        }
        return out;
    }

    /**
     *
     * @param text
     * @return
     */
    private Label cap(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-padding: 8 12; -fx-background-color: #e9edf3; -fx-background-radius: 6; -fx-font-weight: bold;");
        return l;
    }

    public void exitGameplay(ActionEvent event) throws IOException {
        Parent parentRoot =  FXMLLoader.load(getClass().getResource("/com/example/FACT/homepage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(parentRoot);
        stage.setScene(scene);
        stage.show();

    }

    public void setShortcutsAndStart(List<Shortcut> shortcuts, String appTitle) {
        GameEngine engine = new GameEngine(shortcuts);
        setEngine(engine);
        if (appTitleLabel != null && appTitle != null && !appTitle.isBlank()) {
            appTitleLabel.setText(appTitle);
        }
    }



}