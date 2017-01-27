package com.intuso.housemate.globalserver.database.inmemory;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.globalserver.database.Database;
import com.intuso.housemate.globalserver.database.model.*;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by tomc on 21/01/17.
 */
public class InMemoryDatabase implements Database {

    private final ManagedCollection<Listener> listeners;

    private final TreeMap<String, User> users = Maps.newTreeMap();
    private final TreeMap<String, Client> clients = Maps.newTreeMap();
    private final Map<String, Authorisation> authzGrants = Maps.newHashMap();
    private final Map<String, Token> tokens = Maps.newHashMap();

    @Inject
    public InMemoryDatabase(ManagedCollectionFactory managedCollectionFactory) {
        listeners = managedCollectionFactory.create();
    }

    @Override
    public Stream<User> getUsers() {
        return users.values().stream();
    }

    @Override
    public Page<User> getUserPage(long offset, int limit) {
        return page(users.values().stream(), offset, limit, users.size());
    }

    @Override
    public User getUser(String id) {
        return users.get(id);
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
        for(Listener listener : listeners)
            listener.userUpdated(user);
    }

    @Override
    public void deleteUser(String id) {
        users.remove(id);
    }

    @Override
    public Page<Client> getClientPage(long offset, int limit) {
        return page(clients.values().stream(), offset, limit, clients.size());
    }

    @Override
    public Client getClient(String id) {
        return clients.get(id);
    }

    @Override
    public void updateClient(Client client) {
        clients.put(client.getId(), client);
        for(Listener listener : listeners)
            listener.clientUpdated(client);
    }

    @Override
    public void deleteClient(String id) {
        clients.remove(id);
    }

    @Override
    public void updateAuthorisation(Authorisation authorisation) {
        authzGrants.put(authorisation.getCode(), authorisation);
    }

    @Override
    public Authorisation getAuthorisation(String code) {
        return authzGrants.get(code);
    }

    @Override
    public void deleteAuthorisation(String code) {
        authzGrants.remove(code);
    }

    @Override
    public void updateToken(Token token) {
        tokens.put(token.getToken(), token);
    }

    @Override
    public Token getToken(String token) {
        return tokens.get(token);
    }

    @Override
    public void deleteToken(String token) {
        tokens.remove(token);
    }

    @Override
    public Page<Token> getUserTokenPage(String id, long offset, int limit) {
        return page(tokens.values().stream().filter(token -> token.getUser().getId().equals(id)), offset, limit, 0);
    }

    @Override
    public Page<Token> getClientTokenPage(String id, long offset, int limit) {
        return page(tokens.values().stream().filter(token -> token.getClient().getId().equals(id)), offset, limit, 0);
    }

    @Override
    public ManagedCollection.Registration addListener(Listener listener) {
        return listeners.add(listener);
    }

    private <T> Page<T> page(Stream<T> stream, long offset, int limit, long total) {
        if(offset > 0)
            stream = stream.skip(offset);
        if(limit >= 0)
            stream = stream.limit(limit);
        return new Page<>(offset, total, stream.collect(Collectors.toList()));
    }
}
