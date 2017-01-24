package com.intuso.housemate.globalserver.servers.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.intuso.housemate.client.v1_0.proxy.simple.ioc.Server;
import com.intuso.housemate.client.v1_0.proxy.simple.ioc.SimpleProxyModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;

/**
 * Created by tomc on 24/01/17.
 */
public class ServerModule extends AbstractModule {

    private final String userId;
    private final String address;

    public ServerModule(String userId, String address) {
        this.userId = userId;
        this.address = address;
    }

    @Override
    protected void configure() {
        install(new SimpleProxyModule());
        bind(String.class).annotatedWith(ServerAddress.class).toInstance(address);
        bind(Connection.class).toProvider(ConnectionProvider.class);
    }

    @Provides
    @Server
    public Logger getServerLogger() {
        return LoggerFactory.getLogger("user." + userId);
    }
}
