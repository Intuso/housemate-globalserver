package com.intuso.housemate.globalserver.web.api.globalserver.v1_0;

import com.intuso.housemate.globalserver.database.Database;
import com.intuso.housemate.globalserver.web.SessionUtils;
import com.intuso.housemate.globalserver.web.api.globalserver.v1_0.model.LoginResponse;
import com.intuso.housemate.globalserver.web.api.globalserver.v1_0.model.User;
import com.intuso.housemate.globalserver.web.security.Hasher;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.IOException;

/**
 * Created by tomc on 21/01/17.
 */
@Path("/session")
public class SessionResource {

    private final Database database;
    private final Hasher hasher;

    @Inject
    public SessionResource(Database database, Hasher hasher) {
        this.database = database;
        this.hasher = hasher;
    }

    @POST
    @Path("/logout")
    public void logout(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        if(session != null)
            session.invalidate();
    }

    @POST
    @Path("/login")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public LoginResponse login(@FormParam("email") String email,
                               @FormParam("password") String password,
                               @Context HttpServletRequest request,
                               @Context HttpServletResponse response) throws IOException {
        if(email == null || email.length() == 0)
            return new LoginResponse("No email");
        else if(password == null || password.length() == 0)
            return new LoginResponse("No password provided");
        else {
            com.intuso.housemate.globalserver.database.model.User user = database.authenticateUser(email, hasher.hash(password));
            if (user != null) {
                HttpSession session = request.getSession(true);
                SessionUtils.setUser(session, user);
                session.setMaxInactiveInterval(7 * 24 * 60 * 60); // 1 week
                return new LoginResponse(User.from(user));
            } else
                return new LoginResponse("Bad email/password");
        }
    }
}
