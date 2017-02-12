package com.intuso.housemate.globalserver.web.api.globalserver.v1_0;

import com.intuso.housemate.globalserver.web.SessionAttributes;
import com.intuso.housemate.globalserver.web.api.globalserver.v1_0.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

/**
 * Created by tomc on 21/01/17.
 */
@Path("/currentuser")
public class CurrentUserResource {

    @GET
    @Produces("application/json")
    public User getMe(@Context HttpServletRequest request) {
        return (User) request.getSession().getAttribute(SessionAttributes.USER);
    }
}
