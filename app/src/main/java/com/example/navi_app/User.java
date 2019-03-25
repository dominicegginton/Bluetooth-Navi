package com.example.navi_app;

import java.io.Serializable;

public class User implements Serializable {
    public String email;
    public String type;

    public User(String email, String type){
        this.email = email;
        this.type = type;
    }
}
