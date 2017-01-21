package com.intuso.housemate.globalserver.web.api.v1_0;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by tomc on 21/01/17.
 */
@Path("/api/1.0")
public class ApiV1_0Resource {

    @Path("/test")
    @GET
    public String test() {
        return "Success!!";
    }
}
