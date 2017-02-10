package com.intuso.housemate.globalserver.web.api.globalserver.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.globalserver.web.api.globalserver.v1_0.ioc.GlobalServerV1_0Module;

/**
 * Created by tomc on 21/01/17.
 */
public class GlobalServerApiModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new GlobalServerV1_0Module());
    }
}
