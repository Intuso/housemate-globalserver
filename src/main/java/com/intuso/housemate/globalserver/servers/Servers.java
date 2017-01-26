package com.intuso.housemate.globalserver.servers;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.client.v1_0.proxy.simple.SimpleProxyServer;
import com.intuso.housemate.globalserver.database.Database;
import com.intuso.housemate.globalserver.database.model.Client;
import com.intuso.housemate.globalserver.database.model.User;
import com.intuso.housemate.globalserver.servers.ioc.ServerModule;
import com.intuso.utilities.listener.ManagedCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by tomc on 23/01/17.
 */
public class Servers extends AbstractIdleService implements Database.Listener {

    private final static Logger logger = LoggerFactory.getLogger(Servers.class);

    private final Injector injector;
    private final Database database;
    private ManagedCollection.Registration listenerRegistration;

    private final Map<String, SimpleProxyServer> servers = Maps.newHashMap();

    @Inject
    public Servers(Injector injector, Database database) {
        this.injector = injector;
        this.database = database;
    }

    public SimpleProxyServer getServer(String userId) {
        return servers.get(userId);
    }

    @Override
    protected void startUp() throws Exception {
        listenerRegistration = database.addListener(this);
        database.getUsers().forEach(this::createServerForUser);
    }

    @Override
    protected void shutDown() throws Exception {
        if(listenerRegistration == null) {
            this.listenerRegistration.remove();
            listenerRegistration = null;
        }
        for(SimpleProxyServer server : servers.values())
            server.stop();
        servers.clear();
    }

    @Override
    public void userUpdated(User user) {
        createServerForUser(user);
    }

    @Override
    public void clientUpdated(Client client) {
        // do nothing
    }

    private void createServerForUser(User user) {
        try {
            String serverAddress = user.getServerAddress();
            if (serverAddress != null) {
                SimpleProxyServer server = injector.createChildInjector(new ServerModule(user.getId(), user.getServerAddress())).getInstance(SimpleProxyServer.class);
                server.start();
                servers.put(user.getId(), server);
            }
        } catch(Throwable t) {
            logger.error("Failed to create server for user {}", user.getId(), t);
        }
    }
}
