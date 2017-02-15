package com.intuso.housemate.globalserver.web.api.ioc;

import com.google.inject.servlet.ServletModule;
import com.intuso.housemate.globalserver.web.api.oauth.ioc.OAuthApiModule;
import com.intuso.housemate.globalserver.web.api.server.ioc.ServerApiModule;

/**
 * Created by tomc on 21/01/17.
 */
public class ApiModule extends ServletModule {
    @Override
    protected void configureServlets() {
        install(new OAuthApiModule());
        install(new ServerApiModule());
    }
}
