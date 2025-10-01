package com.example.FACT.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCombination;
import java.util.Arrays;

public class GameEngineTest {

    private GameEngine engine;
    private Shortcut copyShortcut;
    private Shortcut pasteShortcut;

    // Sets up the Copy and Paste shortcuts as a demo before each test is executed.
    @BeforeEach
    void setUp() {
        copyShortcut = new Shortcut("Copy", KeyCombination.keyCombination("Ctrl+C"));
        pasteShortcut = new Shortcut("Paste", KeyCombination.keyCombination("Ctrl+V"));
        engine = new GameEngine(Arrays.asList(copyShortcut, pasteShortcut));
    }

    // Test if the shortcut being performed matches the current shortcut stored in the GameEngine.
    @Test
    void testCurrentReturnsFirstShortcutInitially() {
        assertEquals(copyShortcut, engine.current());
    }

    // Tests if the progress matches progress stored in the GameEngine.
    @Test
    void testProgressStartsAtFirstShortcut() {
        assertEquals("1/2", engine.progress());
    }

    @Test
    void testCheckAndAdvanceWithCorrectKey() {
        KeyEvent event = new KeyEvent(KeyEvent.KEY_PRESSED, "C", "C", KeyCode.C, true, false, false, false);
        assertTrue(engine.checkAndAdvance(event));
        assertEquals(pasteShortcut, engine.current());
    }

    @Test
    void testCheckAndAdvanceWithIncorrectKey() {
        KeyEvent event = new KeyEvent(KeyEvent.KEY_PRESSED, "X", "X", KeyCode.X, false, false, false, false);
        assertFalse(engine.checkAndAdvance(event));
        assertEquals(copyShortcut, engine.current()); // should not advance
    }

    @Test
    void testIsFinishedAfterAllShortcutsCompleted() {
        // Complete Copy
        KeyEvent copy = new KeyEvent(KeyEvent.KEY_PRESSED, "C", "C", KeyCode.C, true, false, false, false);
        engine.checkAndAdvance(copy);

        // Complete Paste
        KeyEvent paste = new KeyEvent(KeyEvent.KEY_PRESSED, "V", "V", KeyCode.V, true, false, false, false);
        engine.checkAndAdvance(paste);

        assertTrue(engine.isFinished());
    }
}