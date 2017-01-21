package com.intuso.housemate.globalserver.oauth;

/**
 * Created by tomc on 21/01/17.
 */
public interface OAuthClientRepository {
    void addClient(String id, String secret);
    boolean isValidClient(String id, String secret);
    void addAuthCode(String authCode);
    boolean isValidAuthCode(String authCode);
    void addToken(String token);
    boolean isValidToken(String token);
}
