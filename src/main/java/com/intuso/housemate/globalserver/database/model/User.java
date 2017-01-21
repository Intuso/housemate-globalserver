package com.intuso.housemate.globalserver.database.model;

/**
 * Created by tomc on 21/01/17.
 */
public class User {

    private String id;

    public User() {}

    public User(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
