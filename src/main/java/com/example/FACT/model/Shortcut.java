package com.example.FACT.model;

import javafx.scene.input.KeyCombination;

public class Shortcut {
    private final String description;
    private final KeyCombination combo;

    /**
     * Constructs a new Shortcut instance with the specified description and key combination.
     *
     * @param description a string describing the purpose or function of the shortcut
     * @param combo the KeyCombination associated with this shortcut
     */
    public Shortcut(String description, KeyCombination combo) {
        this.description = description;
        this.combo = combo;
    }

    /**
     * Retrieves the description associated with the shortcut.
     *
     * @return the description of the shortcut as a String.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieves the key combination associated with the shortcut.
     *
     * @return the KeyCombination of the shortcut.
     */
    public KeyCombination getCombo() {
        return combo;
    }
}