package com.intuso.housemate.globalserver.web.security;

import com.google.common.net.UrlEscapers;
import com.google.inject.Inject;
import com.intuso.housemate.globalserver.database.Database;
import com.intuso.housemate.globalserver.database.model.Token;
import com.intuso.housemate.globalserver.web.SessionUtils;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by tomc on 21/01/17.
 */
public class SecurityFilter implements Filter {

    public final static String OAUTH_TOKEN = "/api/oauth/1.0/token";

    public final static String LOGIN_HTML = "/login/index.html";
    public final static String LOGIN_JS = "/js/login.js";
    public final static String LOGIN_1_0_ENDPOINT = "/api/globalserver/1.0/session/login";

    public final static String NEXT_PARAM = "next";

    public final static String X_FORWARDED_FOR = "X-Forwarded-For";

    private final Database database;

    @Inject
    public SecurityFilter(Database database) {
        this.database = database;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {

            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            if (isValidOAuthRequest(httpRequest))
                chain.doFilter(request, response);
            else if (isValidSession(httpRequest))
                chain.doFilter(request, response);
            else if (isPartOfAuthFlow(httpRequest))
                chain.doFilter(request, response);

            // not authorised to access the resource so redirect to login page
            else {
                String encodedURL = UrlEscapers.urlPathSegmentEscaper().escape(getOriginalUrl(httpRequest));
                httpResponse.sendRedirect(httpRequest.getContextPath() + httpRequest.getContextPath() + LOGIN_HTML + "?" + NEXT_PARAM + "=" + encodedURL);
            }
        }

        // not an http request, just ignore it
    }

    private boolean isValidOAuthRequest(HttpServletRequest request) {
        try {

            // Make the OAuth Request out of this request
            OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.HEADER);

            // Get the access token
            String tokenString = oauthRequest.getAccessToken();

            // get the token object
            Token token = database.getToken(tokenString);

            if(token != null) {
                HttpSession session = request.getSession(true);
                SessionUtils.setUser(session, token.getUser());
                SessionUtils.setClient(session, token.getClient());
            }
            return token != null;

        } catch (OAuthSystemException | OAuthProblemException e) {
            return false;
        }
    }

    private boolean isValidSession(HttpServletRequest request) {
        return request.getSession(false) != null;
    }

    private boolean isPartOfAuthFlow(HttpServletRequest request) {
        return
                (request.getMethod().equals("GET")
                        && (request.getRequestURI().equals(LOGIN_HTML)
                                || request.getRequestURI().equals(LOGIN_JS)))
                || (request.getMethod().equals("POST")
                        && (request.getRequestURI().equals(OAUTH_TOKEN)
                                || request.getRequestURI().equals(LOGIN_1_0_ENDPOINT)));
    }

    private String getOriginalUrl(HttpServletRequest httpRequest) {

        // get server and path info.
        String url;
        // If X-Forwarded-For header is set, use that as the base and append the uri
        if(httpRequest.getHeader(X_FORWARDED_FOR) != null)
            url = httpRequest.getHeader(X_FORWARDED_FOR) + httpRequest.getRequestURI();
        // otherwise use the full url
        else
            url = httpRequest.getRequestURL().toString();

        // if there are any query params, add them
        if(httpRequest.getQueryString() != null && httpRequest.getQueryString().length() == 0)
            url += "?" + httpRequest.getQueryString();

        return url;
    }
}
