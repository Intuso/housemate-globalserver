package com.intuso.housemate.globalserver.web.api.v1_0;

import com.intuso.housemate.globalserver.database.Database;
import com.intuso.housemate.globalserver.database.model.Client;
import com.intuso.housemate.globalserver.database.model.Page;
import com.intuso.housemate.globalserver.web.model.NewClient;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.UUID;

/**
 * Created by tomc on 21/01/17.
 */
@Path("/client")
public class ClientResource {

    private final Database database;

    @Inject
    public ClientResource(Database database) {
        this.database = database;
    }

    @GET
    @Produces("application/json")
    public Page<Client> list(@QueryParam("offset") long offset, @QueryParam("limit") int limit) {
        return database.listClients(offset, limit);
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Client create(NewClient newClient) {
        Client client = new Client(database.getUser(newClient.getUserId()),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                newClient.getName());
        database.addClient(client);
        return client;
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Client get(@PathParam("id") String id) {
        return database.getClient(id);
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public void delete(@PathParam("id") String id) {
        database.deleteClient(id);
    }
}
