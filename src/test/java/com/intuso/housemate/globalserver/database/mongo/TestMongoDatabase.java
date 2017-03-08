package com.intuso.housemate.globalserver.database.mongo;

import com.google.common.collect.Maps;
import com.intuso.housemate.globalserver.database.model.Authorisation;
import com.intuso.housemate.globalserver.database.model.Client;
import com.intuso.housemate.globalserver.database.model.Token;
import com.intuso.housemate.globalserver.database.model.User;
import com.intuso.housemate.globalserver.ioc.ManagedCollectionFactoryImpl;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tomc on 21/01/17.
 */
@Ignore // more of an integration test. Needs mongo running
public class TestMongoDatabase {

    private final ManagedCollectionFactory managedCollectionFactory = new ManagedCollectionFactoryImpl();
    private final MongoDatabaseImpl mongoDatabase = new MongoDatabaseImpl(new WriteableMapPropertyRepository(managedCollectionFactory, Maps.newHashMap()), managedCollectionFactory);

    @After
    public void cleanup() {
        mongoDatabase.deleteUser("aUser");
        mongoDatabase.deleteClient("aClient");
        mongoDatabase.deleteAuthorisation("anAuthorisation");
        mongoDatabase.deleteToken("aToken");
    }

    @Test
    public void testCRDUser() {
        User user = new User("aUser", "name@domain.com", "some.server.com:1234");
        mongoDatabase.updateUser(user);
        user = mongoDatabase.getUser("aUser");
        assertNotNull(user);
        assertEquals("aUser", user.getId());
        assertEquals("name@domain.com", user.getEmail());
        assertEquals("some.server.com:1234", user.getServerAddress());
        mongoDatabase.deleteUser("aUser");
        user = mongoDatabase.getUser("aUser");
        assertNull(user);
    }

    @Test
    public void testCRDClient() {
        User user = new User("aUser", "name@domain.com", "some.server.com:1234");
        mongoDatabase.updateUser(user);
        Client client = new Client(user, "aClient", "someSecret", "A Test Client");
        mongoDatabase.updateClient(client);
        client = mongoDatabase.getClient("aClient");
        assertNotNull(client);
        assertNotNull(client.getOwner());
        assertEquals("aUser", client.getOwner().getId());
        assertEquals("aClient", client.getId());
        assertEquals("someSecret", client.getSecret());
        assertEquals("A Test Client", client.getName());
        mongoDatabase.deleteClient("aClient");
        client = mongoDatabase.getClient("aClient");
        assertNull(client);
    }

    @Test
    public void testCRDAuthorisation() {
        User user = new User("aUser", "name@domain.com", "some.server.com:1234");
        mongoDatabase.updateUser(user);
        Client client = new Client(user, "aClient", "someSecret", "A Test Client");
        mongoDatabase.updateClient(client);
        Authorisation authorisation = new Authorisation(client, user, "anAuthorisation");
        mongoDatabase.updateAuthorisation(authorisation);
        authorisation = mongoDatabase.getAuthorisation("anAuthorisation");
        assertNotNull(authorisation);
        assertNotNull(authorisation.getUser());
        assertEquals("aUser", authorisation.getUser().getId());
        assertNotNull(authorisation.getClient());
        assertEquals("aClient", authorisation.getClient().getId());
        assertEquals("anAuthorisation", authorisation.getCode());
        mongoDatabase.deleteAuthorisation("anAuthorisation");
        authorisation = mongoDatabase.getAuthorisation("anAuthorisation");
        assertNull(authorisation);
    }

    @Test
    public void testCRDToken() {
        User user = new User("aUser", "name@domain.com", "some.server.com:1234");
        mongoDatabase.updateUser(user);
        Client client = new Client(user, "aClient", "someSecret", "A Test Client");
        mongoDatabase.updateClient(client);
        Token token = new Token("aToken", client, user, "token", "refresh", 123456789);
        mongoDatabase.updateToken(token);
        token = mongoDatabase.getToken("aToken");
        assertNotNull(token);
        assertEquals("aToken", token.getId());
        assertNotNull(token.getUser());
        assertEquals("aUser", token.getUser().getId());
        assertNotNull(token.getClient());
        assertEquals("aClient", token.getClient().getId());
        assertEquals("token", token.getToken());
        assertEquals("refresh", token.getRefreshToken());
        assertEquals(123456789L, token.getExpiresAt());
        mongoDatabase.deleteToken("aToken");
        token = mongoDatabase.getToken("aToken");
        assertNull(token);
    }
}
