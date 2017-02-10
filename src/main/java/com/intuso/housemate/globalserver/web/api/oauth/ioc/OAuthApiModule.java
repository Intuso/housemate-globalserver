package com.intuso.housemate.globalserver.web.api.oauth.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.globalserver.web.api.oauth.v1_0.ioc.OAuthV1_0Module;

/**
 * Created by tomc on 21/01/17.
 */
public class OAuthApiModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new OAuthV1_0Module());
    }
}
