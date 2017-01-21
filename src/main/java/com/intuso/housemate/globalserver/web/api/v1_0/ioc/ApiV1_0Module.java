package com.intuso.housemate.globalserver.web.api.v1_0.ioc;

import com.intuso.housemate.globalserver.web.api.v1_0.ApiV1_0ResourceConfig;
import com.intuso.housemate.globalserver.web.jersey.JerseyResourceModule;
import com.intuso.housemate.globalserver.web.oauth.OAuthFilter;

/**
 * Created by tomc on 21/01/17.
 */
public class ApiV1_0Module extends JerseyResourceModule {

    public ApiV1_0Module() {
        super("/api/1.0/", ApiV1_0ResourceConfig.class);
    }

    @Override
    protected void configureServlets() {
        super.configureServlets();
        filter("/api/1.0/test").through(OAuthFilter.class);
    }
}
