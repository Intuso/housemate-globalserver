package com.intuso.housemate.globalserver.web.ioc;

import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.ServletModule;
import com.intuso.housemate.globalserver.web.api.ioc.ApiModule;
import com.intuso.housemate.globalserver.web.oauth.ioc.OAuthModule;

/**
 * Created by tomc on 21/01/17.
 */
public class WebModule extends ServletModule {
    @Override
    protected void configureServlets() {

        bind(GuiceFilter.class);

        install(new OAuthModule());
        install(new ApiModule());
    }
}
