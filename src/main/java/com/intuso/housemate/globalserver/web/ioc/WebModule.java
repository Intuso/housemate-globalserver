package com.intuso.housemate.globalserver.web.ioc;

import com.google.inject.servlet.ServletModule;
import com.intuso.housemate.globalserver.web.api.ioc.ApiModule;
import com.intuso.housemate.globalserver.web.security.ioc.SecurityModule;

/**
 * Created by tomc on 21/01/17.
 */
public class WebModule extends ServletModule {
    @Override
    protected void configureServlets() {
        install(new ApiModule());
        install(new SecurityModule());
    }
}
