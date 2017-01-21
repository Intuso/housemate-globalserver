package com.intuso.housemate.globalserver.web.oauth.ioc;

import com.google.inject.Scopes;
import com.intuso.housemate.globalserver.web.jersey.JerseyResourceModule;
import com.intuso.housemate.globalserver.web.oauth.OAuthFilter;
import com.intuso.housemate.globalserver.web.oauth.OAuthResourceConfig;

/**
 * Created by tomc on 21/01/17.
 */
public class OAuthModule extends JerseyResourceModule {

    public OAuthModule() {
        super("/oauth/", OAuthResourceConfig.class);
    }

    @Override
    protected void configureServlets() {
        super.configureServlets();
        bind(OAuthFilter.class).in(Scopes.SINGLETON);
    }
}
