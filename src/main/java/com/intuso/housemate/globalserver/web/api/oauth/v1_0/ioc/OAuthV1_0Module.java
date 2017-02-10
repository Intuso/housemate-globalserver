package com.intuso.housemate.globalserver.web.api.oauth.v1_0.ioc;

import com.intuso.utilities.webserver.ioc.JerseyResourcesModule;

/**
 * Created by tomc on 21/01/17.
 */
public class OAuthV1_0Module extends JerseyResourcesModule {
    public OAuthV1_0Module() {
        super("/api/oauth/1.0/", OAuthV1_0ResourceConfig.class);
    }
}
