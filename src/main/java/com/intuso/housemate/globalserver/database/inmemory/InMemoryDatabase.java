package com.intuso.housemate.globalserver.database.inmemory;

import com.google.common.collect.Maps;
import com.intuso.housemate.globalserver.database.Database;
import com.intuso.housemate.globalserver.database.model.*;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by tomc on 21/01/17.
 */
public class InMemoryDatabase implements Database {

    private final TreeMap<String, User> users = Maps.newTreeMap();
    private final TreeMap<String, Client> clients = Maps.newTreeMap();
    private final Map<String, AuthzGrant> authzGrants = Maps.newHashMap();
    private final Map<String, Token> tokens = Maps.newHashMap();

    @Override
    public Page<User> listUsers(long offset, int limit) {
        return page(users.values().stream(), offset, limit, users.size());
    }

    @Override
    public User getUser(String id) {
        return users.get(id);
    }

    @Override
    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void deleteUser(String id) {
        users.remove(id);
    }

    @Override
    public Page<Client> listClients(long offset, int limit) {
        return page(clients.values().stream(), offset, limit, clients.size());
    }

    @Override
    public Client getClient(String id) {
        return clients.get(id);
    }

    @Override
    public void addClient(Client client) {
        clients.put(client.getId(), client);
    }

    @Override
    public void deleteClient(String id) {
        clients.remove(id);
    }

    @Override
    public void addAuthzGrant(AuthzGrant authzGrant) {
        authzGrants.put(authzGrant.getCode(), authzGrant);
    }

    @Override
    public AuthzGrant getAuthzGrant(String code) {
        return authzGrants.get(code);
    }

    @Override
    public void deleteAuthzGrant(String code) {
        authzGrants.remove(code);
    }

    @Override
    public void addToken(Token token) {
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
    public Page<Token> getUserTokens(String id, long offset, int limit) {
        return page(tokens.values().stream().filter(token -> token.getUser().getId().equals(id)), offset, limit, 0);
    }

    @Override
    public Page<Token> getClientTokens(String id, long offset, int limit) {
        return page(tokens.values().stream().filter(token -> token.getClient().getId().equals(id)), offset, limit, 0);
    }

    private <T> Page<T> page(Stream<T> stream, long offset, int limit, long total) {
        if(offset > 0)
            stream = stream.skip(offset);
        if(limit >= 0)
            stream = stream.limit(limit);
        return new Page<>(offset, total, stream.collect(Collectors.toList()));
    }
}
