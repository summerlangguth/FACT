package com.example.FACT.model;

public class UserManager {
    private static UserManager instance;
    private User loggedInUser;

    public static UserManager getInstance(){
        if(instance == null){
            instance = new UserManager();
        }
        return instance;
    }
    public void setLoggedInUser(User user){this.loggedInUser = user;}
    public User getLoggedInUser(){return this.loggedInUser;}
}
