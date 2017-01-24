package com.intuso.housemate.globalserver.api.oauth.v1_0;

import com.intuso.housemate.globalserver.database.Database;
import com.intuso.housemate.globalserver.database.model.Authorisation;
import com.intuso.housemate.globalserver.database.model.Client;
import com.intuso.housemate.globalserver.database.model.Token;
import com.intuso.housemate.globalserver.database.model.User;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by tomc on 21/01/17.
 */
@Path("/")
public class OAuthResource {

    private final Database database;

    @Inject
    public OAuthResource(Database database) {
        this.database = database;
    }

    @GET
    @Path("/authz")
    public Response authorize(@Context HttpServletRequest request) throws URISyntaxException, OAuthSystemException {
        try {

            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

            User user = database.getUser("294c7ff2-6dbd-4522-8f69-a94b6332cb73");
            if(user == null)
                return buildBadRequestResponse("Could not find user: 294c7ff2-6dbd-4522-8f69-a94b6332cb73");
            Client client = database.getClient(oauthRequest.getClientId());
            if(client == null)
                return buildBadRequestResponse("Could not find client: " + oauthRequest.getClientId());

            //build response according to response_type
            String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);

            OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);

            if (responseType.equals(ResponseType.CODE.toString())) {
                String code = oauthIssuerImpl.authorizationCode();
                Authorisation authorisation = new Authorisation(client, user, code);
                database.updateAuthorisation(authorisation);
                builder.setCode(code);
            }

            String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
            final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();
            return Response.status(response.getResponseStatus())
                    .location(new URI(response.getLocationUri()))
                    .build();
        } catch (OAuthProblemException e) {
            OAuthResponse res = OAuthASResponse
                    .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                    .error(e)
                    .buildJSONMessage();
            return Response
                    .status(res.getResponseStatus()).entity(res.getBody())
                    .build();
        }
    }

    @POST
    @Path("/token")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public Response token(@Context HttpServletRequest request, MultivaluedMap<String, String> form) throws OAuthSystemException {
        try {
            OAuthTokenRequest oauthRequest = new OAuthTokenRequest(new OAuthRequestWrapper(request, form));
            OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

            // do checking for different grant types
            if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
                Authorisation authorisation = database.getAuthorisation(oauthRequest.getParam(OAuth.OAUTH_CODE));
                if (authorisation == null)
                    return buildBadRequestResponse("Unknown auth code: " + oauthRequest.getParam(OAuth.OAUTH_CODE));
                else {
                    String tokenString = oauthIssuerImpl.accessToken();
                    // todo get the user the token is for
                    Token token = new Token(authorisation.getClient(), authorisation.getUser(), tokenString);
                    database.updateToken(token);

                    OAuthResponse response = OAuthASResponse
                            .tokenResponse(HttpServletResponse.SC_OK)
                            .setAccessToken(tokenString)
                            .setExpiresIn("3600")
                            .buildJSONMessage();

                    database.deleteAuthorisation(authorisation.getCode());

                    return Response.status(response.getResponseStatus())
                            .entity(response.getBody()).build();
                }
            } else
                // refresh token is not supported in this implementation
                return buildBadRequestResponse("Unknown grant type: " + oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE));

        } catch (OAuthProblemException e) {
            OAuthResponse res = OAuthASResponse
                    .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                    .error(e)
                    .buildJSONMessage();
            return Response
                    .status(res.getResponseStatus())
                    .entity(res.getBody())
                    .build();
        }
    }

    private Response buildBadRequestResponse(String message) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(message)
                .build();
    }
}
