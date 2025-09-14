package com.example.FACT.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteCreateSetDAO implements ICreateSetDAO {
    private final Connection connection;

    public SqliteCreateSetDAO() {
        this.connection = SqliteConnection.getInstance();
        if (this.connection == null) {
            throw new IllegalStateException("SQLite connection is null. Ensure SqliteConnection.getInstance() returns a valid Connection.");
        }
        initSchema();
    }

    // ----- Schema -----
    private void initSchema() {
        // Enable FK (no harm even if none yet)
        try (Statement s = connection.createStatement()) {
            s.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException ignored) {}

        // KeySets table
        final String createKeySets = """
            CREATE TABLE IF NOT EXISTS KeySets (
                ID          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                Application TEXT    NOT NULL,
                Category    TEXT    NOT NULL,
                Difficulty  TEXT    NOT NULL,
                Description TEXT    NOT NULL,
                KeyBind     TEXT    NOT NULL
            );
        """;

        // Applications table (for the ComboBox)
        final String createApplications = """
            CREATE TABLE IF NOT EXISTS applications (
                id   INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL UNIQUE
            );
        """;

        try (Statement st = connection.createStatement()) {
            st.execute(createKeySets);
            st.execute(createApplications);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Optionally seed a few common apps on first run
        seedDefaultApplications();
    }

    private void seedDefaultApplications() {
        // Insert a few if the table is empty
        final String countSql = "SELECT COUNT(*) FROM applications";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(countSql)) {
            if (rs.next() && rs.getInt(1) == 0) {
                addApplication("VS Code");
                addApplication("Blender");
                addApplication("AutoCAD");
                addApplication("Excel");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ----- ICreateSetDAO -----
    @Override
    public boolean addKeySet(KeySets keyset) {
        final String sql = """
            INSERT INTO KeySets (Application, Category, Difficulty, Description, KeyBind)
            VALUES (?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, keyset.getApplication());
            ps.setString(2, keyset.getCategory());
            ps.setString(3, keyset.getDifficulty());
            ps.setString(4, keyset.getDescription());
            ps.setString(5, keyset.getKeyBind());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // NEW: list existing applications (for ComboBox)
    @Override
    public List<String> listApplications() {
        final String sql = "SELECT name FROM applications ORDER BY LOWER(name)";
        List<String> apps = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                apps.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apps;
    }

    // NEW: add a new application (used by “➕ Add application…”)
    @Override
    public boolean addApplication(String name) {
        if (name == null || name.isBlank()) return false;
        final String sql = "INSERT OR IGNORE INTO applications (name) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            int changed = ps.executeUpdate();
            return changed > 0; // false if duplicate due to UNIQUE
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
