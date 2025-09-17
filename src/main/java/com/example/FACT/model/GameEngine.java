package com.example.FACT.model;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import java.util.Collections;
import java.util.List;

public class GameEngine {

    private User CurrentUser = UserManager.getInstance().getLoggedInUser();

    /**
     * List containing shortcut objects. Initialises an empty list.
     */
    private List<Shortcut> shortcuts = Collections.emptyList();
    /**
     * An integer representing the shortcut we are on. This can be used at the end to also identify how many were answered wrongly.
     */
    private int index = 0;

    public GameEngine(List<Shortcut> shortcuts) {
        setShortcuts(shortcuts);
    }

    /**
     * Copies a list of shortcuts and pastes into the GameEngine.
     * @param list shortcut list to be used for Gameplay.
     */
    public final void setShortcuts(List<Shortcut> list) {
        this.shortcuts = (list != null) ? List.copyOf(list) : Collections.emptyList();
        reset();
    }

    /**
     * Reset index and correct to 0.
     */
    public void reset() {
        index = 0;
    }

    /**
     * Returns which position the current shortcut is in the list.
     * @return Integer for index position, or null if finished.
     */
    public Shortcut current() {
        return (index < shortcuts.size()) ? shortcuts.get(index) : null;
    }

    public String progress(){
        int position = index +1;
        int total = shortcuts.size();
        return position + "/" + total;
    }

    /**
     * Compares shortcut number to size of list, if equal or bigger, the course is finished.
     * @return boolean value.
     */
    public boolean isFinished() {
        return index >= shortcuts.size();
    }

    /**
     * Checks to see if user input matches the expected key event combo. If able to move on, will increase correct
     * and index respectively.
     * @param e key combination entered
     * @return returns boolean value to see if the next shortcut can be loaded,
     */
    public boolean checkAndAdvance(KeyEvent e) {
        KeyCombination expected = current().getCombo();
        boolean currentInput = expected.match(e);

        if (currentInput) {
            index++;
            CurrentUser.incrementStreak();
            CurrentUser.incrementCorrect();
            return true;
        }
        else {
            CurrentUser.setStreak(0);
            CurrentUser.incrementIncorrect();
            return false;
        }
    }
}