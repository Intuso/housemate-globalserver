package com.intuso.housemate.globalserver.api.server.v1_0.ioc;

import com.intuso.housemate.globalserver.api.oauth.v1_0.OAuthFilter;
import com.intuso.utilities.webserver.ioc.JerseyResourcesModule;

/**
 * Created by tomc on 21/01/17.
 */
public class ServerV1_0Module extends JerseyResourcesModule {

    public ServerV1_0Module() {
        super("/api/server/1.0/", ServerV1_0ResourceConfig.class);
    }

    @Override
    protected void configureServlets() {
        super.configureServlets();
        filter("/api/server/1.0/*").through(OAuthFilter.class);
    }
}
