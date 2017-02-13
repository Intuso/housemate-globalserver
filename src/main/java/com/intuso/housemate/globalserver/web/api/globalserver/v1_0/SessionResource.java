package com.intuso.housemate.globalserver.web.api.globalserver.v1_0;

import com.intuso.housemate.globalserver.database.Database;
import com.intuso.housemate.globalserver.database.model.User;
import com.intuso.housemate.globalserver.web.SessionUtils;
import com.intuso.housemate.globalserver.web.api.globalserver.v1_0.model.LoginResponse;
import com.intuso.housemate.globalserver.web.api.globalserver.v1_0.model.RegisterResponse;
import com.intuso.housemate.globalserver.web.security.Hasher;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by tomc on 21/01/17.
 */
@Path("/")
public class SessionResource {

    private final Database database;
    private final Hasher hasher;

    @Inject
    public SessionResource(Database database, Hasher hasher) {
        this.database = database;
        this.hasher = hasher;
    }

    @POST
    @Path("/register")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public RegisterResponse register(@FormParam("email") String email,
                                     @FormParam("password") String password,
                                     @Context HttpServletRequest request) throws IOException {

        RegisterResponse response = new RegisterResponse(false, false, false, false);

        // check the email and password are valid format
        if(email != null && email.length() > 0) // todo check format too?
            response.setValidEmail(true);
        if(password != null && password.length() > 0)
            response.setValidPassword(true);

        // if so ...
        if(response.isValidEmail() && response.isValidPassword()) {

            // check if there's already a user with that email address
            com.intuso.housemate.globalserver.database.model.User user = database.getUserByEmail(email);
            response.setAlreadyRegistered(user != null);

            // if there's not, create that user, and create them a session so they're logged in
            if(!response.isAlreadyRegistered()) {
                user = new User(UUID.randomUUID().toString(), email, null);
                database.updateUser(user);
                database.setUserPassword(user.getId(), hasher.hash(password));
                HttpSession session = request.getSession(true);
                SessionUtils.setUser(session, user);
                session.setMaxInactiveInterval(7 * 24 * 60 * 60); // 1 week
                response.setSuccess(true);
            }
        }
        return response;
    }

    @POST
    @Path("/login")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public LoginResponse login(@FormParam("email") String email,
                               @FormParam("password") String password,
                               @Context HttpServletRequest request) throws IOException {

        LoginResponse response = new LoginResponse(false, false, false, false);

        // check the email and password are valid format
        if(email != null && email.length() > 0) // todo check format too?
            response.setValidEmail(true);
        if(password != null && password.length() > 0)
            response.setValidPassword(true);

        // if so ....
        if(response.isValidEmail() && response.isValidPassword()) {

            // check a user with that email exists
            com.intuso.housemate.globalserver.database.model.User user = database.getUserByEmail(email);
            response.setKnownEmail(user != null);

            // if there is, check their password
            if (response.isKnownEmail()) {
                response.setCorrectPassword(database.authenticateUser(user.getId(), hasher.hash(password)));

                // if it's all good, then create them a session
                if(response.isCorrectPassword()) {
                    HttpSession session = request.getSession(true);
                    SessionUtils.setUser(session, user);
                    session.setMaxInactiveInterval(7 * 24 * 60 * 60); // 1 week
                }
            }
        }
        return response;
    }

    @POST
    @Path("/logout")
    public void logout(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        if(session != null)
            session.invalidate();
    }
}
