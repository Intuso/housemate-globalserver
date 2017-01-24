package com.intuso.housemate.globalserver.servers.ioc;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.intuso.housemate.globalserver.GlobalServerException;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 24/01/17.
 */
public class ConnectionProvider implements Provider<Connection> {

    private final String address;

    @Inject
    public ConnectionProvider(@ServerAddress String address) {
        this.address = address;
    }

    @Override
    public Connection get() {
        try {
            Connection connection = new ActiveMQConnectionFactory("tcp://" + address).createConnection();
            connection.start();
            return connection;
        } catch (JMSException e) {
            throw new GlobalServerException("Failed to create connection to broker", e);
        }
    }
}
