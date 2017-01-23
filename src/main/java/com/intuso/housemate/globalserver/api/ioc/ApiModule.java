package com.intuso.housemate.globalserver.api.ioc;

import com.google.inject.servlet.ServletModule;
import com.intuso.housemate.globalserver.api.globalserver.ioc.GlobalServerApiModule;
import com.intuso.housemate.globalserver.api.oauth.ioc.OAuthApiModule;
import com.intuso.housemate.globalserver.api.server.ioc.ServerApiModule;

/**
 * Created by tomc on 21/01/17.
 */
public class ApiModule extends ServletModule {
    @Override
    protected void configureServlets() {
        install(new GlobalServerApiModule());
        install(new OAuthApiModule());
        install(new ServerApiModule());
    }
}
