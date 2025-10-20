import com.example.FACT.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShortcutTest {

    private static final String APPLICATION = "Photoshop";
    private static final String APPLICATION_TWO = "Illustrator";
    private static final String DESCRIPTION = "Copy selection";
    private static final String DESCRIPTION_TWO = "Paste selection";
    private static final KeyCombination COMBO = new KeyCodeCombination(javafx.scene.input.KeyCode.C, KeyCombination.CONTROL_DOWN);
    private static final KeyCombination COMBO_TWO = new KeyCodeCombination(javafx.scene.input.KeyCode.V, KeyCombination.CONTROL_DOWN);

    private Shortcut shortcut;
    private Shortcut shortcutTwo;

    @BeforeEach
    public void setup() {
        shortcut = new Shortcut(APPLICATION, DESCRIPTION, COMBO);
        shortcutTwo = new Shortcut(APPLICATION_TWO, DESCRIPTION_TWO, COMBO_TWO);
    }

    @Test
    public void testGetApplication() {
        assertEquals(APPLICATION, shortcut.getApplication());
    }

    @Test
    public void testGetDescription() {
        assertEquals(DESCRIPTION, shortcut.getDescription());
    }

    @Test
    public void testGetCombo() {
        assertEquals(COMBO, shortcut.getCombo());
    }

    @Test
    public void testConstructorAssignsValuesCorrectly() {
        assertEquals(APPLICATION_TWO, shortcutTwo.getApplication());
        assertEquals(DESCRIPTION_TWO, shortcutTwo.getDescription());
        assertEquals(COMBO_TWO, shortcutTwo.getCombo());
    }
}
