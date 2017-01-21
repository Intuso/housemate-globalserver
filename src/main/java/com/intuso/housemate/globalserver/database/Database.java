package com.intuso.housemate.globalserver.database;

import com.intuso.housemate.globalserver.database.model.*;

/**
 * Created by tomc on 21/01/17.
 */
public interface Database {

    Page<User> listUsers(long offset, int limit);
    User getUser(String id);
    void addUser(User user);
    void deleteUser(String id);

    Page<Client> listClients(long offset, int limit);
    Client getClient(String id);
    void addClient(Client client);
    void deleteClient(String id);

    void addAuthzGrant(AuthzGrant authzGrant);
    AuthzGrant getAuthzGrant(String code);
    void deleteAuthzGrant(String code);

    void addToken(Token token);
    Token getToken(String token);
    void deleteToken(String token);
    Page<Token> getUserTokens(String id, long offset, int limit);
    Page<Token> getClientTokens(String id, long offset, int limit);
}
