import com.example.FACT.model.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameEngineTest {

    private User mockUser;
    private UserManager mockUserManager;
    private Shortcut mockShortcut;
    private KeyEvent mockEvent;
    private KeyCombination mockCombo;
    private GameEngine gameEngine;

    @BeforeEach
    public void setUp() throws Exception {
        // mock user + user manager
        mockUser = mock(User.class);
        mockUserManager = mock(UserManager.class);
        when(mockUserManager.getLoggedInUser()).thenReturn(mockUser);
        UserManager.setInstance(mockUserManager);
        // mock shortcut + key event
        mockShortcut = mock(Shortcut.class);
        // simple mock as KeyEvent is final
        mockEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "A", "A", KeyCode.A, false, false, false, false);
        mockCombo = mock(KeyCombination.class);
        when(mockShortcut.getCombo()).thenReturn(mockCombo);

        // initialise GameEngine with one shortcut
        gameEngine = new GameEngine(List.of(mockShortcut));
    }

    @Test
    public void testSetCurrentShortcutsResetsIndex() {
        gameEngine.setCurrentShortcuts(List.of(mockShortcut));
        // after set, index should reset to 0
        assertEquals("1/1", gameEngine.progress());
    }

    @Test
    public void testSetCurrentShortcutsNullList() {
        gameEngine.setCurrentShortcuts(null);
        assertEquals("1/0", gameEngine.progress());
        assertTrue(gameEngine.isFinished());
    }

    @Test
    public void testCurrentReturnsShortcut() {
        assertEquals(mockShortcut, gameEngine.current());
    }

    @Test
    public void testCurrentReturnsNullWhenIndexOutOfBounds() {
        // simulate correct matches for all shortcuts
        when(mockCombo.match(any(KeyEvent.class))).thenReturn(true);

        // advance once (for our single shortcut)
        gameEngine.checkAndAdvance(mockEvent);

        // Now index == size, so current() should return null
        assertNull(gameEngine.current(), "Expected current() to return null after finishing all shortcuts");
        assertTrue(gameEngine.isFinished(), "GameEngine should report finished");
    }

    @Test
    public void testProgressDisplaysCorrectFraction() {
        assertEquals("1/1", gameEngine.progress());
    }
    @Test
    public void testIsFinishedFalseWhenNotDone() {
        assertFalse(gameEngine.isFinished());
    }

    @Test
    public void testIsFinishedTrueWhenDone() {
        when(mockCombo.match(mockEvent)).thenReturn(true);
        gameEngine.checkAndAdvance(mockEvent);
        assertTrue(gameEngine.isFinished());
    }

    @Test
    public void testCheckAndAdvanceCorrectInput() {
        when(mockCombo.match(mockEvent)).thenReturn(true);

        boolean result = gameEngine.checkAndAdvance(mockEvent);

        assertTrue(result);
        verify(mockUser).incrementStreak();
        verify(mockUser).incrementCorrect();
    }

    @Test
    public void testCheckAndAdvanceIncorrectInput() {
        when(mockCombo.match(mockEvent)).thenReturn(false);

        boolean result = gameEngine.checkAndAdvance(mockEvent);

        assertFalse(result);
        verify(mockUser).setStreak(0);
        verify(mockUser).incrementIncorrect();
    }

    @Test
    public void testResetResetsIndex() {
        when(mockCombo.match(mockEvent)).thenReturn(true);
        gameEngine.checkAndAdvance(mockEvent); // index should increase
        assertEquals("2/1", gameEngine.progress()); // index advanced past list size

        gameEngine.reset();
        assertEquals("1/1", gameEngine.progress());
    }
}
