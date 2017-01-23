package com.intuso.housemate.globalserver.api.oauth.v1_0.ioc;

import com.google.inject.Scopes;
import com.intuso.housemate.globalserver.api.oauth.v1_0.OAuthFilter;
import com.intuso.utilities.webserver.ioc.JerseyResourcesModule;

/**
 * Created by tomc on 21/01/17.
 */
public class OAuthV1_0Module extends JerseyResourcesModule {

    public OAuthV1_0Module() {
        super("/api/oauth/1.0/", OAuthV1_0ResourceConfig.class);
    }

    @Override
    protected void configureServlets() {
        super.configureServlets();
        bind(OAuthFilter.class).in(Scopes.SINGLETON);
    }
}
