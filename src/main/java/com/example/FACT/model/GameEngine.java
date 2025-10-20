package com.example.FACT.model;

import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import java.util.Collections;
import java.util.List;

public class GameEngine {

    /**
     * Retrieves current user.
     */
    private User CurrentUser = UserManager.getInstance().getLoggedInUser();

    /**
     * Empty shortcut list.
     */
    private List<Shortcut> currentShortcuts = Collections.emptyList();

    /**
     * Progress integer.
     */
    private int index = 0;

    public GameEngine(List<Shortcut> shortcuts) {
        setCurrentShortcuts(shortcuts);
    }

    /**
     * Updates the current list of shortcuts. If the provided list is null, an empty list is assigned.
     * Resets the progress index after updating the shortcuts.
     *
     * @param list the new list of Shortcut objects to be set. If null, the current list will be set
     *             to an empty list.
     */
    public final void setCurrentShortcuts(List<Shortcut> list) {
        this.currentShortcuts = (list != null) ? List.copyOf(list) : Collections.emptyList();
        reset();
    }

    /**
     * Resets index to 0.
     */
    public void reset() {
        index = 0;
    }

    /**
     * Retrieves the current shortcut from the list based on the index.
     * @return the current Shortcut object at the specified index, or null if the index exceeds the list size.
     */
    public Shortcut current() {
        return (index < currentShortcuts.size()) ? currentShortcuts.get(index) : null;
    }

    public String progress() {
        int position = index + 1;
        int total = shortcuts.size();
        return position + "/" + total;
    }

    /**
     * Compares shortcut number to size of list, if equal or bigger, the course is finished.
     * @return Boolean True if finished, otherwise False.
     */
    public boolean isFinished() {
        return index >= currentShortcuts.size();
    }

    /**
     * Validates the provided key event against the current shortcut's key combination. If the key
     * event matches the expected combination, the progress index is incremented, and the user's streak
     * and correct answer count are updated. If the key event does not match, the user's streak is reset,
     * and their incorrect answer count is incremented.
     *
     * @param e the KeyEvent to be validated against the current shortcut's key combination
     * @return true if the provided key event matches the current shortcut's key combination, false otherwise
     */

    public boolean checkAndAdvance(KeyEvent e) {
        KeyCombination expected = current().getCombo();
        boolean currentInput = expected.match(e);

        if (currentInput) {
            index++;
            //CurrentUser.incrementStreak();
            //CurrentUser.incrementCorrect();
            return true;
        } else {
            //CurrentUser.setStreak(0);
            //CurrentUser.incrementIncorrect();
            return false;
        }

    }
}