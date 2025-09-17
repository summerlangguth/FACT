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

}