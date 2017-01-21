package com.intuso.housemate.globalserver.web.oauth;

import com.intuso.housemate.globalserver.oauth.OAuthClientRepository;
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
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by tomc on 21/01/17.
 */
@Path("/")
public class OAuthResource {

    private final OAuthClientRepository oAuthClientRepository;

    @Inject
    public OAuthResource(OAuthClientRepository oAuthClientRepository) {
        this.oAuthClientRepository = oAuthClientRepository;
    }

    @GET
    @Path("/authz")
    public Response authorize(@Context HttpServletRequest request) throws URISyntaxException, OAuthSystemException {
        try {
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

            //build response according to response_type
            String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);

            OAuthASResponse.OAuthAuthorizationResponseBuilder builder =
                    OAuthASResponse.authorizationResponse(request,
                            HttpServletResponse.SC_FOUND);

            if (responseType.equals(ResponseType.CODE.toString())) {
                final String authorizationCode =
                        oauthIssuerImpl.authorizationCode();
                oAuthClientRepository.addAuthCode(authorizationCode);
                builder.setCode(authorizationCode);
            }

            String redirectURI =
                    oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
            final OAuthResponse response = builder
                    .location(redirectURI)
                    .buildQueryMessage();
            URI url = new URI(response.getLocationUri());
            return Response.status(response.getResponseStatus())
                    .location(url)
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
    public Response getToken(@Context HttpServletRequest request) throws OAuthSystemException {
        try {
            OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
            OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

            // check if clientid is valid
            if (!oAuthClientRepository.isValidClient(oauthRequest.getClientId(), oauthRequest.getClientSecret()))
                return buildBadRequestResponse("Invalid client id/secret");

            // do checking for different grant types
            if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
                if (!oAuthClientRepository.isValidAuthCode(oauthRequest.getParam(OAuth.OAUTH_CODE)))
                    return buildBadRequestResponse("Invalid auth code");
                else {
                    final String accessToken = oauthIssuerImpl.accessToken();
                    oAuthClientRepository.addToken(accessToken);

                    OAuthResponse response = OAuthASResponse
                            .tokenResponse(HttpServletResponse.SC_OK)
                            .setAccessToken(accessToken)
                            .setExpiresIn("3600")
                            .buildJSONMessage();
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
