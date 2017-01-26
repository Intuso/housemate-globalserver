package com.intuso.housemate.globalserver.ioc;

import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.globalserver.servers.ioc.ServersModule;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import com.intuso.utilities.webserver.ioc.WebServerModule;

import java.util.Set;

/**
 * Created by tomc on 23/01/17.
 */
public class GlobalServerModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new WebServerModule());
        install(new ServersModule());

        bind(ManagedCollectionFactory.class).to(ManagedCollectionFactoryImpl.class);
        bind(ManagedCollectionFactoryImpl.class).in(Scopes.SINGLETON);

        // bind empty set just in case there are no services bound
        Multibinder.newSetBinder(binder(), Service.class);
    }

    @Provides
    public ServiceManager getServiceManager(Set<Service> services) {
        return new ServiceManager(services);
    }
}
