package com.intuso.housemate.globalserver.servers.ioc;

import com.google.common.util.concurrent.Service;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.globalserver.servers.Servers;

/**
 * Created by tomc on 24/01/17.
 */
public class ServersModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder.newSetBinder(binder(), Service.class).addBinding().to(Servers.class);
        bind(Servers.class).in(Scopes.SINGLETON);
    }
}
