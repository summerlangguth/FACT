package com.example.FACT.model;
/**
 * A simple model class representing a user with a first name, last name, email, and password.
 */
public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Integer streak;
    private Float accuracy;
    private Integer numIncorrect;
    private Integer numCorrect;
    /**
     * Constructs a new user with the specified first name, last name, email, and password.
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @param email The email of the user
     * @param password The phone number of the user
     */
    public User(String firstName, String lastName, String email, String password){
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPassword(password);
        setStreak(0);
        setNumCorrect(1);
        setNumIncorrect(1);
    }

    /**
     * retrieves the id of the user
     * @return the user's id
     */
    public int getId(){
        return id;
    }

    /**
     * retrieves the first name of the user
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * sets the user's first name to the value given
     * @param firstName the user's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * retrieves the last name of the user
     * @return the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * sets the user's last name to the value given
     * @param lastName the user's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * retrieves the user's email
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * sets the user's email to the value given
     * @param email the user's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * retrieves the user's password
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * sets the user's streak
     * @param streak the users streak
     */
    public void setStreak(int streak) {
        this.streak = streak;
    }

    /**
     * retrieves the users streak
     * @return the users streak
     */
    public Integer getStreak() {
        return streak;
    }

    /**
     * increments the users streak
     */
    public void incrementStreak() {
        streak++;
    }

    /**
     * sets the users accuracy
     * @param accuracy the users accuracy
     */
    public void setAccuracy(float accuracy){ this.accuracy = accuracy; }

    /**
     * gets the users accuracy
     * @return the users accuracy
     */
    public Float getAccuracy(){return accuracy;}

    /**
     * sets the users correct answers
     * @param numCorrect the users correct answers
     */
    public void setNumCorrect(int numCorrect){ this.numCorrect = numCorrect; }

    /**
     * gets the users number of correct answers
     * @return the users number of correct answers
     */
    public Integer getNumCorrect(){return numCorrect;}

    /**
     * Increments the number of correct answers by 1
     */
    public void incrementCorrect(){numCorrect++;}

    /**
     * sets the users correct answers
     * @param numIncorrect the users correct answers
     */
    public void setNumIncorrect(int numIncorrect){ this.numIncorrect = numIncorrect; }

    /**
     * gets the users number of correct answers
     * @return the users number of correct answers
     */
    public Integer getNumIncorrect(){return numIncorrect;}

    /**
     * Increments the number of incorrect answers by 1
     */
    public void incrementIncorrect(){numIncorrect++;}

    /**
     * calculates the users accuracy
     */
    public Float calculateAccuracy(){
        return (float)((numCorrect/numIncorrect)*100);
    }

}
