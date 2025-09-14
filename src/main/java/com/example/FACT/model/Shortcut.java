package com.example.FACT.model;

import javafx.scene.input.KeyCombination;

public class Shortcut {
    private final String description;
    private final KeyCombination combo;

    /**
     * Constructs a new Shortcut instance with the specified description and key combination.
     * @param description String describing the shortcut.
     * @param combo the key combination used to activate the shortcut.
     */
    public Shortcut(String description, KeyCombination combo) {
        this.description = description;
        this.combo = combo;
    }

    /**
     * Retrieves the description associated with the shortcut.
     * @return the description of the shortcut as a string.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieves the key combination associated with the shortcut.
     * @return the key combination of the shortcut.
     */
    public KeyCombination getCombo() {
        return combo;
    }
}