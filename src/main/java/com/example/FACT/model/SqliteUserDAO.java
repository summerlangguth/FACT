package com.example.FACT.model;

import java.sql.*;
import java.util.List;

public class SqliteUserDAO implements IUserDAO{
    private Connection connection;

    /**
     * Constructor for the SQLite user data access object.
     * Connects to the instance of the database and creates the table if not already present.
     */
    public SqliteUserDAO() {
        connection = SqliteConnection.getInstance();
        createTable();

    }

    /**
     * used for testing
     * @param connection connection instance
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS userDetails ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "firstName TEXT NOT NULL,"
                    + "lastName TEXT NOT NULL,"
                    + "email TEXT NOT NULL UNIQUE,"
                    + "password VARCHAR NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks that the user has provided valid credentials to login
     * @param email The email entered by the user
     * @param password The email entered by the user
     * @return True if the given credentials match an existing user, false if not.
     * @throws SQLException
     */
    public boolean isLogin(String email, String password) throws SQLException {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String query = "SELECT * FROM userDetails WHERE email = ? AND password = ?";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            else{
                return false;
            }
        }
        catch(Exception e){
            return false;
            //TODO
        }
    }
    @Override
    public boolean addUser(User user){
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("INSERT INTO userDetails (firstName, lastName, email, password) VALUES (?, ?, ?, ?)");
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }

    }
}
