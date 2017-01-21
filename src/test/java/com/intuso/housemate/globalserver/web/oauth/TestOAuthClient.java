package com.intuso.housemate.globalserver.web.oauth;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by tomc on 21/01/17.
 */
public class TestOAuthClient {

    private final String AUTHZ = "http://localhost:8080/oauth/1.0/authz";
    private final String TOKEN = "http://localhost:8080/oauth/1.0/token";
    private final String TEST = "http://localhost:8080/api/1.0/test";
    private final String CLIENT_ID = "2965157c-29fb-464f-8493-aca7eb4cc64d";
    private final String CLIENT_SECRET = "4fe07cfd-d904-43bc-b54f-c58083988b8b";
    private final String AUTH_CODE = "427fad00694b6bcf84bec615dfcde90a";

    @Test
    public void getAuthCode() throws OAuthSystemException {

        OAuthClientRequest authRequest = OAuthClientRequest
                .authorizationLocation(AUTHZ)
                .setResponseType(ResponseType.CODE.toString())
                .setClientId(CLIENT_ID)
                .setRedirectURI(TEST)
                .buildQueryMessage();

        System.out.println("Visit: " + authRequest.getLocationUri());
    }

    @Test
    public void getTokenAndTestRequest() throws OAuthSystemException, OAuthProblemException, IOException {

        //create OAuth client that uses custom http client under the hood
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

        OAuthClientRequest request = OAuthClientRequest
                .tokenLocation(TOKEN)
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setClientId(CLIENT_ID)
                .setClientSecret(CLIENT_SECRET)
                .setRedirectURI("http://www.example.com/redirect")
                .setCode(AUTH_CODE)
                .buildQueryMessage();

        OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(request);

        String accessToken = oAuthResponse.getAccessToken();
        Long expiresIn = oAuthResponse.getExpiresIn();

        OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(TEST)
                .setAccessToken(accessToken)
                .buildQueryMessage();

        OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
        BufferedReader br = new BufferedReader(new InputStreamReader(resourceResponse.getBodyAsInputStream()));
        String line;
        while((line = br.readLine()) != null)
            System.out.println(line);
    }
}
