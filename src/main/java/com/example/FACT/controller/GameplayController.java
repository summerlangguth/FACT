package com.example.FACT.controller;

import com.example.FACT.model.GameEngine;
import com.example.FACT.model.Shortcut;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * The GameplayController is responsible for managing the user interface and
 * handling gameplay mechanics related to keyboard shortcuts. It interacts
 * with the GameEngine to provide feedback to the user about their performance
 * and updates the UI accordingly.
 *
 * The class listens to key events to evaluate user input, provides real-time
 * feedback (such as "CORRECT" or "INCORRECT"), and updates the display to
 * reflect the next shortcut or the completion status when all shortcuts
 * are done.
 */
public class GameplayController {

    @FXML private BorderPane root;
    @FXML private ImageView logoImageView;
    @FXML private Label appTitleLabel;
    @FXML private Text shortcutDescText;
    @FXML private FlowPane keysPane;
    @FXML private Label statusLabel;
    private GameEngine engine;

    private boolean acceptingInput = true;
    private final String appTitleDefault = "VS Studio Code";

    /**
     * Sets the GameEngine instance to be used by the GameplayController.
     * This method updates the internal reference to the provided engine and
     * ensures the user interface is refreshed to reflect the current state
     * of the engine.
     *
     * @param engine the GameEngine instance to be set. This object manages the
     *               game logic, including shortcuts and key input handling.
     */
    public void setEngine(GameEngine engine) {
        this.engine = engine;
        Platform.runLater(this::refreshUI);
    }

    /**
     * Initializes the controller after its root element has been completely loaded.
     * This method is automatically invoked by the JavaFX runtime.
     *
     * Behavior:
     * - Attaches a key listener to the Scene once it becomes available. The key
     *   listener processes key press events through the `onKeyPressed` method.
     * - Updates the application title label with the default title text upon
     *   initialization.
     *
     * This method sets up essential functionality for the gameplay session by
     * ensuring proper event handling and interface readiness.
     */
    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            root.getScene().addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
            appTitleLabel.setText(appTitleDefault);
        });
    }

    /**
     * Handles the key press event triggered during the gameplay session. The method processes
     * the KeyEvent to determine if it matches the expected key combination, updates the game
     * state accordingly, and manages the user interface to provide feedback on correctness.
     *
     * Behavior:
     * - Ignores events if the game engine is null or input is not currently accepted.
     * - Does not process key events for modifier-only keys (e.g., SHIFT, CONTROL).
     * - If the engine signals that the game is finished, displays a completion status.
     * - Checks the input key against the expected key combination, provides visual feedback
     *   for correct or incorrect input, and pauses input briefly after correct input.
     *
     * @param e the KeyEvent corresponding to a user's key press
     */
    private void onKeyPressed(KeyEvent e) {
        if (engine == null || !acceptingInput) return;

        if (isModifierOnly(e)) {
            e.consume();
            return;
        }

        if (engine.isFinished()) {
            showStatus("COMPLETE", "#2e7d32");
            return;
        }

        boolean ok = engine.checkAndAdvance(e);
        if (ok) {
            acceptingInput = false; // pause input while we display CORRECT
            showStatus("CORRECT", "#2e7d32");
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(ev -> {
                statusLabel.setText(" ");
                refreshUI();
                acceptingInput = true;
            });
            pause.play();
        } else {
            showStatus("INCORRECT", "#c62828");
        }

        e.consume();
    }

    /**
     * Updates the user interface to reflect the current state of the `engine`.
     * The method retrieves the current shortcut from the engine, updates the
     * descriptive text for the shortcut, and displays the corresponding key
     * combination as a set of styled labels. If no shortcuts remain, a completion
     * message is shown and the key combination display is cleared.
     *
     * Behavior:
     * - If `engine` is null, the method returns without making changes.
     * - If there is no current shortcut (i.e., engine returns null), the user
     *   interface displays a message indicating that all shortcuts are complete.
     * - If a valid shortcut exists, the description of the shortcut is displayed
     *   and the associated key combination is rendered visually in the `keysPane`
     *   using styled labels generated by the `makeKeycaps` method.
     */
    private void refreshUI() {
        if (engine == null) return;

        Shortcut cur = engine.current();
        if (cur == null) {
            shortcutDescText.setText("All shortcuts complete!");
            keysPane.getChildren().clear();
            return;
        }

        shortcutDescText.setText(cur.getDescription());
        keysPane.getChildren().setAll(makeKeycaps(cur.getCombo()));
    }

    /**
     * Updates the status text and applies a specific color for the text using the provided
     * parameters. The updated status is displayed using the `appTitleLabel` label.
     *
     * @param text     the status text to be displayed in the label.
     * @param colorHex the color to apply to the text, specified in hexadecimal format
     *                 (e.g., "#FFFFFF" for white).
     */
    private void showStatus(String text, String colorHex) {
        statusLabel.setText(text);
        statusLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + colorHex + ";");
    }

    /**
     * Determines if the given key event corresponds solely to a modifier key.
     *
     * @param e the KeyEvent to be checked
     * @return true if the key associated with the event is a modifier key
     *         (SHIFT, CONTROL, META, or ALT); false otherwise
     */
    private static boolean isModifierOnly(KeyEvent e) {
        KeyCode c = e.getCode();
        return c == KeyCode.SHIFT || c == KeyCode.CONTROL || c == KeyCode.META || c == KeyCode.ALT;
    }

    /**
     * Generates a list of styled Label elements representing the key combination provided.
     * This method formats the key combination into human-readable labels with platform-specific modifiers.
     *
     * @param combo the key combination to be transformed into styled label elements.
     *              It can be an instance of KeyCodeCombination or any other implementation of KeyCombination.
     * @return a list of Label objects representing the key combination with appropriate styles and platform-specific symbols.
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
     * Creates a styled Label with the provided text.
     * The label is styled with specific padding, background color, rounded corners,
     * and bold font weight for a consistent visual appearance.
     *
     * @param text the content to be displayed on the created Label.
     * @return a Label instance with the specified text and applied styles.
     */
    private Label cap(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-padding: 8 12; -fx-background-color: #e9edf3; -fx-background-radius: 6; -fx-font-weight: bold;");
        return l;
    }
}