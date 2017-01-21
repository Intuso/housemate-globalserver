package com.intuso.housemate.globalserver.web.api.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.globalserver.web.api.v1_0.ioc.ApiV1_0Module;

/**
 * Created by tomc on 21/01/17.
 */
public class ApiModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ApiV1_0Module());
    }
}
