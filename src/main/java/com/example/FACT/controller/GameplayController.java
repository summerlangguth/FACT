package com.example.FACT.controller;

import com.example.FACT.model.*;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameplayController {

    @FXML private BorderPane root;
    @FXML private Label appTitleLabel;
    @FXML private Text shortcutDescText;
    @FXML private FlowPane keysPane;
    @FXML private Label statusLabel;
    @FXML private Label progress;
    @FXML private Label streak;
    @FXML private Label finalScore;
    @FXML private Label keysToPress;
    @FXML private Button exitButton;
    @FXML private Button skipButton;
    private Stage stage;
    private Scene scene;
    private GameEngine engine;
    private Integer streakNumber;
    private Integer maxStreakNumber;//stored as a local variable
    private Integer correctScore;
    private Integer skippedShortCuts;
    private String storedAppTitle;
    public SqliteSetStatisticsDAO model = new SqliteSetStatisticsDAO();
    /**
     * Method that attaches a new GameEngine instance to the GameplayController.
     * @param engine New GameEngine instance.
     */
    public void setEngine(GameEngine engine) {
        this.engine = engine;
        Platform.runLater(this::refreshUI);
    }

    /**
     * This method is automatically invoked when the Gameplay runs. Beings listening for key press events.
     */
    @FXML
    private void initialize() {
        streakNumber = 0;
        maxStreakNumber = 0;
        skippedShortCuts = 0;
        correctScore = 0;
        finalScore.setVisible(false);
        Platform.runLater(() -> {
            root.getScene().addEventFilter((KeyEvent.KEY_PRESSED), this::onKeyPressed);
        });
    }

    /**
     * Sets the content of the view by loading the specified FXML resource.
     * If the resource is not found or an error occurs while loading,
     * an error message is printed to the console.
     *
     * @param fxmlResourcePath the path of the FXML resource to be loaded.
     */
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

        if ((e.getCode() == KeyCode.CONTROL)||(e.getCode() == KeyCode.SHIFT)||(e.getCode() == KeyCode.ALT)||(e.getCode() == KeyCode.TAB)||(e.getCode() == KeyCode.COMMAND)){
            showStatus(" ", "#c62828");
        }
        else {
            boolean inputStatus = engine.checkAndAdvance(e);
            if (inputStatus) {
                updateStats(true);
                showStatus("CORRECT", "#2e7d32");
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(ev -> {
                    statusLabel.setText(" ");
                    refreshUI();
                });
                pause.play();
            }
            else{
                updateStats(false);
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
            finalScore.setVisible(true);
            Integer score = (correctScore / engine.size()) * 100;
            finalScore.setText("Final Score: " + score + " %");
            streak.setText("Maximum streak: " + maxStreakNumber);
            keysToPress.setText("All shortcuts complete!");
            shortcutDescText.setText("Skipped ShortCuts: " + skippedShortCuts);
            skipButton.setVisible(false);
            keysPane.getChildren().clear();
            storeScore(score);
            return;
        }
        appTitleLabel.setText(storedAppTitle);
        shortcutDescText.setText(currentShortcut.getDescription());
        keysPane.getChildren().setAll(makeKeycaps(currentShortcut.getCombo()));
        progress.setText(engine.progress());
        streak.setText("Streak: " + streakNumber);
        keysToPress.setText("Keys to Press");
    }

    /**
     * Updates the Status Label (Correct or Incorrect).
     * @param text Status Label text.
     * @param colorHex Status Label colour.
     */
    private void showStatus(String text, String colorHex) {
        statusLabel.setText(text);
        statusLabel.setStyle("-fx-font-size: 30px; -fx-font-family: Helvetica; -fx-font-weight:bold; -fx-text-fill: " + colorHex + ";");
    }

    private void storeScore(Integer score){
        String email = UserManager.getInstance().getLoggedInUser().getEmail();
        model.updateMaxscore(email,storedAppTitle, score);
    }
    private void updateStats(boolean correct){
        if(correct){
            streakNumber = streakNumber + 1;
            correctScore = correctScore + 1;
            if(streakNumber > maxStreakNumber){
                maxStreakNumber = streakNumber;
            }
        }
        else{
            streakNumber = 0;
        }
    }

    private List<Label> makeKeycaps(KeyCombination combo) {
        List<Label> out = new ArrayList<>();
        boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");

        if (combo instanceof KeyCodeCombination k) {
            if (k.getShortcut() == KeyCombination.ModifierValue.DOWN)
                out.add(cap( isMac ? "⌘": "Ctrl"));
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

    private Label cap(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-padding: 8 12; -fx-background-color: #e9edf3; -fx-background-radius: 6; -fx-font-weight: bold;");
        return l;
    }
    public void skipShortcut(){
        skippedShortCuts = skippedShortCuts + 1;
        engine.skipSet();
        refreshUI();

    }
    public void exitGameplay(ActionEvent event) throws IOException {
        try{
            // Load base shell
            URL baseUrl = getClass().getResource("/com/example/FACT/homebase.fxml");
            FXMLLoader baseLoader = new FXMLLoader(Objects.requireNonNull(baseUrl, "homebase.fxml not found"));
            Parent baseRoot = baseLoader.load();

            // Ask BaseController to show Home inside the center
            com.example.FACT.controller.BaseController baseController = baseLoader.getController();
            baseController.setContent("/com/example/FACT/setSelector.fxml");

            // Reuse the current stage
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.setScene(new Scene(baseRoot));
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public void setShortcutsAndStart(List<Shortcut> shortcuts, String appTitle) {
        GameEngine engine = new GameEngine(shortcuts);
        setEngine(engine);
        if (appTitleLabel != null && appTitle != null && !appTitle.isBlank()) {
            storedAppTitle = appTitle;
            appTitleLabel.setText(appTitle);
        }
    }
}