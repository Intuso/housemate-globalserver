package com.intuso.housemate.globalserver.database.model;

/**
 * Created by tomc on 21/01/17.
 */
public class Authorisation {

    private Client client;
    private User user;
    private String code;

    public Authorisation() {}

    public Authorisation(Client client, User user, String code) {
        this.client = client;
        this.user = user;
        this.code = code;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
