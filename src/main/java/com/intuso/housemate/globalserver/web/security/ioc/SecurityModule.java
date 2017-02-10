package com.intuso.housemate.globalserver.web.security.ioc;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import com.intuso.housemate.globalserver.web.security.Hasher;
import com.intuso.housemate.globalserver.web.security.SecurityFilter;

/**
 * Created by tomc on 21/01/17.
 */
public class SecurityModule extends ServletModule {
    @Override
    protected void configureServlets() {
        super.configureServlets();
        bind(SecurityFilter.class).in(Scopes.SINGLETON);
        bind(Hasher.class).in(Scopes.SINGLETON);
        filter("*").through(SecurityFilter.class);
    }
}
