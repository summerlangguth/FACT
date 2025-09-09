package com.example.FACT.model;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class Shortcut {
    private final String description;
    private final KeyCombination combo;

    public Shortcut(String description, KeyCombination combo){
        this.description = description;
        this.combo = combo;
    }

    public String getDescription(){
        return description;
    }

    public KeyCombination getCombo(){
        return combo;
    }
}