package com.intuso.housemate.globalserver.web.api.v1_0;

import com.intuso.housemate.globalserver.database.Database;
import com.intuso.housemate.globalserver.database.model.User;
import com.intuso.housemate.globalserver.database.model.Page;
import com.intuso.housemate.globalserver.web.model.NewUser;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.UUID;

/**
 * Created by tomc on 21/01/17.
 */
@Path("/user")
public class UserResource {

    private final Database database;

    @Inject
    public UserResource(Database database) {
        this.database = database;
    }

    @GET
    @Produces("application/json")
    public Page<User> list(@QueryParam("offset") long offset, @QueryParam("limit") int limit) {
        return database.listUsers(offset, limit);
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public User create(NewUser newUser) {
        User user = new User(UUID.randomUUID().toString());
        database.addUser(user);
        return user;
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public User get(@PathParam("id") String id) {
        return database.getUser(id);
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public void delete(@PathParam("id") String id) {
        database.deleteUser(id);
    }
}
