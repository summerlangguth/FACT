import com.example.FACT.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeySetsTest {

    private static final int ID = 1;
    private static final int NEW_ID = 2;
    private static final String APPLICATION = "Photoshop";
    private static final String APPLICATION_TWO = "Illustrator";
    private static final String CATEGORY = "Editing";
    private static final String CATEGORY_TWO = "Drawing";
    private static final String DIFFICULTY = "Intermediate";
    private static final String DIFFICULTY_TWO = "Advanced";
    private static final String DESCRIPTION = "Undo last action";
    private static final String DESCRIPTION_TWO = "Redo last action";
    private static final String KEYBIND = "Ctrl+Z";
    private static final String KEYBIND_TWO = "Ctrl+Y";

    private KeySets keySet;
    private KeySets keySetTwo;

    @BeforeEach
    public void setup() {
        keySet = new KeySets(ID, APPLICATION, CATEGORY, DIFFICULTY, DESCRIPTION, KEYBIND);
        keySetTwo = new KeySets(APPLICATION_TWO, CATEGORY_TWO, DIFFICULTY_TWO, DESCRIPTION_TWO, KEYBIND_TWO);
    }

    @Test
    public void testGetId() {
        assertEquals(ID, keySet.getId());
    }

    @Test
    public void testSetId() {
        keySet.setId(NEW_ID);
        assertEquals(NEW_ID, keySet.getId());
    }

    @Test
    public void testGetApplication() {
        assertEquals(APPLICATION, keySet.getApplication());
    }

    @Test
    public void testSetApplication() {
        keySet.setApplication(APPLICATION_TWO);
        assertEquals(APPLICATION_TWO, keySet.getApplication());
    }

    @Test
    public void testGetCategory() {
        assertEquals(CATEGORY, keySet.getCategory());
    }

    @Test
    public void testSetCategory() {
        keySet.setCategory(CATEGORY_TWO);
        assertEquals(CATEGORY_TWO, keySet.getCategory());
    }

    @Test
    public void testGetDifficulty() {
        assertEquals(DIFFICULTY, keySet.getDifficulty());
    }

    @Test
    public void testSetDifficulty() {
        keySet.setDifficulty(DIFFICULTY_TWO);
        assertEquals(DIFFICULTY_TWO, keySet.getDifficulty());
    }

    @Test
    public void testGetDescription() {
        assertEquals(DESCRIPTION, keySet.getDescription());
    }

    @Test
    public void testSetDescription() {
        keySet.setDescription(DESCRIPTION_TWO);
        assertEquals(DESCRIPTION_TWO, keySet.getDescription());
    }

    @Test
    public void testGetKeyBind() {
        assertEquals(KEYBIND, keySet.getKeyBind());
    }

    @Test
    public void testSetKeyBind() {
        keySet.setKeyBind(KEYBIND_TWO);
        assertEquals(KEYBIND_TWO, keySet.getKeyBind());
    }

    @Test
    public void testConstructorWithoutIdSetsDefaultId() {
        assertEquals(-1, keySetTwo.getId());
        assertEquals(APPLICATION_TWO, keySetTwo.getApplication());
        assertEquals(CATEGORY_TWO, keySetTwo.getCategory());
        assertEquals(DIFFICULTY_TWO, keySetTwo.getDifficulty());
        assertEquals(DESCRIPTION_TWO, keySetTwo.getDescription());
        assertEquals(KEYBIND_TWO, keySetTwo.getKeyBind());
    }
}
