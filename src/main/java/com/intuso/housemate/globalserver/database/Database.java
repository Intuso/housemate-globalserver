package com.intuso.housemate.globalserver.database;

import com.intuso.housemate.globalserver.database.model.*;

/**
 * Created by tomc on 21/01/17.
 */
public interface Database {

    Page<User> listUsers(long offset, int limit);
    void addUser(User user);
    User getUser(String id);
    void deleteUser(String id);

    Page<Client> listClients(long offset, int limit);
    void addClient(Client client);
    Client getClient(String id);
    void deleteClient(String id);

    void addAuthorisation(Authorisation authorisation);
    Authorisation getAuthorisation(String code);
    void deleteAuthorisation(String code);

    void addToken(Token token);
    Token getToken(String token);
    void deleteToken(String token);
    Page<Token> getUserTokens(String id, long offset, int limit);
    Page<Token> getClientTokens(String id, long offset, int limit);
}
