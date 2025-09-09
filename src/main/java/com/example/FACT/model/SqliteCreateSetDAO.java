package com.example.FACT.model;
import java.sql.*;
import java.util.List;

public class SqliteCreateSetDAO implements ICreateSetDAO {
    private Connection connection;

    public SqliteCreateSetDAO() {
        connection = SqliteConnection.getInstance();
        createTable();

    }
    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS KeySets ("
                    + "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL"
                    + "Application TEXT NOT NULL,"
                    + "Category TEXT NOT NULL,"
                    + "Difficulty TEXT NOT NULL,"
                    + "Description TEXT NOT NULL,"
                    + "KeyBind TEXT NOT NULL,"
                    + ")";

            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean addKeySet(KeySets keyset){
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("INSERT INTO KeySets (Application, Category, Difficulty, Description, KeyBind) VALUES (?, ?, ?, ?, ?)");
            statement.setString(1, keyset.getApplication());
            statement.setString(2, keyset.getCategory());
            statement.setString(3, keyset.getDifficulty());
            statement.setString(4, keyset.getDescription());
            statement.setString(5, keyset.getKeyBind());

            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }

    }

}
