package com.intuso.housemate.globalserver.web.api.globalserver.v1_0.model;

/**
 * Created by tomc on 08/02/17.
 */
public class LoginResponse {

    private String error;
    private User user;

    public LoginResponse() {}

    public LoginResponse(String error) {
        this.error = error;
    }

    public LoginResponse(User user) {
        this.user = user;
    }

    public LoginResponse(String error, User user) {
        this.error = error;
        this.user = user;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
