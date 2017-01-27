package com.intuso.housemate.globalserver.database;

import com.intuso.housemate.globalserver.database.model.*;
import com.intuso.utilities.collection.ManagedCollection;

import java.util.stream.Stream;

/**
 * Created by tomc on 21/01/17.
 */
public interface Database {

    Stream<User> getUsers();
    Page<User> getUserPage(long offset, int limit);
    void updateUser(User user);
    User getUser(String id);
    void deleteUser(String id);

    Page<Client> getClientPage(long offset, int limit);
    void updateClient(Client client);
    Client getClient(String id);
    void deleteClient(String id);

    void updateAuthorisation(Authorisation authorisation);
    Authorisation getAuthorisation(String code);
    void deleteAuthorisation(String code);

    void updateToken(Token token);
    Token getToken(String token);
    void deleteToken(String token);
    Page<Token> getUserTokenPage(String id, long offset, int limit);
    Page<Token> getClientTokenPage(String id, long offset, int limit);

    ManagedCollection.Registration addListener(Listener listener);

    interface Listener {
        void userUpdated(User user);
        void clientUpdated(Client client);
    }
}
