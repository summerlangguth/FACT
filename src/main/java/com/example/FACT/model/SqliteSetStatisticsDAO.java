package com.example.FACT.model;

import java.sql.*;

public class SqliteSetStatisticsDAO {

    private final Connection connection;

    /**
     * initialises with the instance connection
     */
    public SqliteSetStatisticsDAO() {
        connection = SqliteConnection.getInstance();
        initSchema();
    }

    private void initSchema() {
        try {
            Statement statement = connection.createStatement();

            // Enable foreign key enforcement
            statement.execute("PRAGMA foreign_keys = ON;");

            // Create userScores table if it doesn't exist
            String query = "CREATE TABLE IF NOT EXISTS userScores ("
                    + "userEmail TEXT NOT NULL,"
                    + "setName TEXT NOT NULL,"
                    + "maxScore INTEGER NOT NULL,"
                    + "PRIMARY KEY(userEmail, setName),"
                    + "FOREIGN KEY(userEmail) REFERENCES userdetails(email) ON DELETE CASCADE ON UPDATE CASCADE,"
                    + "FOREIGN KEY(setName) REFERENCES applications(name) ON DELETE CASCADE ON UPDATE CASCADE"
                    + ")";
            statement.execute(query);

            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * returns the user's maximum score for the application set
     * @param email user's email (unique id)
     * @param setName application name (combined PK)
     * @return
     */
    public int getMaxScore(String email, String setName){
        String query = "SELECT maxScore FROM userScores WHERE userEmail = ? AND setName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, setName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("maxScore");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * checks to see if the new score is a maximum or if there is no score stored and updates accordingly
     * @param email user's email
     * @param setName application name (combo PK)
     * @param score score of most recent game
     */
    public void updateMaxscore(String email, String setName, Integer score){
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String query = "SELECT * FROM userScores WHERE userEmail = ? AND setName = ?";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, setName);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                if(score > resultSet.getInt("maxScore")){
                    PreparedStatement activeStatement = connection.prepareStatement("UPDATE userScores SET maxScore = ? WHERE userEmail = ? AND setName = ?");
                    activeStatement.setInt(1, score);
                    activeStatement.setString(2, email);
                    activeStatement.setString(3, setName);
                    activeStatement.executeUpdate();
                }
            }
            else{
                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO userScores(userEmail, setName, maxScore) VALUES(?, ?, ?)");
                insertStatement.setString(1, email);
                insertStatement.setString(2, setName);
                insertStatement.setInt(3, score);
                insertStatement.executeUpdate();
                insertStatement.close();
            }
            }
        catch(Exception e){
            e.printStackTrace();
        }
    }


}
