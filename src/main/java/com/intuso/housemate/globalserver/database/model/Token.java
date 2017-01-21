package com.intuso.housemate.globalserver.database.model;

/**
 * Created by tomc on 21/01/17.
 */
public class Token {

    private Client client;
    private User user;
    private String token;

    public Token() {}

    public Token(Client client, User user, String token) {
        this.client = client;
        this.user = user;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
