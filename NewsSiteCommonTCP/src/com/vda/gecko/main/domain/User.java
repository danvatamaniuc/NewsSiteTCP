package com.vda.gecko.main.domain;

/**
 * Created by 1 on 10/10/2015.
 */
public class User {

    private String username;
    private int    id;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
