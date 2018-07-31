package com.intuso.housemate.globalserver.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.intuso.housemate.client.v1_0.messaging.jms.ioc.JMSMessagingModule;
import com.intuso.housemate.client.v1_0.proxy.object.ioc.Server;
import com.intuso.housemate.client.v1_0.proxy.object.ioc.SimpleProxyModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;

/**
 * Created by tomc on 24/01/17.
 */
public class ClientServerModule extends AbstractModule {

    private final String userId;
    private final String address;

    public ClientServerModule(String userId, String address) {
        this.userId = userId;
        this.address = address;
    }

    @Override
    protected void configure() {
        install(new SimpleProxyModule());
        install(new JMSMessagingModule());
        bind(String.class).annotatedWith(ClientAddress.class).toInstance(address);
        bind(Connection.class).toProvider(ClientConnectionProvider.class).in(Scopes.SINGLETON);
    }

    @Provides
    @Server
    public Logger getServerLogger() {
        return LoggerFactory.getLogger("user." + userId);
    }
}
