package com.intuso.housemate.globalserver;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.proxy.object.ProxyServer;
import com.intuso.housemate.webserver.SessionUtils;
import com.intuso.housemate.webserver.database.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by tomc on 05/04/17.
 */
public class ServerFilter implements Filter {

    private final Servers servers;

    @Inject
    public ServerFilter(Servers servers) {
        this.servers = servers;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // nothing to do
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpSession session = httpRequest.getSession(false);
            if(session != null && SessionUtils.getServer(session) == null) {
                User user = SessionUtils.getUser(session);
                if (user != null) {
                    ProxyServer.Simple server = servers.getServer(user.getId());
                    if (server != null)
                        SessionUtils.setServer(session, server);
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // nothing to do
    }
}
