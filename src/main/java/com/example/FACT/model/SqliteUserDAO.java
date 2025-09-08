package com.example.FACT.model;

import java.sql.*;
import java.util.List;

public class SqliteUserDAO implements IUserDAO{
    private Connection connection;

    public SqliteUserDAO() {
        connection = SqliteConnection.getInstance();
        createTable();

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

    public boolean isLogin(String email, String password) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
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
        } finally {
            preparedStatement.close();
            resultSet. close();
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

    @Override
    public User getUser(int id) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }
}
