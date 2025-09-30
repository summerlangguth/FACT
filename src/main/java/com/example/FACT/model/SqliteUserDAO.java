package com.example.FACT.model;

import java.sql.*;
import java.util.List;
import java.time.*;
import java.util.concurrent.TimeUnit;

public class SqliteUserDAO implements IUserDAO{
    private Connection connection;
    private Timestamp current;
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
                    + "firstName VARCHAR NOT NULL,"
                    + "lastName VARCHAR NOT NULL,"
                    + "email VARCHAR NOT NULL UNIQUE,"
                    + "password VARCHAR NOT NULL,"
                    + "lastActive TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "streak INTEGER"
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
     * @throws SQLException exception thrown by database
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
                Timestamp lastActive = resultSet.getTimestamp("lastActive");
                Integer streak = resultSet.getInt("streak");
                setDailyActivity(lastActive, streak, email);
                return true;
            }
            else{
                return false;
            }
        }
        catch(Exception e){
            return false;
        }
    }

    /**
     * updates the database with the user's daily activity streak
     * @param lastActive the date the user last logged in
     * @param streak the user's current activity streak
     * @param email the user's email (unique identifier)
     * @throws SQLException database exception
     */
    public void setDailyActivity(Timestamp lastActive, Integer streak, String email) throws SQLException {
        double active = (double) TimeUnit.DAYS.convert(Timestamp.valueOf(LocalDateTime.now()).getTime() - lastActive.getTime(), TimeUnit.MILLISECONDS);
        PreparedStatement activeStatement = connection.prepareStatement("UPDATE userDetails SET streak = ? WHERE email = ?");
        if(active > 1 && active < 2){
            activeStatement.setInt(1, streak + 1);
        }
        else if (active >= 2){
            activeStatement.setInt(1, 0);
        }
        else if (streak == 0){
            activeStatement.setInt(1, 1);
        }
        activeStatement.setString(2, email);
        activeStatement.executeUpdate();
    }

    /**
     * sets the user's daily activity as an accessible value in the user object
     * is separate from the setDailyActivity as the user instance has to be set in the loginController
     * @param email the user's email
     */
    public void setActivity(String email){
        PreparedStatement statement;
        String query = "SELECT streak FROM userDetails WHERE email = ?";
        ResultSet resultSet;
        try{
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            resultSet = statement.executeQuery();
            if (resultSet.next()){
                int updated = resultSet.getInt("streak");
                UserManager.getInstance().getLoggedInUser().setActivity(updated);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean addUser(User user){
        PreparedStatement statement;
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

    public User createUserObject(String email, String password) throws SQLException{
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String query = "SELECT * FROM userDetails WHERE email = ? AND password = ?";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                return new User(firstName, lastName, email, password);
            }
            else{
                throw new SQLException();
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
