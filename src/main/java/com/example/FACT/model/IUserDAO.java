package com.example.FACT.model;

import java.util.List;

/**
 * Interface for the User Data Access Object that handles
 * the CRUD operations for the user class with the database.
 */
public interface IUserDAO {
    /**
     * Adds a new user to the database.
     * @param user The user to add.
     */
    public boolean addUser(User user);

    /**
     * Retrieves a user from the database.
     * @param id The id of the user to retrieve.
     * @return The user with the given id, or null if not found.
     */
    public User getUser(int id);
    /**
     * Retrieves all users from the database.
     * @return A list of all users in the database.
     */
    public List<User> getAllUsers();
}