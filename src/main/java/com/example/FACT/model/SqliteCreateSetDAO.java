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

    private void initSchema() {
        try (Statement s = connection.createStatement()) {
            s.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException ignored) {}

        final String createKeySets = """
            CREATE TABLE IF NOT EXISTS KeySets (
                ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                Application TEXT NOT NULL,
                Category TEXT NOT NULL,
                Difficulty TEXT NOT NULL,
                Description TEXT NOT NULL,
                KeyBind TEXT NOT NULL
            );
        """;
        final String createApplications = """
            CREATE TABLE IF NOT EXISTS applications (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL UNIQUE
            );
        """;

        try (Statement st = connection.createStatement()) {
            st.execute(createKeySets);
            st.execute(createApplications);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        seedDefaultApplications();
    }

    private void seedDefaultApplications() {
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

    @Override
    public java.util.List<KeySets> listKeySetsByApplication(String application) {
        final String sql = """
        SELECT ID, Application, Category, Difficulty, Description, KeyBind
        FROM KeySets WHERE Application = ? ORDER BY Category, Description
    """;
        java.util.List<KeySets> list = new java.util.ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, application);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KeySets k = new KeySets(
                            rs.getInt("ID"),
                            rs.getString("Application"),
                            rs.getString("Category"),
                            rs.getString("Difficulty"),
                            rs.getString("Description"),
                            rs.getString("KeyBind")
                    );
                    list.add(k);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public boolean updateKeySet(KeySets ks) {
        final String sql = """
        UPDATE KeySets SET
            Application = ?, Category = ?, Difficulty = ?, Description = ?, KeyBind = ?
        WHERE ID = ?
    """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, ks.getApplication());
            ps.setString(2, ks.getCategory());
            ps.setString(3, ks.getDifficulty());
            ps.setString(4, ks.getDescription());
            ps.setString(5, ks.getKeyBind());
            ps.setInt(6, ks.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean deleteKeySet(int id) {
        final String sql = "DELETE FROM KeySets WHERE ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }


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
    @Override
    public boolean addApplication(String name) {
        if (name == null || name.isBlank()) return false;
        final String sql = "INSERT OR IGNORE INTO applications (name) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            int changed = ps.executeUpdate();
            return changed > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
