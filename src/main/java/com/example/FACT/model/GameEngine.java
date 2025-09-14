package com.example.FACT.model;

import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import java.util.Collections;
import java.util.List;

/**
 * The GameEngine is used to link the keyboard shortcuts from the SQLite database and
 * load them into the program. It is where the key input methods are stored.
 */
public class GameEngine {

    /**
     * List containing shortcut objects. Initialises an empty list.
     */
    private List<Shortcut> shortcuts = Collections.emptyList();
    /**
     * An integer representing the shortcut we are on, and the number correct. This
     * can be used at the end to also identify how many were answered wrongly.
     */
    private int index = 0;
    private int correct = 0;

    /**
     * Constructs a new GameEngine instance and initializes it with the specified list of shortcuts.
     * The provided list of shortcuts will be set, and the progress will be reset.
     *
     * @param shortcuts the list of shortcut objects to be used by the GameEngine.
     *                  If the list is null, it will default to an empty list.
     */
    public GameEngine(List<Shortcut> shortcuts) {
        setShortcuts(shortcuts);
    }

    /**
     * Updates the shortcuts with the provided list and resets the progress.
     * If the provided list is null, sets the shortcuts to an empty list.
     *
     * @param list the list of shortcuts to set. If null, the shortcuts will default to an empty list.
     */
    public final void setShortcuts(List<Shortcut> list) {
        this.shortcuts = (list != null) ? List.copyOf(list) : Collections.emptyList();
        reset();
    }

    /**
     * Resets the state of the GameEngine to its initial values.
     * This method sets the index to 0, indicating the starting position
     * in the list of shortcuts, and resets the correct count to 0.
     */
    public void reset() {
        index = 0;
        correct = 0;
    }

    /**
     * Retrieves the current shortcut based on the current index in the list of shortcuts.
     * If the index is within bounds, the corresponding shortcut is returned.
     * Otherwise, returns null if the index exceeds the size of the shortcuts list.
     *
     * @return the current Shortcut object if the index is valid, or null if the index is out of bounds.
     */
    public Shortcut current() {
        return (index < shortcuts.size()) ? shortcuts.get(index) : null;
    }

    /**
     * Determines whether the current operation is finished.
     * The operation is considered finished when the current index
     * is greater than or equal to the size of the shortcuts list.
     *
     * @return true if the current index is greater than or equal to the size of the shortcuts list, otherwise false.
     */
    public boolean isFinished() {
        return index >= shortcuts.size();
    }

    /**
     * Returns the number of shortcuts currently stored in the GameEngine.
     *
     * @return the size of the list of shortcuts
     */
    public int size() {
        return shortcuts.size();
    }

    /**
     * Retrieves the current position within the list of shortcuts.
     * If the operation is finished, the position returned is the size of the shortcuts list.
     * Otherwise, the position is the current index incremented by one.
     *
     * @return the current position, which is either the size of the shortcuts list
     *         if the operation is finished, or the current index + 1 otherwise.
     */
    public int position() {
        return isFinished() ? shortcuts.size() : (index + 1);
    }

    /**
     * Retrieves the current count of correct operations or actions.
     *
     * @return the number of correct operations or actions recorded.
     */
    public int correctCount() {
        return correct;
    }

    /**
     * Processes the provided KeyEvent to determine if it matches the current expected key combination
     * and advances to the next shortcut if the match is successful. Only non-modifier key events are
     * considered for comparison.
     *
     * @param e the KeyEvent to be checked against the expected key combination
     * @return true if the KeyEvent matches the expected key combination and the index is advanced, false otherwise
     */
    public boolean checkAndAdvance(KeyEvent e) {
        if (isFinished()) return false;

        switch (e.getCode()) {
            case SHIFT, CONTROL, META, ALT -> { return false; }
            default -> {}
        }

        Shortcut cur = current();
        KeyCombination expected = cur.getCombo();
        boolean ok = expected.match(e);

        if (ok) {
            correct++;
            index++;
            return true;
        }
        return false;
    }
}