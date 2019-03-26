package com.example.navi_app;

import java.io.Serializable;

/**
 * User
 * Holds all data about a given user
 */
public class User implements Serializable {

    // User email
    public String email;
    // User type
    public String type;

    /**
     * INIT User
     * @param email new user email
     * @param type new user tpye
     */
    public User(String email, String type){

        // Set data
        this.email = email;
        this.type = type;

    }
}
