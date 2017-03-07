package com.intuso.housemate.globalserver.database.model;

/**
 * Created by tomc on 21/01/17.
 */
public class Token {

    private String id;
    private Client client;
    private User user;
    private String token;
    private String refreshToken;
    private long expiresAt;

    public Token() {}

    public Token(String id, Client client, User user, String token, String refreshToken, long expiresAt) {
        this.client = client;
        this.user = user;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }
}
