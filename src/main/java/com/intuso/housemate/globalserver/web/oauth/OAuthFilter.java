package com.intuso.housemate.globalserver.web.oauth;

import com.google.inject.Inject;
import com.intuso.housemate.globalserver.database.Database;
import com.intuso.housemate.globalserver.database.model.Token;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by tomc on 21/01/17.
 */
public class OAuthFilter implements Filter {

    private final Database database;

    @Inject
    public OAuthFilter(Database database) {
        this.database = database;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            try {

                // Make the OAuth Request out of this request
                OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest((HttpServletRequest) request, ParameterStyle.QUERY);

                // Get the access token
                String tokenString = oauthRequest.getAccessToken();

                // get the token object
                Token token = database.getToken(tokenString);

                // todo validate the client?
                // todo insert the user id into the request so resources know which user access is granted to
                if (token != null)
                    chain.doFilter(request, response);
                else {
                    // Return the OAuth error message
                    OAuthResponse oauthResponse = OAuthRSResponse
                            .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                            .setRealm("housemate")
                            .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                            .buildHeaderMessage();

                    //return Response.status(Response.Status.UNAUTHORIZED).build();
                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                    httpServletResponse.sendError(Response.Status.UNAUTHORIZED.getStatusCode(), Response.Status.UNAUTHORIZED.getReasonPhrase());
                    httpServletResponse.setHeader(OAuth.HeaderType.WWW_AUTHENTICATE, oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
                }
            } catch (OAuthSystemException | OAuthProblemException e) {
                throw new ServletException("Could not validate oauth client/token", e);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
