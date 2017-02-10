package com.intuso.housemate.globalserver.web.api.globalserver.v1_0;

import com.intuso.housemate.globalserver.database.Database;
import com.intuso.housemate.globalserver.web.api.globalserver.v1_0.model.LoginDetails;
import com.intuso.housemate.globalserver.web.api.globalserver.v1_0.model.LoginResponse;
import com.intuso.housemate.globalserver.web.api.globalserver.v1_0.model.User;
import com.intuso.housemate.globalserver.web.security.Hasher;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    @Path("/login")
    @Produces("application/json")
    @Consumes("application/json")
    public LoginResponse login(LoginDetails loginDetails,
                               @Context HttpServletRequest request,
                               @Context HttpServletResponse response) throws IOException {
        if(loginDetails == null)
            return new LoginResponse("No details provided");
        else if(loginDetails.getEmail() == null || loginDetails.getEmail().length() == 0)
            return new LoginResponse("No email");
        else if(loginDetails.getPassword() == null || loginDetails.getPassword().length() == 0)
            return new LoginResponse("No password provided");
        else {
            User user = User.from(database.authenticateUser(loginDetails.getEmail(), hasher.hash(loginDetails.getPassword())));
            if (user != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                //setting session to expiry in 30 mins
                session.setMaxInactiveInterval(7 * 24 * 60 * 60);
                Cookie userName = new Cookie("user", loginDetails.getEmail());
                userName.setMaxAge(7 * 24 * 60 * 60);
                response.addCookie(userName);
                return new LoginResponse(user);
            } else
                return new LoginResponse("Bad email/password");
        }
    }

    @POST
    @Path("/logout")
    public void logout(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        if(session != null)
            session.invalidate();
    }
}
