package com.example.FACT.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

    private static PasswordUtils instance;
    public static PasswordUtils getInstance(){
        if(instance == null){
            instance = new PasswordUtils();
        }
        return instance;
    }
    /**
     * used for testing
     * @param instance
     */
    public static void setInstance(PasswordUtils instance) {
        PasswordUtils.instance = instance;
    }
    public String passwordHash;

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    public static String hashPassword(String rawPassword) {
        return ENCODER.encode(rawPassword);
    }

    public static boolean verifyPassword(String rawPassword, String encodedPassword) {
        return ENCODER.matches(rawPassword, encodedPassword);
    }
    public void setPasswordHash(String hash){
        this.passwordHash = hash;
    }
    public String getPasswordHash(){
        return passwordHash;
    }
}
