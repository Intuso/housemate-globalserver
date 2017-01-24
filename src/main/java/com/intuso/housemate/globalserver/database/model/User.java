package com.intuso.housemate.globalserver.database.model;

/**
 * Created by tomc on 21/01/17.
 */
public class User {

    private String id;
    private String serverAddress;

    public User() {}

    public User(String id, String serverAddress) {
        this.id = id;
        this.serverAddress = serverAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }
}
