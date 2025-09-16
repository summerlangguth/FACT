import com.example.FACT.model.*;
import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/// created by george n11276941
class CreateSetTest {

    private Connection conn;
    private SqliteCreateSetDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        Path tmp = Files.createTempFile("quickcuts-test-", ".db");
        conn = DriverManager.getConnection("jdbc:sqlite:" + tmp.toAbsolutePath());
        dao  = new SqliteCreateSetDAO(conn, true); // or default ctor that seeds

        try (Statement s = conn.createStatement()) {
            s.executeUpdate("DELETE FROM applications");
            s.executeUpdate("DELETE FROM KeySets");
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        if (conn != null) conn.close();
    }

    @Test
    void addAndListApplications_shouldPersistAndReturnSorted() {
        assertTrue(dao.addApplication("Inventor"));
        assertTrue(dao.addApplication("Word"));
        assertFalse(dao.addApplication("Inventor"));
        assertEquals(List.of("Inventor", "Word"), dao.listApplications());
    }

    @Test
    void addKeySet_thenQueryByApplication_shouldReturnRow() {
        dao.addApplication("VS Code");
        KeySets ks = new KeySets("VS Code","Editing","Beginner","Copy","Ctrl+C");
        assertTrue(dao.addKeySet(ks));

        List<KeySets> got = dao.listKeySetsByApplication("VS Code");
        assertEquals(1, got.size());
        KeySets row = got.get(0);
        assertEquals("Editing", row.getCategory());
        assertEquals("Beginner", row.getDifficulty());
        assertEquals("Copy", row.getDescription());
        assertEquals("Ctrl+C", row.getKeyBind());
        assertTrue(row.getId() > 0);
    }

    @Test
    void updateKeySet_shouldModifyFields() {
        dao.addApplication("VS Code");
        assertTrue(dao.addKeySet(new KeySets("VS Code","Editing","Beginner","Copy","Ctrl+C")));
        KeySets row = dao.listKeySetsByApplication("VS Code").get(0);

        row.setCategory("Navigation");
        row.setDifficulty("Intermediate");
        row.setDescription("Go to line");
        row.setKeyBind("Ctrl+G");

        assertTrue(dao.updateKeySet(row));

        KeySets updated = dao.listKeySetsByApplication("VS Code").get(0);
        assertEquals("Navigation", updated.getCategory());
        assertEquals("Intermediate", updated.getDifficulty());
        assertEquals("Go to line", updated.getDescription());
        assertEquals("Ctrl+G", updated.getKeyBind());
    }

    @Test
    void deleteKeySet_shouldRemoveRow() {
        dao.addApplication("VS Code");
        assertTrue(dao.addKeySet(new KeySets("VS Code","Editing","Beginner","Copy","Ctrl+C")));
        int id = dao.listKeySetsByApplication("VS Code").get(0).getId();

        assertTrue(dao.deleteKeySet(id));
        assertTrue(dao.listKeySetsByApplication("VS Code").isEmpty());
    }
}
