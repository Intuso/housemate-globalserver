package com.intuso.housemate.globalserver.ioc;

import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.globalserver.ManagedCollectionFactoryImpl;
import com.intuso.housemate.globalserver.ServerFilter;
import com.intuso.housemate.globalserver.Servers;
import com.intuso.housemate.webserver.ioc.HousemateWebServerModule;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

import java.util.Set;

/**
 * Created by tomc on 23/01/17.
 */
public class GlobalServerModule extends AbstractModule {

    public static void configureDefaults(WriteableMapPropertyRepository defaultProperties) {
        // todo, eg webserver port
        HousemateWebServerModule.configureDefaults(defaultProperties);
    }

    private final PropertyRepository properties;

    public GlobalServerModule(PropertyRepository properties) {
        this.properties = properties;
    }

    @Override
    protected void configure() {

        bind(PropertyRepository.class).toInstance(properties);

        Multibinder.newSetBinder(binder(), Service.class).addBinding().to(Servers.class);
        bind(Servers.class).in(Scopes.SINGLETON);

        install(new HousemateWebServerModule(8090, ServerFilter.class));

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
