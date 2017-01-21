package com.intuso.housemate.globalserver.oauth;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

/**
 * Created by tomc on 21/01/17.
 */
public class OAuthClientRepositoryImpl implements OAuthClientRepository {

    private final Map<String, String> clients = Maps.newHashMap();
    private final Set<String> authCodes = Sets.newHashSet();
    private final Set<String> tokens = Sets.newHashSet();

    @Override
    public void addClient(String id, String secret) {
        clients.put(id, secret);
    }

    @Override
    public boolean isValidClient(String id, String secret) {
        return clients.containsKey(id) && clients.get(id).equals(secret);
    }

    @Override
    public void addAuthCode(String authCode) {
        authCodes.add(authCode);
    }

    @Override
    public boolean isValidAuthCode(String authCode) {
        return authCodes.contains(authCode);
    }

    @Override
    public void addToken(String token) {
        tokens.add(token);
    }

    @Override
    public boolean isValidToken(String token) {
        return tokens.contains(token);
    }
}
