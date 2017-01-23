package com.intuso.housemate.globalserver.api.globalserver.v1_0.model;

/**
 * Created by tomc on 21/01/17.
 */
public class Client {

    private User owner;
    private String id;
    private String secret;
    private String name;

    public Client() {}

    public Client(User owner, String id, String secret, String name) {
        this.owner = owner;
        this.id = id;
        this.secret = secret;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public static Client from(com.intuso.housemate.globalserver.database.model.Client client) {
        return new Client(User.from(client.getOwner()),
                client.getId(),
                client.getSecret(),
                client.getName());
    }
}
