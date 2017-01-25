package com.intuso.housemate.globalserver.api.globalserver.v1_0;

import com.intuso.housemate.globalserver.api.globalserver.v1_0.model.Client;
import com.intuso.housemate.globalserver.api.globalserver.v1_0.model.ClientNoIdSecret;
import com.intuso.housemate.globalserver.api.globalserver.v1_0.model.Page;
import com.intuso.housemate.globalserver.database.Database;

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
    public Page<Client> list(@QueryParam("offset") @DefaultValue("0") long offset, @QueryParam("limit") @DefaultValue("20")  int limit) {
        return Page.from(database.getClientPage(offset, limit), Client::from);
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Client create(ClientNoIdSecret clientNoIdSecret) {
        com.intuso.housemate.globalserver.database.model.Client client = new com.intuso.housemate.globalserver.database.model.Client(
                database.getUser(clientNoIdSecret.getUserId()),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                clientNoIdSecret.getName());
        database.updateClient(client);
        return Client.from(client);
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Client get(@PathParam("id") String id) {
        return Client.from(database.getClient(id));
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Client update(@PathParam("id") String id, ClientNoIdSecret clientNoIdSecret) {
        com.intuso.housemate.globalserver.database.model.Client existing = database.getClient(id);
        if(clientNoIdSecret.getName() != null)
            existing.setName(clientNoIdSecret.getName());
        if(clientNoIdSecret.getUserId() != null)
            existing.setOwner(database.getUser(clientNoIdSecret.getUserId()));
        database.updateClient(existing);
        return Client.from(existing);
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public void delete(@PathParam("id") String id) {
        database.deleteClient(id);
    }
}
